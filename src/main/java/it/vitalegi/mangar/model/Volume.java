package it.vitalegi.mangar.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Volume {
    String id;
    List<Chapter> chapters;

    public Volume() {
        chapters = new ArrayList<>();
    }
}
