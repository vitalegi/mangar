package it.vitalegi.mangar.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Series {
    List<Volume> volumes;
    List<Chapter> chapters;

    public Series() {
        volumes = new ArrayList<>();
        chapters = new ArrayList<>();
    }

    public void addChapter(String id, String name, String url, Volume volume) {
        if (hasChapter(id)) {
            return;
        }
        addVolume(volume);
        Chapter chapter = createChapter(id, name, url);
        chapters.add(chapter);
        connect(chapter, volume);
    }

    public void addVolume(Volume volume) {
        if (hasVolume(volume.getId())) {
            return;
        }
        volumes.add(volume);
    }

    public void addVolume(String id) {
        if (hasVolume(id)) {
            return;
        }
        Volume volume = new Volume();
        volume.setId(id);
        volumes.add(volume);
    }

    public void connect(Chapter chapter, Volume volume) {
        chapter.setVolume(volume);
        volume.getChapters().add(chapter);
    }

    public Volume getVolume(String id) {
        return volumes.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean hasChapter(String id) {
        return chapters.stream().anyMatch(c -> c.getId().equals(id));
    }

    public boolean hasVolume(String id) {
        return volumes.stream().anyMatch(c -> c.getId().equals(id));
    }

    protected Chapter createChapter(String id, String name, String url) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setName(name);
        chapter.setUrl(url);
        return chapter;
    }
}
