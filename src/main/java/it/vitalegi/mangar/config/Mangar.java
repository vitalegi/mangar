package it.vitalegi.mangar.config;

import lombok.Data;

import java.net.URI;

@Data
public class Mangar {
    MangarType type;
    String name;

    boolean enabled;

    String uri;

    String outDir;

    Padding leftPaddingVolumes;
    Padding leftPaddingChapters;
    Padding leftPaddingPages;
}
