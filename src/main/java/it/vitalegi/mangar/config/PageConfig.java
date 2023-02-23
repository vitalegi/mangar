package it.vitalegi.mangar.config;

import lombok.Data;

import java.util.List;

@Data
public class PageConfig {
    Padding leftPadding;
    List<OverrideRule> overrideIds;
}
