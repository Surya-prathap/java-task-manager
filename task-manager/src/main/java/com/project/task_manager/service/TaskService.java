package com.project.task_manager.service;

import com.project.task_manager.dto.taskDtos.TaskRequestDTO;
import com.project.task_manager.dto.taskDtos.TaskResponseDTO;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.User;
import com.project.task_manager.exceptions.TaskNotFoundException;
import com.project.task_manager.exceptions.UnauthorizedException;
import com.project.task_manager.exceptions.UserNotFoundException;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskResponseDTO addTask(TaskRequestDTO dto)
    {
        String email = getCurrentUserEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.isStatus());
        task.setDueDate(dto.getDueDate());

        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(savedTask.getId());
        responseDTO.setTitle(savedTask.getTitle());
        responseDTO.setDescription(savedTask.getDescription());
        responseDTO.setStatus(savedTask.isStatus());
        responseDTO.setDueDate(savedTask.getDueDate());

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
            responseDTO.setDueDate(task.getDueDate());
            responseDTOList.add(responseDTO);
        }
        return responseDTOList;
    }

    public TaskResponseDTO getTaskById(Integer id){
        String email = getCurrentUserEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You are not authorized to get this task");
        }
        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(task.getId());
        responseDTO.setTitle(task.getTitle());
        responseDTO.setDescription(task.getDescription());
        responseDTO.setStatus(task.isStatus());
        responseDTO.setDueDate(task.getDueDate());

        return responseDTO;
    }

    public TaskResponseDTO updateTask(Integer id,TaskRequestDTO dto){
        String email = getCurrentUserEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if(!existingTask.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You are not authorized to update this task");
        }
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.isStatus());
        existingTask.setDueDate(dto.getDueDate());

        Task updatedTask = taskRepository.save(existingTask);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setTitle(updatedTask.getTitle());
        responseDTO.setDescription(updatedTask.getDescription());
        responseDTO.setStatus(updatedTask.isStatus());
        responseDTO.setDueDate(updatedTask.getDueDate());
        return responseDTO;
    }

    public void deleteTask(Integer id){
        String email = getCurrentUserEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You are not authorized to delete this task");
        }
        taskRepository.delete(task);
    }

    private String getCurrentUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }
        return user.getEmail();
    }

    public List<TaskResponseDTO> getMyTasks(){
        String email = getCurrentUserEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Task> tasks = taskRepository.findByUser(user);

        List<TaskResponseDTO> taskResponseDTOList = new ArrayList<>();

        for(Task task : tasks){
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.isStatus());
            dto.setDueDate(task.getDueDate());
            taskResponseDTOList.add(dto);
        }
        return taskResponseDTOList;
    }
}
