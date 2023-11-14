package com.hacker.hackathon.service;


import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.controller.exception.NotFoundException;
import com.hacker.hackathon.controller.exception.TokenForbiddenException;
import com.hacker.hackathon.controller.exception.UserConflictException;
import com.hacker.hackathon.dto.authorization.request.SigninRequestDTO;
import com.hacker.hackathon.dto.authorization.request.SignupRequestDTO;
import com.hacker.hackathon.dto.authorization.response.SignupResponseDTO;
import com.hacker.hackathon.dto.authorization.response.TokenServiceVO;
import com.hacker.hackathon.model.Users;
import com.hacker.hackathon.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UsersRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SignupResponseDTO signupService(SignupRequestDTO requestDTO) {
        validateUserData(requestDTO);

        final Users newUser = userRepository.save(Users.of(requestDTO));
        TokenServiceVO tokenServiceVO = registerToken(newUser);

        return SignupResponseDTO.builder()
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
    }

    @Transactional
    public TokenServiceVO reIssueToken(@NotNull TokenServiceVO token){
        boolean isAccessTokenExpired = jwtService.isExpired(token.getAccessToken());
        boolean isRefreshTokenExpired = jwtService.isExpired(token.getRefreshToken());

        if(isAccessTokenExpired) {
            if(isRefreshTokenExpired){
                throw new TokenForbiddenException(ErrorMessage.EXPIRED_ALL_TOKEN_EXCEPTION);
            }
            final String refreshToken = token.getRefreshToken();
            return setNewAccessToken(refreshToken);
        }

        throw new TokenForbiddenException(ErrorMessage.VALID_ALL_TOKEN_EXCEPTION);
    }

    public TokenServiceVO signinService(SigninRequestDTO requestDTO) {
        userRepository.findByPassword(requestDTO.getPassword())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        Users user = userRepository.findByEmail(requestDTO.getEmail())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        TokenServiceVO tokenServiceVO = registerToken(user);

        return TokenServiceVO.of(tokenServiceVO.getAccessToken(), tokenServiceVO.getRefreshToken());
    }

    public void validateUserData(SignupRequestDTO requestDTO) {
        userRepository.findByPassword(requestDTO.getPassword())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_PASSWORD_EXCEPTION);
            });

        userRepository.findByNickname(requestDTO.getNickname())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_NICKNAME_EXCEPTION);
            });

    }

    public TokenServiceVO registerToken(Users user) {
        TokenServiceVO serviceToken = jwtService.createServiceToken(user.getUsersId());

        return serviceToken;
    }

    public TokenServiceVO setNewAccessToken(String refreshToken) {
        final Long userId = jwtService.getUserId(refreshToken);

        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        final String newAccessToken = jwtService.createAccessToken(user.getUsersId());
        final TokenServiceVO token = TokenServiceVO.of(newAccessToken, refreshToken);

        return token;
    }


}
