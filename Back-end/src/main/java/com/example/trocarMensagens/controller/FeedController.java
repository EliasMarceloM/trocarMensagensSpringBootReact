package com.example.trocarMensagens.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.trocarMensagens.entity.Message;
import com.example.trocarMensagens.entity.MessageDTO;
import com.example.trocarMensagens.entity.User;
import com.example.trocarMensagens.entity.oldMessagesDTO;
import com.example.trocarMensagens.repository.UserRepository;
import com.example.trocarMensagens.service.AuthorizationService;
import com.example.trocarMensagens.service.MessageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // Permite apenas chamadas do front-end local
public class FeedController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthorizationService authorizationService; 
    
    // Endpoint GET para buscar as mensagens
    @GetMapping("/feed")
    public ResponseEntity<Object> getFeedMessages(HttpServletRequest request) {
        String tokenNome = authorizationService.readCookie("tokenNome", request);
        if (tokenNome != null) {
        	List<Message> allMessages = messageService.getLatestMessages();  // Pega as últimas mensagens
        	   
        		for (int i = 0; i < allMessages .size(); i++) {
        		      System.out.println("conteudo"+ allMessages.get(i).getContent() + allMessages.get(i).getAuthor().getNome() + allMessages.get(i).getTimestamp());
        		    }
        	
        	try {
        	        List<Message> messages = messageService.getLatestMessages(); // Exemplo de consulta
        	        
        	    } catch (Exception e) {
        	        System.out.println("Erro ao obter mensagens: " + e.getMessage());
        	        
        	    }
        	
            return ResponseEntity.ok().body(new Object() {
                public final List<Message> messages = allMessages;
            });
        } else {
            System.out.println("/feed: Usuário não autenticado");
            throw new RuntimeException("Usuário não autenticado");
        }
    }


    // Endpoint POST para salvar uma nova mensagem
    @PostMapping("/feedSalve")
    public String postMessage(@RequestBody String content, HttpServletRequest request) {
        String tokenNome =authorizationService.readCookie("tokenNome", request);

        if (tokenNome != null && content != null && !content.trim().isEmpty()) {
            User user = (User) userRepository.findByNome(tokenNome);  // Busca o usuário pelo nome
            if (user != null) {
                messageService.postMessage(user, content);  // Salva a nova mensagem
                return "{\"message\": \"Mensagem salva com sucesso!\"}";
            } else {
                System.out.println("/feedSalve: Usuário não autenticado");
                throw new RuntimeException("Usuário não encontrado.");
            }
        } else {
            System.out.println("/feedSalve: Conteúdo da mensagem vazio ou usuário não autenticado");
            throw new RuntimeException("Conteúdo da mensagem vazio ou usuário não autenticado.");
        }
    }
    
}
