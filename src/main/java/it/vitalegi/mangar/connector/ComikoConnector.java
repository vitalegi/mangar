package it.vitalegi.mangar.connector;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.model.Chapter;
import it.vitalegi.mangar.model.Series;
import it.vitalegi.mangar.model.Volume;
import it.vitalegi.mangar.service.StatusTrackerService;
import it.vitalegi.mangar.service.StorageService;
import it.vitalegi.mangar.util.ImageUtil;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Log4j2
public class ComikoConnector extends SeleniumConnector {
    static final Pattern VOLUME_AND_CHAPTER = Pattern.compile("^Volume (.*) Chapter (.*)$");
    static final Pattern CHAPTER = Pattern.compile("^Chapter (.*)$");

    Series series;
    StorageService storageService;

    StatusTrackerService statusTrackerService;

    public ComikoConnector(Mangar config, StorageService storageService, StatusTrackerService statusTrackerService) {
        super(config);
        series = new Series();
        this.storageService = storageService;
        this.statusTrackerService = statusTrackerService;
    }

    @Override
    protected void doExecute() {
        get(config.getUri());
        WebElement chapters = getByClassUnique("main");
        registerVolumesAndChapters(chapters);
        storageService.createDirs(config, series);

        series.getVolumes().stream().sorted(Volume.comparator()).forEach(this::downloadImages);
    }

    protected void downloadImage(Volume volume, Chapter chapter, int index, WebElement imageTag) {
        String imageUrl = imageTag.getAttribute("src");
        Path path = storageService.getImagePath(config, volume, chapter, index, "png");
        log.info("Download in path {} image {}", path, imageUrl);
        try {
            var image = ImageUtil.downloadImage(imageUrl);
            ImageUtil.saveImage(image, "png", path);
            log.info("Download completed.");
            statusTrackerService.downloadOk(volume.getId(), chapter.getId(), index, imageUrl);
        } catch (RuntimeException e) {
            statusTrackerService.downloadKo(volume.getId(), chapter.getId(), index, imageUrl);
        }
    }

    protected void downloadImages(Volume volume) {
        log.debug("Download images of volume {}", volume.getId());
        volume.getChapters().stream().sorted(Chapter.comparator()).forEach(c -> downloadImages(volume, c));
        statusTrackerService.volumeDone(volume.getId());
    }

    protected void downloadImages(Volume volume, Chapter chapter) {
        log.debug("Download images of chapter {}, vol {}, url={}", chapter.getId(), volume.getId(), chapter.getUrl());
        get(chapter.getUrl());
        var viewer = findUnique(By.id("viewer"));
        var images = findAll(viewer, By.tagName("img"));
        log.info("Chapter {} contains {} images  ", chapter.getId(), images.size());
        IntStream.range(0, images.size()).parallel().forEach(i -> downloadImage(volume, chapter, i, images.get(i)));
        statusTrackerService.chapterDone(volume.getId());
        statusTrackerService.status();
    }

    protected String getChapterId(WebElement chapter) {
        return getChapterId(findUnique(chapter, By.tagName("b")).getText());
    }

    protected String getChapterId(String label) {
        Matcher matcher = VOLUME_AND_CHAPTER.matcher(label);
        if (matcher.find()) {
            return applyOverrideRulesToChapterId(matcher.group(2));
        }
        matcher = CHAPTER.matcher(label);
        if (matcher.find()) {
            return applyOverrideRulesToChapterId(matcher.group(1));
        }
        return applyOverrideRulesToChapterId(null);
    }

    protected String getChapterName(WebElement chapter) {
        var names = findAll(chapter, By.tagName("span"));
        if (names.isEmpty()) {
            return getChapterName((String) null);
        }
        if (names.size() > 1) {
            throw new IllegalArgumentException("Found " + names.size() + " elements. Expected 1.");
        }
        return getChapterName(names.get(0).getText());
    }

    protected String getChapterName(String label) {
        if (label == null) {
            return null;
        }
        label = label.trim();
        if (label.startsWith(": ")) {
            label = label.substring(1).trim();
        }
        return label;
    }

    protected String getVolumeId(WebElement chapter) {
        return getVolumeId(findUnique(chapter, By.tagName("b")).getText());
    }

    protected String getVolumeId(String label) {
        log.debug("Process {}", label);
        Matcher matcher = VOLUME_AND_CHAPTER.matcher(label);
        if (matcher.find()) {
            log.debug("found {}", matcher.group(1));
            return applyOverrideRulesToVolumeId(matcher.group(1));
        }
        log.debug("Not found");
        return applyOverrideRulesToVolumeId(null);
    }

    protected void registerChapter(WebElement entry) {
        String url = entry.getAttribute("href");
        if (!acceptChapter(config.getChapters().getIgnore(), url)) {
            return;
        }
        String volumeId = getVolumeId(entry);
        String chapterId = getChapterId(entry);
        String chapterName = getChapterName(entry);
        series.addVolume(volumeId);
        series.addChapter(chapterId, chapterName, url, series.getVolume(volumeId));
        log.info("Registered chapter {}", chapterId);
    }

    protected void registerVolumesAndChapters(WebElement element) {
        var elements = findAll(element, By.className("chapt"));
        log.info("elements {}", elements.size());
        for (int i = 0; i < elements.size(); i++) {
            try {
                registerChapter(elements.get(i));
            } catch (Exception e) {
                throw new RuntimeException("Error processing element " + i);
            }
        }
    }
}
