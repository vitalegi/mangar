package it.vitalegi.mangar.service;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.model.Series;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
public class StorageService {

    public void createDirs(Mangar config, Series series) {
        log.info("Create dir for {} in folder {}", config.getName(), config.getOutDir());
        Path parent;
        try {
            parent = Files.createDirectories(Paths.get(config.getOutDir()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
