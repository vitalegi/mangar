package it.vitalegi.mangar.model;

import it.vitalegi.mangar.util.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class Volume {
    String id;
    List<Chapter> chapters;

    public Volume() {
        chapters = new ArrayList<>();
    }

    public static Comparator<Volume> comparator() {
        return Comparator.comparing(v -> {
            String id = v.getId();
            return StringUtil.leftPadding(id, 5, '0');
        });
    }
}
