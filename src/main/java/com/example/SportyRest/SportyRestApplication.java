package com.example.SportyRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Cloudinary packages
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class SportyRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportyRestApplication.class, args);

		Dotenv dotenv = Dotenv.load();
		Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
		System.out.println(cloudinary.config.cloudName);

		// Upload an image
		// UploadImage(cloudinary);

		// Get image details
		// GetImageDetails(cloudinary);


    }

	public static void UploadImage(Cloudinary cloudinary){
		try{
			Map params1 = ObjectUtils.asMap(
					"use_filename", true,
					"unique_filename", false,
					"overwrite", true
			);

			// https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg
			System.out.println(
					cloudinary.uploader().upload("https://i.pinimg.com/564x/b6/2b/f4/b62bf4d6aa7019de819f80f01667e466.jpg", params1));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void uploadImageByte(Cloudinary cloudinary, byte[] imageBytes) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
			Map<String, Object> params = ObjectUtils.asMap(
					"use_filename", true,
					"unique_filename", false,
					"overwrite", true
			);

			Map uploadResult = cloudinary.uploader().upload(inputStream, params);
			System.out.println(uploadResult);

			// Obtener la URL de la imagen subida
			String imageUrl = (String) uploadResult.get("url");
			System.out.println("URL de la imagen subida: " + imageUrl);
		} catch (IOException e) {
			throw new RuntimeException("Error al subir la imagen", e);
		}
	}

	public static void GetImageDetails(Cloudinary cloudinary){
		try{
			// Get the asset details
			Map params2 = ObjectUtils.asMap(
					"quality_analysis", true
			);

			System.out.println(
					cloudinary.api().resource("coffee_cup", params2));
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static void TransformImage(Cloudinary cloudinary){
		try {
			// Create the image tag with the transformed image and log it to the console
			System.out.println(
					cloudinary.url().transformation(new Transformation()
									.crop("pad")
									.width(300)
									.height(400)
									.background("auto:predominant"))
							.imageTag("coffee_cup"));

			// The code above generates an HTML image tag similar to the following:
			//  <img src='https://res.cloudinary.com/demo/image/upload/b_auto:predominant,c_pad,h_400,w_300/coffee_cup' height='400' width='300'/>
		} catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
