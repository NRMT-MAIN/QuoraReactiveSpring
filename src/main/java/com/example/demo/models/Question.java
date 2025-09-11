package com.example.demo.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Quora_DB")
public class Question {

	@Id
	private String id; 
	
	@NotBlank(message = "Title is required")
    @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 1000, message = "Content must be between 10 and 1000 characters")
    private String content;

    private Integer views;
 
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
