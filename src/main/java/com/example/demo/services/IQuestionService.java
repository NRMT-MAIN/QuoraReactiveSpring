package com.example.demo.services;

import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionResponseDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {
	 public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);

	 public Mono<QuestionResponseDTO> getQuestionById(String id);
	 
	 public Flux<QuestionResponseDTO> getAllQuestions(String cursor , int pageSize) ; 
	 
	 public Flux<QuestionResponseDTO> searchQuestion(String searchTerm , int pageNo , int pageSize) ;
	 
	 Mono<Void> delete(String id) ; 
}
