package com.example.rest_practice.dto;

import com.example.rest_practice.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    private String id;
    private String title;
    private boolean done;

    public TodoDTO(final TodoEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity toEntity(final TodoDTO todoDTO){
        return TodoEntity.builder()
                .id(todoDTO.getId())
                .title(todoDTO.getTitle())
                .done(todoDTO.isDone())
                .build();
    }
}
