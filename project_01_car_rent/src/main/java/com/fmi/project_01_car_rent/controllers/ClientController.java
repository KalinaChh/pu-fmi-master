package com.fmi.project_01_car_rent.controllers;


import com.fmi.project_01_car_rent.entities.Client;
import com.fmi.project_01_car_rent.http.AppResponse;
import com.fmi.project_01_car_rent.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ClientController {


    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> createClient(@RequestBody Client client) {
        // Check if the phone number already exists
        if (clientRepository.existsByPhone(client.getPhone())) {
            return AppResponse.error()
                    .withMessage("A client with this phone number already exists.")
                    .withCode(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // Save the new client
        clientRepository.createClient(client);
        return AppResponse.success()
                .withMessage("Client created successfully.")
                .withCode(HttpStatus.CREATED)
                .build();
    }
}
