package demo.person.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite toate rutele
                .allowedOrigins(
                        "http://frontend.localhost",
                        "http://user.localhost",
                        "http://device.localhost",
                        "http://monitoring.localhost",
                        "http://chat.localhost",
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://localhost:8082",
                        "http://localhost:8083",
                        "https://heroic-boba-6ce237.netlify.app"
                ) // Permite accesul de la mai multe origini
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite metode HTTP
                .allowedHeaders("*") // Permite toate headerele
                .allowCredentials(true); // Permite cookie-uri/sesiuni partajate
    }
}
