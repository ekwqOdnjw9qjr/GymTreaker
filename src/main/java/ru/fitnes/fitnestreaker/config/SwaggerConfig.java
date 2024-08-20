package ru.fitnes.fitnestreaker.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8086");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Roman Mironov");
        myContact.setEmail("psychokid534@gmail.com");

        Info information = new Info()
                .title("Gym REST API")
                .version("1.0.0")
                .description("The application is designed to make an appointment with a trainer, get a gym membership.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}

