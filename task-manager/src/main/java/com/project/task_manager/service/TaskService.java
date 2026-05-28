package com.project.task_manager.service;

import com.project.task_manager.dto.taskDtos.TaskRequestDTO;
import com.project.task_manager.dto.taskDtos.TaskResponseDTO;
import com.project.task_manager.entity.Task;
import com.project.task_manager.exceptions.TaskNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskResponseDTO addTask(TaskRequestDTO dto)
    {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.isStatus());
        task.setDueDate(dto.getDueDate());

        Task savedTask = taskRepository.save(task);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(savedTask.getId());
        responseDTO.setTitle(savedTask.getTitle());
        responseDTO.setDescription(savedTask.getDescription());
        responseDTO.setStatus(savedTask.isStatus());

        return responseDTO;
    }

    public List<TaskResponseDTO> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponseDTO> responseDTOList = new ArrayList<>();
        for (Task task : tasks){
            TaskResponseDTO responseDTO = new TaskResponseDTO();
            responseDTO.setId(task.getId());
            responseDTO.setTitle(task.getTitle());
            responseDTO.setDescription(task.getDescription());
            responseDTO.setStatus(task.isStatus());
            responseDTOList.add(responseDTO);
        }
        return responseDTOList;
    }

    public TaskResponseDTO getTaskById(Integer id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(task.getId());
        responseDTO.setTitle(task.getTitle());
        responseDTO.setDescription(task.getDescription());
        responseDTO.setStatus(task.isStatus());

        return responseDTO;
    }

    public TaskResponseDTO updateTask(Integer id,TaskRequestDTO dto){
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.isStatus());
        Task updatedTask = taskRepository.save(existingTask);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setTitle(updatedTask.getTitle());
        responseDTO.setDescription(updatedTask.getDescription());
        responseDTO.setStatus(updatedTask.isStatus());
        return responseDTO;
    }

    public void deleteTask(Integer id){
        taskRepository.deleteById(id);
    }
}
