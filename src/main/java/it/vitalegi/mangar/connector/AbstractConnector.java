package it.vitalegi.mangar.connector;

import it.vitalegi.mangar.config.Mangar;

public abstract class AbstractConnector {
    Mangar config;

    public AbstractConnector(Mangar config) {
        this.config = config;
    }

    public abstract void execute();
}
