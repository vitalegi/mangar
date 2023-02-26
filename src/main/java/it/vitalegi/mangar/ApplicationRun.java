package it.vitalegi.mangar;

import it.vitalegi.mangar.config.Mangar;
import it.vitalegi.mangar.config.Mangars;
import it.vitalegi.mangar.connector.AbstractConnector;
import it.vitalegi.mangar.connector.ComikoConnector;
import it.vitalegi.mangar.service.StatusTrackerService;
import it.vitalegi.mangar.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ApplicationRun implements CommandLineRunner {

    @Autowired
    Mangars mangars;
    @Autowired
    StorageService storageService;
    @Autowired
    StatusTrackerService statusTrackerService;

    @Override
    public void run(String... args) throws Exception {
        mangars.getMangars().stream().filter(Mangar::isEnabled).forEach(this::process);
    }

    AbstractConnector getProcessor(Mangar mangar) {
        switch (mangar.getType()) {
            case COMIKO:
                return new ComikoConnector(mangar, storageService, statusTrackerService);
        }
        throw new RuntimeException("Connector " + mangar.getName() + " with type " + mangar.getType() + " is " +
                "unknown");
    }

    void process(Mangar mangar) {
        log.info("Start processing of {}", mangar.getName());
        AbstractConnector processor = getProcessor(mangar);
        processor.execute();
    }
}
