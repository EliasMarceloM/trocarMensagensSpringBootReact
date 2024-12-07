package com.example.trocarMensagens.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.trocarMensagens.entity.User;
import com.example.trocarMensagens.repository.UserRepository;
import com.example.trocarMensagens.security.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class AuthorizationService implements UserDetailsService {
	 @Autowired
	 UserRepository repository;
	 
	 @Autowired
	    private TokenService tokenService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("AuthorizationService ");
		
		 return repository.findByNome(username);
	}
	
	 // Função que envia o cookie com o token
    public void generateTokenAndSetCookies(User user, HttpServletResponse response) {
        try {
            String token = tokenService.generateToken(user);
            String username = user.getUsername();
            enviarCookie(response, "tokenAuth", token);  // Salva o token
            enviarCookie(response, "tokenNome", username);  // Salva o nome do usuário
        } catch (Exception e) {
            System.out.println("Erro ao gerar o token: " + e.getMessage());
        }
    }
    // Função auxiliar para enviar o cookie
    public void enviarCookie(HttpServletResponse response, String nomeCookie, String valorCookie) {
    	System.out.println("Enviando cookie: " + nomeCookie +" " + valorCookie); 
    	Cookie cookie = new Cookie(nomeCookie, valorCookie);
        cookie.setMaxAge(60 * 60);  // 1 hora
        cookie.setPath("/");  // O cookie será acessível para toda a aplicação
        response.addCookie(cookie);
    }
    
    // Função que lê um cookie
    public String readCookie(String key, HttpServletRequest request) {
        try {
            return Arrays.stream(request.getCookies())
                    .filter(c -> key.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findAny()
                    .orElse(null);  // Retorna null se o cookie não for encontrado
        } catch (Exception e) {
            System.out.println("Erro ao ler cookie: " + e.getMessage());
            return null;
        }
    }
    
	
	

}
