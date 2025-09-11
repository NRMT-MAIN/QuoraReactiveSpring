package com.example.demo.services;


import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.adapter.QuestionAdapter;
import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.events.ViewCountEvent;
import com.example.demo.models.Question;
import com.example.demo.producers.KafkaEventProducer;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.utils.CursorUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
	
	private final IQuestionRepository questRepo ; 
	private final KafkaEventProducer kafkaEventProducer;
	
	@Override
	public Mono<QuestionResponseDTO> getQuestionById(String id) {
		// TODO Auto-generated method stub
		return questRepo.findById(id)
				.map(QuestionAdapter :: toQuestionResponseDTO)
				.doOnError(error -> System.out.println("Error fetching questions: " + error))
	            .doOnSuccess(response -> {
		            	System.out.println("Questions fetched successfully " + response) ;
		            	ViewCountEvent viewCountEvent = new ViewCountEvent(id, "question", LocalDateTime.now());
		                kafkaEventProducer.publishViewCountEvent(viewCountEvent) ; 
	            	}
	            );
				
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
	
	@Override
	public Flux<QuestionResponseDTO> searchQuestion(String searchTerm, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize) ; 
		
		return questRepo.findByTitleOrContentContainingIgnoreCase(searchTerm, pageable)
				.map(QuestionAdapter :: toQuestionResponseDTO)
				.doOnError(error -> System.out.println("Error in searching the question : " + error))  
                .doOnComplete(() -> System.out.println("Questions searched successfully")) ; 
					
	}
	
	@Override
	public Flux<QuestionResponseDTO> getAllQuestions(String cursor , int pageSize) {
		Pageable pageable = PageRequest.of(0, pageSize) ; 
		if(!CursorUtils.isValidCursor(cursor)) {
			return questRepo.findTop10ByOrderByCreatedAtAsc()  
                    .take(pageSize)  
                    .map(QuestionAdapter::toQuestionResponseDTO)  
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))  
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
		} else {
			LocalDateTime cursorTimeStamp = CursorUtils.parseCursor(cursor);  
            return questRepo.findByCreatedAtGreaterThanOrderByCreatedAtAsc(cursorTimeStamp, pageable)  
                    .map(QuestionAdapter::toQuestionResponseDTO)  
                    .doOnError(error -> System.out.println("Error fetching questions: " + error))  
                    .doOnComplete(() -> System.out.println("Questions fetched successfully"));
		}
	}
	
	 @Override  
	    public Mono<Void> delete(String id) {  
	        questRepo.deleteById(id)  
	                .doOnSuccess(response -> System.out.println("Question deleted successfully! : " + response))  
	                .doOnError(error -> System.out.println("Error in deleting question : " + error));  
	        return Mono.empty();  
	    }  

}
