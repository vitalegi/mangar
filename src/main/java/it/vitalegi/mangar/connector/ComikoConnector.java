package it.vitalegi.mangar.connector;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.model.Chapter;
import it.vitalegi.mangar.model.Series;
import it.vitalegi.mangar.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ComikoConnector extends SeleniumConnector {
    static final String DEFAULT_VOLUME_ID = "99";
    static final Pattern VOLUME_AND_CHAPTER = Pattern.compile("^Volume (.*) Chapter (.*)$");
    static final Pattern CHAPTER = Pattern.compile("^Chapter (.*)$");

    Series series;
    StorageService storageService;

    public ComikoConnector(Mangar config, StorageService storageService) {
        super(config);
        series = new Series();
        this.storageService = storageService;
    }

    @Override
    protected void doExecute() {
        get(config.getUri());
        WebElement chapters = getByClassUnique("main");
        registerChapters(chapters);
        series.getChapters()
              .forEach(c -> log.info("ID={}, NAME={}, VOLUME={}, URL={}", c.getId(), c.getName(), c.getVolume()
                                                                                                   .getId(),
                      c.getUrl()));

        storageService.createDirs(config, series);
        registerImages(series.getChapters().get(0));
    }

    protected String getChapterId(WebElement chapter) {
        return getChapterId(findUnique(chapter, By.tagName("b")).getText());
    }

    protected String getChapterId(String label) {
        Matcher matcher = VOLUME_AND_CHAPTER.matcher(label);
        if (matcher.find()) {
            return matcher.group(2);
        }
        matcher = CHAPTER.matcher(label);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "0";
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
            return matcher.group(1);
        }
        log.debug("Not found");
        return DEFAULT_VOLUME_ID;
    }

    protected void registerChapter(WebElement entry) {
        String url = entry.getAttribute("href");
        String volumeId = getVolumeId(entry);
        String chapterId = getChapterId(entry);
        String chapterName = getChapterName(entry);
        series.addVolume(volumeId);
        series.addChapter(chapterId, chapterName, url, series.getVolume(volumeId));
    }

    protected void registerChapters(WebElement element) {
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

    protected void registerImage(Chapter chapter, int index, WebElement imageTag) {
        String imageUrl = imageTag.getAttribute("src");
        log.info("Download {}", imageUrl);
    }

    protected void registerImages(Chapter chapter) {
        log.debug("Register images of chapter {} ({})", chapter.getId(), chapter.getUrl());
        get(chapter.getUrl());
        var viewer = findUnique(By.id("viewer"));
        var images = findAll(viewer, By.tagName("img"));
        log.info("Chapter {} contains {} images", chapter.getId(), images.size());
        for (int i = 0; i < images.size(); i++) {
            registerImage(chapter, i, images.get(i));
        }
    }
}
