package com.fonctionpublique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.time.LocalDate;

@CrossOrigin
@SpringBootApplication
public class AttestationApplication extends SpringBootServletInitializer {


    public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
        SpringApplication.run(AttestationApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(AttestationApplication.class);

    }





}
