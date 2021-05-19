package se.ifmo.pepe.lab1.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.model.User;
import se.ifmo.pepe.lab1.repository.SkinRepository;
import se.ifmo.pepe.lab1.repository.UserRepository;
import se.ifmo.pepe.lab1.service.NotificationService;
import se.ifmo.pepe.lab1.service.SkinService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;


@Slf4j
@Service
public class SkinServiceImpl implements SkinService {
    private final SkinRepository skinRepository;
    private final UserRepository userRepository;

    @Autowired
    public SkinServiceImpl(SkinRepository skinRepository, UserRepository userRepository) {
        this.skinRepository = skinRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ArrayList<Skin> fetchAllSkins() {
        return new ArrayList<>(skinRepository.findAll());
    }

    @Override
    public void removeSkinById(Long id) {
        skinRepository.deleteById(id);
    }

    @Override
    public ArrayList<Skin> fetchAllApprovedSkins(Boolean approved) {
        return new ArrayList<>(skinRepository.findAllByApproved(approved));
    }

    @Override
    public ArrayList<Skin> fetchAllSkinsByTag(String tag) {
        return new ArrayList<>(skinRepository.findAllByTag(tag));
    }

    @Override
    public ArrayList<Skin> fetchAllSkinsBySex(String sex) {
        return new ArrayList<>(skinRepository.findAllBySex(sex));
    }

    @Override
    public Skin fetchSkinById(Long id) {
        return skinRepository.findById(id).orElse(null);
    }

    @Override
    public ArrayList<Skin> fetchAllApprovedSkins() {
        return (ArrayList<Skin>) skinRepository.findAllByApproved(true);
    }

    @Override
    public ArrayList<Skin> fetchAllDeclinedSkins() {
        return (ArrayList<Skin>) skinRepository.findAllByApproved(false);
    }

    @Override
    public Skin saveSkin(Skin skin) {
        skin.setDlUrl(generateDownloadLink(skin.getTitle()));
        User author = skin.getUser();
        author.setSkinCounter(author.getSkinCounter() + 1);
        userRepository.save(author);
        skinRepository.save(skin);
        return skin;
    }

    @Override
    public String generateDownloadLink(String skinTitle) {
        /*
         * !PLACEHOLDER!
         * STRING GENERATOR WITH LENGTH BASED ON SKIN'S TITLE LENGTH
         * */
        byte[] array = new byte[skinTitle.length()];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("Windows-1251"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUpDatabaseFromDeclinedSkins() {
        ArrayList<Skin> list = fetchAllDeclinedSkins();
        log.info("Scheduled job started. Cleaning up database from declined skins");
        list.forEach(s -> removeSkinById(s.getId()));
        log.info("Scheduled job finished successfully");
    }

}
