package it.vitalegi.mangar.config;

import lombok.Data;

@Data
public class Mangar {
    MangarType type;
    String name;

    boolean enabled;

    String uri;

    String outDir;
    VolumeConfig volumes;

    ChapterConfig chapters;

    PageConfig pages;
}
