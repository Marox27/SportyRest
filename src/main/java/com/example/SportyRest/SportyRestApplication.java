package com.example.SportyRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Cloudinary packages
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class SportyRestApplication {
	// Se inicia la Api
	public static void main(String[] args) {
		SpringApplication.run(SportyRestApplication.class, args);

		Dotenv dotenv = Dotenv.load();
		Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
		System.out.println("SPORTYREST LISTA Y EN FUNCIONAMIENTO");
    }

}
