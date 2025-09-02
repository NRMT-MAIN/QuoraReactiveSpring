package com.example.demo.services;

import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionResponseDTO;

import reactor.core.publisher.Mono;

public interface IQuestionService {
	 public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO);

	 public Mono<QuestionResponseDTO> getQuestionById(String id);
}
