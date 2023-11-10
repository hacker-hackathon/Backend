package com.hacker.hackathon.service;

import com.hacker.hackathon.dto.TranscriptDTO;
import com.hacker.hackathon.dto.TranscriptDataDTO;
import com.hacker.hackathon.model.Quiz;
import com.hacker.hackathon.model.Transcript;
import com.hacker.hackathon.model.Video;
import com.hacker.hackathon.repository.QuizRepository;
import com.hacker.hackathon.repository.TranscriptRepository;
import com.hacker.hackathon.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TranscriptService {

    private final WebClient webClient;
    private final VideoRepository videoRepository;

    private final TranscriptRepository transcriptRepository;

    public TranscriptService(WebClient.Builder webClientBuilder, VideoRepository videoRepository, TranscriptRepository transcriptRepository,
                             QuizRepository quizRepository) {
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
        this.quizRepository = quizRepository;
    }

    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();
    private final QuizRepository quizRepository;

    public Video fetchTranscripts(String link) throws Exception {
        List<Map> youtubeData;
        try {
            youtubeData = webClient.post()
                    .uri("http://localhost:5001/quiz/" + link)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            throw new Exception("An error occurred while retrieving the transcripts", e);
        }

        return createVideo(link, youtubeData);
    }

    private Video createVideo(String link, List<Map> youtubeData) {
        Video video = new Video();
        video.setLink(link);
        videoRepository.save(video);
        if (youtubeData != null && !youtubeData.isEmpty()) {
            Map<String, Object> details = youtubeData.get(0);
            video.setTitle((String) details.get("title"));
            video.setCreator((String) details.get("creator"));
            Object durationObject = details.get("duration");
            if (durationObject != null) {
                String durationString = durationObject.toString();
                long durationInSeconds = Long.parseLong(durationString);
                video.setDuration(durationInSeconds);
            }
            List<Map<String, Object>> quizzes = (List<Map<String, Object>>) details.get("quizs");
            if (quizzes != null) {
                for (Map<String, Object> quizMap : quizzes) {
                    Quiz quiz = new Quiz();
                    quiz.setQuestion((String) quizMap.get("question"));
                    quiz.setAnswer((Boolean) quizMap.get("answer"));
                    quiz.setS3Url((String) quizMap.get("s3Url"));
                    quiz.setVideo(video);
                    quizRepository.save(quiz);
                }
            }
        }

//        List<CompletableFuture<Transcript>> futures = new ArrayList<>();
//        assert youtubeData != null;
//        for (Map transcriptData : youtubeData) {
//            List<Map<String, Object>> transcripts = (List<Map<String, Object>>) transcriptData.get("transcripts");
//            if (transcripts != null) {
//                for (Map<String, Object> transcriptMap : transcripts) {
//                    futures.add(createTranscript(transcriptMap, video));
//                }
//            }
//        }
//
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//        for (CompletableFuture<Transcript> future : futures) {
//            video.getTranscripts().add(future.join());
//        }
//
          return videoRepository.save(video);
    }
/*    @Async("taskExecutor")
    public CompletableFuture<Transcript> createTranscript(Map<String, Object> transcriptMap, Video video) {
        Transcript transcript = new Transcript();
        transcript.setSentence((String) transcriptMap.get("text"));
        transcript.setStart(((Number) transcriptMap.get("start")).doubleValue());
        transcript.setDuration(((Number) transcriptMap.get("duration")).doubleValue());
        transcript.setVideo(video);
        transcript = transcriptRepository.save(transcript);
        return CompletableFuture.completedFuture(transcript);
    }*/

    public TranscriptDataDTO getTranscriptsByVideoId(Long videoId) {
        return videoRepository.findById(videoId)
                .map(video -> {
                    videoRepository.save(video);
                    List<TranscriptDTO> transcriptDtos = video.getTranscripts().stream()
                            .sorted(Comparator.comparing(Transcript::getStart)) // Sort by start
                            .map(t -> new TranscriptDTO(t.getTranscriptId(), t.getSentence(), t.getStart(), t.getDuration()))
                            .collect(Collectors.toList());
                    return new TranscriptDataDTO(videoId, transcriptDtos);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transcripts not found for video with id: " + videoId));
    }
    public TranscriptDTO getTranscriptByVideoIdAndTranscriptId(Long videoId, Long transcriptId) {
        Optional<Transcript> optionalTranscript = transcriptRepository.findByTranscriptIdAndVideoVideoId(transcriptId, videoId);

        if (!optionalTranscript.isPresent()) {
            throw new EntityNotFoundException("Transcript not found with id: " + transcriptId + " for video with id: " + videoId);
        }
        Transcript transcript = optionalTranscript.get();
        return new TranscriptDTO(transcript.getTranscriptId(), transcript.getSentence(), transcript.getStart(), transcript.getDuration());
    }
}
