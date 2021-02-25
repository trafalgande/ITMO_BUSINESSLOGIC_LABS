package se.ifmo.pepe.lab1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Author;
import se.ifmo.pepe.lab1.repository.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author fetchAuthorById(Long id) {
        return authorRepository.findById(id).isPresent() ? authorRepository.findById(id).get() : null;
    }
}
