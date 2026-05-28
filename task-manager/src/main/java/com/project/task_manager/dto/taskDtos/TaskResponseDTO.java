package com.project.task_manager.dto.taskDtos;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Integer id;
    private String title;
    private String description;
    private boolean status;
}
