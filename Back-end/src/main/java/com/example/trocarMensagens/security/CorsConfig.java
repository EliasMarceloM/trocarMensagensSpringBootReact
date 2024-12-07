package com.example.trocarMensagens.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // Configura o CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permite requisições de "http://localhost:3000" (onde seu front-end React está rodando)
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // Permite CORS apenas do React (localhost:3000)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Permite os métodos GET, POST, PUT, DELETE e OPTIONS
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));  // Permite os cabeçalhos necessários
        config.setAllowCredentials(true);  // Permite que cookies e credenciais sejam enviados

        // Aplica a configuração a todos os endpoints ("/**")
        source.registerCorsConfiguration("/**", config);

        // Retorna o filtro CORS configurado
        return new CorsFilter(source);
    }
}
 
/*
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}*/ 