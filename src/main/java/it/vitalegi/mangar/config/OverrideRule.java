package it.vitalegi.mangar.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverrideRule {
    String type;
    String param;
    String value;
}
