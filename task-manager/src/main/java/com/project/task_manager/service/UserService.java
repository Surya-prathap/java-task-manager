package com.project.task_manager.service;

import com.project.task_manager.dto.loginDtos.LoginRequestDTO;
import com.project.task_manager.dto.loginDtos.LoginResponseDTO;
import com.project.task_manager.dto.registerDtos.RegisterRequestDTO;
import com.project.task_manager.dto.registerDtos.RegisterResponseDTO;
import com.project.task_manager.entity.User;
import com.project.task_manager.exceptions.EmailAlreadyExistsException;
import com.project.task_manager.exceptions.InvalidCredentialsException;
import com.project.task_manager.jwt.JwtService;
import com.project.task_manager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

@Autowired
private UserRepository userRepository;

@Autowired
private BCryptPasswordEncoder passwordEncoder;

@Autowired
private JwtService jwtService;

public RegisterResponseDTO registerUser(RegisterRequestDTO dto){
   Optional<User> existingUser = userRepository.findUserByEmail(dto.getEmail());
   if (existingUser.isPresent()){
       throw new EmailAlreadyExistsException("Email already exists");
   }
   User user = new User();
   user.setUserName(dto.getUserName());
   user.setEmail(dto.getEmail());
   user.setPassword(passwordEncoder.encode(dto.getPassword()));
   user.setRole(dto.getRole());

   User savedUser = userRepository.save(user);

   RegisterResponseDTO responseDTO = new RegisterResponseDTO();

   responseDTO.setId(savedUser.getId());
   responseDTO.setUserName(savedUser.getUserName());
   responseDTO.setEmail(savedUser.getEmail());
   responseDTO.setRole(savedUser.getRole());

    return responseDTO;
}

public LoginResponseDTO loginUser(LoginRequestDTO dto){

   Optional<User> user = userRepository.findUserByEmail(dto.getEmail());
   if (user.isEmpty()){
      throw new InvalidCredentialsException("Invalid credentials");
   }

   boolean isPasswordMatched = passwordEncoder.matches(dto.getPassword(), user.get().getPassword());

   if (!isPasswordMatched) {
      throw new InvalidCredentialsException("Invalid credentials");
   }

   String token = jwtService.generateToken(user.get().getEmail());

   LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

   loginResponseDTO.setId(user.get().getId());
   loginResponseDTO.setUserName(user.get().getUserName());
   loginResponseDTO.setEmail(user.get().getEmail());
   loginResponseDTO.setRole(user.get().getRole());
   loginResponseDTO.setToken(token);

   return loginResponseDTO;
}
}
