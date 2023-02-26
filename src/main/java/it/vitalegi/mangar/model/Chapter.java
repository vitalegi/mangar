package it.vitalegi.mangar.model;

import it.vitalegi.mangar.util.StringUtil;
import lombok.Data;

import java.util.Comparator;

@Data
public class Chapter {

    String id;
    String name;
    String url;
    Volume volume;

    public static Comparator<Chapter> comparator() {
        return Comparator.comparing(v -> {
            String id = v.getId();
            return StringUtil.leftPadding(id, 5, '0');
        });
    }
}
