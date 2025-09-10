package com.example.demo.services;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
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
		// TODO Auto-generated method stub
		return questRepo.findById(id)
				.map(QuestionAdapter :: toQuestionResponseDTO)
				.doOnError(error -> System.out.println("Error fetching questions: " + error))
	            .doOnSuccess(response -> System.out.println("Questions fetched successfully " + response));
				
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
