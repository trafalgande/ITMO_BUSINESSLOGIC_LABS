package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.repository.SkinRepository;

@Service
public class AdminService {

    private final SkinRepository skinRepository;

    @Autowired
    public AdminService(SkinRepository skinRepository) {
        this.skinRepository = skinRepository;
    }


}
