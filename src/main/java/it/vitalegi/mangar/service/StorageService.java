package it.vitalegi.mangar.service;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.model.Chapter;
import it.vitalegi.mangar.model.Series;
import it.vitalegi.mangar.model.Volume;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
public class StorageService {

    @Autowired
    NamingService namingService;

    public void createDirs(Mangar config, Series series) {
        log.info("Create dir for \"{}\" in folder \"{}\"", config.getName(), config.getOutDir());
        mkdirs(getRootPath(config));
        createVolumesTree(config, series);
    }

    public Path getChapterPath(Mangar config, Volume volume, Chapter chapter) {
        String name = namingService.getChapterName(config, chapter);
        return getVolumePath(config, volume).resolve(name);
    }

    public Path getImagePath(Mangar config, Volume volume, Chapter chapter, int page, String extension) {
        String name = namingService.getPageName(config, chapter, page, extension);
        return getChapterPath(config, volume, chapter).resolve(name);
    }

    public Path getRootPath(Mangar config) {
        return Paths.get(config.getOutDir());
    }

    public Path getVolumePath(Mangar config, Volume volume) {
        String name = namingService.getVolumeName(config, volume);
        return getRootPath(config).resolve(name);
    }

    protected Path createChapter(Mangar config, Volume volume, Chapter chapter) {
        Path path = getChapterPath(config, volume, chapter);
        log.info("Create chapter dir \"{}\"", path);
        return mkdirs(path);
    }

    protected Path createVolume(Mangar config, Volume volume) {
        Path path = getVolumePath(config, volume);
        log.info("Create volume dir \"{}\"", path);
        return mkdirs(path);
    }

    protected void createVolumeChapters(Path volumeFolder, Mangar config, Volume volume) {
        for (Chapter chapter : volume.getChapters()) {
            createChapter(config, volume, chapter);
        }
    }

    protected Path createVolumeTree(Mangar config, Volume volume) {
        Path volumePath = createVolume(config, volume);
        createVolumeChapters(volumePath, config, volume);
        return volumePath;
    }

    protected void createVolumesTree(Mangar config, Series series) {
        for (Volume volume : series.getVolumes()) {
            createVolumeTree(config, volume);
        }
    }

    protected Path mkdirs(Path path) {
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
