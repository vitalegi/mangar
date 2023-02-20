package it.vitalegi.mangar.model;

import lombok.Data;

@Data
public class Chapter {

    String id;
    String name;
    String url;
    Volume volume;
}
