package com.example.rest_practice.service;

import com.example.rest_practice.model.TodoEntity;
import com.example.rest_practice.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

    //create
    public List<TodoEntity> create(final TodoEntity todoEntity){
        //Validations
        validate(todoEntity);

        // 엔티티를 DB에 저장 후 로그를 남긴다.
        todoRepository.save(todoEntity);
        log.info("Entity Id : {} is saved", todoEntity.getId());

        //저장된 엔티티를 포함하는 새 리스트를 리턴한다.
        return todoRepository.findByUserId(todoEntity.getUserId());
    }

    //retrieve
    public List<TodoEntity> retrieve(final String userId){
        log.info("result of Entity userId : {}", userId);

        return todoRepository.findByUserId(userId);
    }


    private void validate(TodoEntity todoEntity) {
        if(todoEntity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(todoEntity.getUserId() == null){
            log.warn("Unknown User.");
            throw new RuntimeException("Unknown User.");
        }
    }
}
