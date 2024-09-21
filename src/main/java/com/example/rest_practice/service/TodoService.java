package com.example.rest_practice.service;

import com.example.rest_practice.model.TodoEntity;
import com.example.rest_practice.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    TodoRepository todoRepository;

    public String testService(){
        TodoEntity todoEntity = TodoEntity.builder().title("My first todo Item").build();
        todoRepository.save(todoEntity);

        TodoEntity savedEntity = todoRepository.findById(todoEntity.getId()).get();
        return savedEntity.getTitle();
    }
}
