package com.project.task_manager.controller;

import com.project.task_manager.dto.taskDtos.TaskRequestDTO;
import com.project.task_manager.dto.taskDtos.TaskResponseDTO;
import com.project.task_manager.response.ApiResponse;
import com.project.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(@Valid @RequestBody TaskRequestDTO dto){
       TaskResponseDTO responseDTO = taskService.addTask(dto);
       ApiResponse<TaskResponseDTO> response = new ApiResponse<>("Task created successfully",responseDTO);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks(){
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>("All Tasks",tasks);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Integer id) {
        TaskResponseDTO taskById = taskService.getTaskById(id);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>("Task by Id",taskById);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Integer id,@Valid @RequestBody TaskRequestDTO requestDTO){
        return taskService.updateTask(id,requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Integer id){
        taskService.deleteTask(id);
        ApiResponse<String> response = new ApiResponse<>("Task deleted successfully",null);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
