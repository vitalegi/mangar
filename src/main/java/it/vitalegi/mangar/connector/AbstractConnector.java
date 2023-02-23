package it.vitalegi.mangar.connector;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.config.OverrideRule;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public abstract class AbstractConnector {
    Mangar config;

    public AbstractConnector(Mangar config) {
        this.config = config;
    }

    public abstract void execute();

    protected String applyOverrideRulesToChapterId(String id) {
        return applyRules(config.getChapters().getOverrideIds(), id);
    }

    protected String applyOverrideRulesToVolumeId(String id) {
        return applyRules(config.getVolumes().getOverrideIds(), id);
    }

    protected String applyRule(OverrideRule rule, String text) {
        if (rule.getType().equals("IS_NULL")) {
            if (text == null || text.trim().length() == 0) {
                log.info("Override null value with {}", rule.getValue());
                return rule.getValue();
            }
        }
        if (rule.getType().equals("EQUALS")) {
            if (rule.getParam().equals(text)) {
                log.info("Override {} with {}", text, rule.getValue());
                return rule.getValue();
            }
        }
        return text;
    }

    protected String applyRules(List<OverrideRule> rules, String text) {
        for (OverrideRule rule : rules) {
            text = applyRule(rule, text);
        }
        return text;
    }
}
