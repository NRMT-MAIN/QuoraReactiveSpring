package com.example.demo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Question;

@Repository
public interface IQuestionRepository extends ReactiveMongoRepository<Question, String> {

}
