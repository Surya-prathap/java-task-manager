package com.project.task_manager.controller;

import com.project.task_manager.dto.loginDtos.LoginRequestDTO;
import com.project.task_manager.dto.loginDtos.LoginResponseDTO;
import com.project.task_manager.dto.registerDtos.RegisterRequestDTO;
import com.project.task_manager.dto.registerDtos.RegisterResponseDTO;
import com.project.task_manager.response.ApiResponse;
import com.project.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> registerUser(@Valid @RequestBody RegisterRequestDTO dto){
        RegisterResponseDTO user = userService.registerUser(dto);
        ApiResponse<RegisterResponseDTO> response = new ApiResponse<>("user registered successfully",user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginUser(@Valid @RequestBody LoginRequestDTO dto){
        LoginResponseDTO user = userService.loginUser(dto);
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>("Login successful",user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
