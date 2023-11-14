package com.hacker.hackathon.controller;


import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.config.resolver.ServiceToken;
import com.hacker.hackathon.dto.authorization.request.SigninRequestDTO;
import com.hacker.hackathon.dto.authorization.request.SignupRequestDTO;
import com.hacker.hackathon.dto.authorization.response.SignupResponseDTO;
import com.hacker.hackathon.dto.authorization.response.TokenServiceVO;
import com.hacker.hackathon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> signup(
        @RequestBody SignupRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

    @PostMapping("/auth/token")
    public ApiResponse<TokenServiceVO> reIssueToken(@ServiceToken TokenServiceVO token) {
        TokenServiceVO data = authService.reIssueToken(token);
        return ApiResponse.success(SuccessMessage.TOKEN_RE_ISSUE_SUCCESS, data);
    }



    @PostMapping("/auth/signin")
    public ApiResponse<TokenServiceVO> signin(
        @RequestBody SigninRequestDTO requestDTO
    ) {
        TokenServiceVO data = authService.signinService(requestDTO);
        return ApiResponse.success(SuccessMessage.LOGIN_SUCCESS, data);
    }


}
