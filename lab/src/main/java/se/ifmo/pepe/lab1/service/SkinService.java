package se.ifmo.pepe.lab1.service;

import org.springframework.scheduling.annotation.Scheduled;
import se.ifmo.pepe.lab1.model.Skin;

import java.util.ArrayList;

public interface SkinService {
    ArrayList<Skin> fetchAllSkins();

    void removeSkinById(Long id);

    ArrayList<Skin> fetchAllApprovedSkins(Boolean approved);

    ArrayList<Skin> fetchAllSkinsByTag(String tag);

    ArrayList<Skin> fetchAllSkinsBySex(String sex);

    Skin fetchSkinById(Long id);

    ArrayList<Skin> fetchAllApprovedSkins();

    ArrayList<Skin> fetchAllDeclinedSkins();

    Skin saveSkin(Skin skin);

    String generateDownloadLink(String skinTitle);

    /**
     * Job is scheduled
     * At 12:00:00pm, on every Saturday, every month
     */
    @Scheduled(cron = "0 0 12 ? * SAT")
    void cleanUpDatabaseFromDeclinedSkins();
}
