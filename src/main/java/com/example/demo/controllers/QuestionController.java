package com.example.demo.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.services.IQuestionService;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

	private final IQuestionService questService ; 
	
	@PostMapping()
    public Mono<QuestionResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        return questService.createQuestion(questionRequestDTO)
        .doOnSuccess(response -> System.out.println("Question created successfully: " + response))
        .doOnError(error -> System.out.println("Error creating question: " + error));
    }
    
    @GetMapping("/{id}")
    public Mono<QuestionResponseDTO> getQuestionById(@PathVariable("id") String id) {
        return questService.getQuestionById(id)
        .doOnError(error -> System.out.println("Error fetching question: " + error))
        .doOnSuccess(response -> System.out.println("Question fetched successfully: " + response));
    }
    
    @GetMapping()  
    public Flux<QuestionResponseDTO> getAllQuestions(  
            @RequestParam(name = "cursor" , required = false) String cursor,  
            @RequestParam(name ="size" ,defaultValue = "10") int size  
    ) {  
        return questService.getAllQuestions(cursor, size)  
                .doOnError(error -> System.out.println("Error fetching questions: " + error))  
                .doOnComplete(() -> System.out.println("Questions fetched successfully"));  
    }  
  
    @DeleteMapping("/{id}")  
    public Mono<Void> deleteQuestionById(@PathVariable String id) {  
        return this.questService.delete(id)  
                .doOnSuccess(respone -> System.out.println("Question deleted successfully! : " + respone))  
                .doOnError(error -> System.out.println("Error in deleting question : " + error));  
    }  
  
    @GetMapping("/search")  
    public Flux<QuestionResponseDTO> searchQuestions(
            @RequestParam String query,  
            @RequestParam(defaultValue = "0") int offset,  
            @RequestParam(defaultValue = "10") int page  
    ) {  
        return questService.searchQuestion(query, offset, page);  
    }  
  
    @GetMapping("/tag/{tag}")  
    public Flux<QuestionResponseDTO> getQuestionsByTag(@PathVariable String tag,  
                                                       @RequestParam(defaultValue = "0") int page,  
                                                       @RequestParam(defaultValue = "10") int size  
    ) {  
        throw new UnsupportedOperationException("Not implemented");  
    }  
}
