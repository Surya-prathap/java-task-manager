package com.project.task_manager.controller;

import com.project.task_manager.entity.Task;
import com.project.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task){
       return taskService.addTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Integer id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Integer id, @RequestBody Task updatedTask){
        return taskService.updateTask(id,updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id){
        taskService.deleteTask(id);
    }

}
