package se.ifmo.pepe.lab1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.lab1.model.Skin;
import se.ifmo.pepe.lab1.repository.SkinRepository;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SkinService {
    private final SkinRepository skinRepository;

    @Autowired
    public SkinService(SkinRepository skinRepository) {
        this.skinRepository = skinRepository;
    }

    public ArrayList<Skin> fetchAllSkins() {
        return new ArrayList<>(skinRepository.findAll());
    }

    public void removeSkinById(Long id) {
        skinRepository.deleteById(id);
    }

    public ArrayList<Skin> fetchAllApprovedSkins(Boolean approved) {
        return new ArrayList<>(skinRepository.findAllByApproved(approved));
    }

    public ArrayList<Skin> fetchAllSkinsByTag(String tag) {
        return new ArrayList<>(skinRepository.findAllByTag(tag));
    }

    public ArrayList<Skin> fetchAllSkinsBySex(String sex) {
        return new ArrayList<>(skinRepository.findAllBySex(sex));
    }

    public Skin fetchSkinById(Long id) {
        return skinRepository.findById(id).orElse(null);
    }

    private ArrayList<Skin> fetchAllApprovedSkins() {
        return (ArrayList<Skin>) skinRepository.findAllByApproved(true);
    }

    private ArrayList<Skin> fetchAllDeclinedSkins() {
        return (ArrayList<Skin>) skinRepository.findAllByApproved(false);
    }

    public Skin saveSkin(Skin skin) {
        skin.setDlUrl(generateDownloadLink(skin.getTitle()));
        skinRepository.save(skin);
        return skin;
    }

    private String generateDownloadLink(String skinTitle) {
        /*
         * !PLACEHOLDER!
         * STRING GENERATOR WITH LENGTH BASED ON SKIN'S TITLE LENGTH
         * */
        byte[] array = new byte[skinTitle.length()];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("Windows-1251"));
    }

    /**
     * Job is scheduled
     * At 12:00:00pm, on every Saturday, every month
     */
    @Scheduled(cron = "0 0 12 ? * SAT")
    private void cleanUpDatabaseFromDeclinedSkins() {
        ArrayList<Skin> list = fetchAllDeclinedSkins();
        log.info("Scheduled job started. Cleaning up database from declined skins");
        list.forEach(s -> removeSkinById(s.getId()));
        log.info("Scheduled job finished successfully");
    }

}
