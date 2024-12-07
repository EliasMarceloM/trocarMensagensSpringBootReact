package com.example.trocarMensagens.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.trocarMensagens.entity.Message;
import com.example.trocarMensagens.entity.MessageDTO;
import com.example.trocarMensagens.entity.User;
import com.example.trocarMensagens.entity.UserDTO;
import com.example.trocarMensagens.entity.UserRole;
import com.example.trocarMensagens.repository.UserRepository;
import com.example.trocarMensagens.security.TokenService;
import com.example.trocarMensagens.service.AuthorizationService;
import com.example.trocarMensagens.service.MessageService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // Permite apenas chamadas do front-end local
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private AuthorizationService authorizationService; 
    
    
    // Endpoint GET que retorna uma mensagem simples
    @GetMapping("/mensagem")
    public String getMensagem() {
        return "{\"message\": \"Spring Boot resposta\"}";
    }

    // Endpoint para criar um novo usuário e retornar token
    @PostMapping("/registrar")
    public String registerUser(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        // Verifica se o usuário já existe
        if (userRepository.findByNome(userDTO.nome()) != null) {
            throw new RuntimeException("Usuário já existe.");
        }

        // Cria o objeto User a partir do UserDTO
        User user = new User();
        user.setNome(userDTO.nome());
        user.setPassword(passwordEncoder.encode(userDTO.senha())); // Codificando a senha
        user.setRole(UserRole.USER); // Atribui a role USER

        // Salva o usuário no banco de dados
        userRepository.save(user);

        // Gera o token de autenticação
        authorizationService.generateTokenAndSetCookies(user, response);

        // Retorna uma resposta simples com sucesso
        return "{\"message\": \"Usuário registrado com sucesso!\", \"user\": {\"id\": \"" + user.getId() + "\", \"nome\": \"" + user.getNome() + "\", \"role\": \"" + user.getRole().getRole() + "\"}}";
    }





    // Endpoint para autenticar um usuário (login)
    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        // Verifica se o usuário existe no banco de dados
        User user = (User) userRepository.findByNome(userDTO.nome());
        
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        // Verifica se a senha fornecida corresponde à senha codificada no banco
        if (!passwordEncoder.matches(userDTO.senha(), user.getPassword())) {
            throw new RuntimeException("Senha inválida.");
        }

        // Gera o token de autenticação
        authorizationService.generateTokenAndSetCookies(user, response);

        // Retorna a resposta com a mensagem de sucesso e o usuário autenticado
        return "{\"message\": \"Login bem-sucedido!\", \"user\": {\"id\": \"" + user.getId() + "\", \"nome\": \"" + user.getNome() + "\", \"role\": \"" + user.getRole().getRole() + "\"}}";
    }
    
    
    
    
    
}
