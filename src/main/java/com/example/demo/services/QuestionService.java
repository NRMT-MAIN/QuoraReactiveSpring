package com.example.demo.services;

import java.time.LocalDateTime;



import org.springframework.stereotype.Service;

import com.example.demo.adapter.QuestionAdapter;
import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.models.Question;
import com.example.demo.repositories.IQuestionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
	
	private final IQuestionRepository questRepo ; 
	
	@Override
	public Mono<QuestionResponseDTO> getQuestionById(String id) {
	    return questRepo.findById(id)
	        .doOnNext(question -> System.out.println("Fetched Question: " + question))
	        .map(QuestionAdapter::toQuestionResponseDTO)
	        .switchIfEmpty(Mono.error(new RuntimeException("Question not found for ID: " + id)))
	        .doOnError(error -> System.out.println("Error fetching question: " + error))
	        .doOnSuccess(response -> System.out.println("Question fetched successfully: " + response));
	}

	
	@Override
	public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO) {
		Question question = Question.builder()
	            .title(questionRequestDTO.getTitle())
	            .content(questionRequestDTO.getContent())
	            .createdAt(LocalDateTime.now())
	            .updatedAt(LocalDateTime.now())
	            .build();
		
		return questRepo.save(question)
				.map(QuestionAdapter :: toQuestionResponseDTO)
				.doOnSuccess(response -> System.out.println("Question created successfully: " + response))
		        .doOnError(error -> System.out.println("Error creating question: " + error));
	}

}
