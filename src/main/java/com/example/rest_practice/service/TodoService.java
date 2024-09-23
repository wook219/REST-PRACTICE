package com.example.rest_practice.service;

import com.example.rest_practice.model.TodoEntity;
import com.example.rest_practice.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    //update
    public List<TodoEntity> update(final TodoEntity todoEntity){
        // 1. 엔티티가 유효한지 확인.
        validate(todoEntity);

        // 2. 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져옴. 존재하지 않는 엔티티는 업데이트할 수 없기 떄문.
        final Optional<TodoEntity> original = todoRepository.findById(todoEntity.getId());

        /* 아래와 동일하다.
        if(original.ifPresent()){
            final TodoEntity entity = original.get();
            entity.setTitle(todoEntity.getTitle());
            entity.setDone(todoEntity.isDone());

            todoRepository.save(entity);
        }
         */

        original.ifPresent(todo -> {
            // 3. 반환된 TodoEntity가 존재하면 새 entity의 값으로 덮어 씌운다.
            todo.setTitle(todoEntity.getTitle());
            todo.setDone(todoEntity.isDone());

            // 4. DB에 새 값을 저장
            todoRepository.save(todo);
        });

        // 5. 위에서 정의한 retrieve 메서드로 유저의 모든 Todo리스트를 리턴
        return retrieve(todoEntity.getUserId());
    }

    //delete
    public List<TodoEntity> delete(final TodoEntity todoEntity){
        // 1. 검증
        validate(todoEntity);

        try{
            // 2. 엔티티 삭제
            todoRepository.delete(todoEntity);
        }catch (Exception e){
            // 3. exception 발생 시 id와 exception 로깅
            log.error("error deleting entity ", todoEntity.getId(), e);

            // 4. 컨트롤러로 exception을 날린다. DB 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴
            throw new RuntimeException("error deleting entity " + todoEntity.getId());
        }

        // 새 Todo리스트를 가져와 리턴
        return retrieve(todoEntity.getUserId());
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
