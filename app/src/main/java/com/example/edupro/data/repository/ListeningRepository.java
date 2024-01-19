package com.example.edupro.data.repository;

import com.example.edupro.model.listening.ListeningDto;

public class ListeningRepository {
    private final FirebaseRepository<ListeningDto> firebaseRepository;

    private ListeningRepository() {
        firebaseRepository = FirebaseRepository.getInstance(ListeningDto.class,"listening");
    }

}
