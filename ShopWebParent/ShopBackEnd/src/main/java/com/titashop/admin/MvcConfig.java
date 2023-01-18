package com.titashop.admin;

import jakarta.persistence.Column;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);
        //System.out.println(" ----userPhotosDir " + userPhotosDir);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        //  System.out.println("----userPhotosPath : "+ userPhotosPath);

        registry.addResourceHandler("/" + dirName + "/**")
                //for user window : file:/....
                .addResourceLocations("file:/" + userPhotosPath + "/");


        String categoryImagesDirName = "../category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);

        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/category-images/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");
    }

}
