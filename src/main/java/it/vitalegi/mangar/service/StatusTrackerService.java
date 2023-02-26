package it.vitalegi.mangar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class StatusTrackerService {

    List<String> messagesKo = new ArrayList<>();
    List<String> messagesOk = new ArrayList<>();

    int pagesOk;
    int pagesKo;
    int chapters;
    int volumes;

    public void chapterDone(String chapter) {
        messagesOk.add("Chapter " + chapter + " done");
        chapters++;
    }

    public void downloadKo(String volume, String chapter, int page, String url) {
        messagesKo.add("Failed to download image: volume " + volume + ", chapter " + chapter + ", page " + page + ", "
                + "url " + url);
        pagesKo++;
    }

    public void downloadOk(String volume, String chapter, int page, String url) {
        messagesOk.add("volume " + volume + ", chapter " + chapter + ", page " + page + ", url " + url);
        pagesOk++;
    }

    public void status() {
        log.error("ERRORS:");
        messagesKo.forEach(m -> log.error(m));
        log.info("STATUS");
        log.info("Processed volumes:  {}", volumes);
        log.info("Processed chapters: {}", chapters);
        log.info("Processed pages: OK {}, KO {}", pagesOk, pagesKo);
    }

    public void volumeDone(String volume) {
        messagesOk.add("Volume " + volume + " done");
        volumes++;
    }
}
