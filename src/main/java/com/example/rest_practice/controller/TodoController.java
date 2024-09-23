package com.example.rest_practice.controller;

import com.example.rest_practice.dto.ResponseDTO;
import com.example.rest_practice.dto.TodoDTO;
import com.example.rest_practice.model.TodoEntity;
import com.example.rest_practice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = todoService.testService();

        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO
                .<String>builder()
                .data(list)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO){
        try{
            String temporaryUserId = "temporary-user";

            // 1. TodoEntity로 변환
            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

            // 2. id를 null로 초기화. 생성 당시에는 id가 없어야 하기 때문이다.
            todoEntity.setId(null);

            // 3. 임시 유저 아이디를 설정. 4장 인증과 인가에서 수정할 예정.
            todoEntity.setUserId(temporaryUserId);

            // 4. 서비스를 이용해 Todo엔티티 생성
            List<TodoEntity> entities = todoService.create(todoEntity);

            // 5. 스트림을 이용해 Entity -> DTO
            List<TodoDTO> todoDTOS = entities.stream()
                    .map(TodoDTO::new)
                    .collect(Collectors.toList());

            // 6. 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(todoDTOS)
                    .build();

            // 7. ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            // 8. 예외 발생 시 dto 대신 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(error)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporary-user";

        // 1. 서비스 메서드의 retrieve 메서드를 사용해 Todo리스트 가져옴
        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);

        // 2. 스트림을 이용해 리턴된 엔티티 리스트를 DTO로 변환
        List<TodoDTO> todoDTOS = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        // 3. 변환된 DTO를 이용해 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(todoDTOS)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todoDTO){
        String temporaryUserId = "temporary-user";

        // 1. DTO -> Entity
        TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

        // 2. userId 초기화
        todoEntity.setUserId(temporaryUserId);

        // 3. service를 이용해 entity update
        List<TodoEntity> entities = todoService.update(todoEntity);

        // 4. 스트림을 이용해 Entity -> DTO
        List<TodoDTO> todoDTOS = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        // 5. ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(todoDTOS)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
