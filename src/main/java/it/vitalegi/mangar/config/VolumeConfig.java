package it.vitalegi.mangar.config;

import lombok.Data;

import java.util.List;

@Data
public class VolumeConfig {
    Padding leftPadding;
    List<OverrideRule> overrideIds;
}
