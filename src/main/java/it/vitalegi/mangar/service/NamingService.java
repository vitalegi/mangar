package it.vitalegi.mangar.service;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.config.Padding;
import it.vitalegi.mangar.model.Chapter;
import it.vitalegi.mangar.model.Volume;
import it.vitalegi.mangar.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NamingService {

    public String getChapterName(Mangar config, Chapter chapter) {
        String chapterId = getChapterId(config, chapter);
        String title = chapter.getName();
        if (title != null && title.trim().length() > 0) {
            title = " - " + sanitize(title);
        } else {
            title = "";
        }
        return config.getName() + " c" + chapterId + title;
    }

    public String getPageName(Mangar config, Chapter chapter, int page, String extension) {
        String chapterId = getChapterId(config, chapter);
        String paddedPage = leftPadding("" + page, config.getPages().getLeftPadding());
        return config.getName() + " c" + chapterId + "_" + paddedPage + "." + extension;
    }

    public String getVolumeName(Mangar config, Volume volume) {
        String sanitizedId = sanitize(volume.getId());
        return config.getName() + " v" + leftPadding(sanitizedId, config.getVolumes().getLeftPadding());
    }

    protected String getChapterId(Mangar config, Chapter chapter) {
        String sanitizedId = sanitize(chapter.getId());
        return leftPadding(sanitizedId, config.getChapters().getLeftPadding());
    }

    protected String leftPadding(String str, Padding padding) {
        return StringUtil.leftPadding(str, padding.getLength(), padding.getCharacter());
    }

    protected String sanitize(String str) {
        return str.replaceAll("\\.", "").replaceAll("\\t", "").replaceAll("\\r", "").replaceAll("\\n", "")
                  .replaceAll("<", "").replaceAll(">", "").replaceAll(":", "").replaceAll("\"", "").replaceAll("/", "")
                  .replaceAll("\\\\", "").replaceAll("\\|", "").replaceAll("\\?", "").replaceAll("\\*", "");
    }
}
