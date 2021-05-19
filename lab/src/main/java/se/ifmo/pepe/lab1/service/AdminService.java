package se.ifmo.pepe.lab1.service;

import se.ifmo.pepe.lab1.exception.SkinException;

public interface AdminService {
    Boolean decline(Long skinId) throws SkinException;
    Boolean approve(Long skinId) throws SkinException;
}
