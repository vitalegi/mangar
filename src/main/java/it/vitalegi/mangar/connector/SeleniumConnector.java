package it.vitalegi.mangar.connector;

import it.vitalegi.mangar.config.Mangar;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

@Log4j2
public abstract class SeleniumConnector extends AbstractConnector {

    FirefoxDriver driver;


    public SeleniumConnector(Mangar config) {
        super(config);
    }

    @Override
    public void execute() {
        log.info("Start");
        try {
            init();
            doExecute();
        } finally {
            close();
        }
    }

    protected void close() {
        if (driver != null) {
            driver.close();
        }
    }

    protected abstract void doExecute();

    protected void downloadImage(String src, String output, String formatName) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new URL(src));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File outputfile = new File(output);
        try {
            ImageIO.write(bufferedImage, formatName, outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected List<WebElement> findAll(SearchContext root, By by) {
        return retry().until(d -> root.findElements(by));
    }

    protected List<WebElement> findAll(By by) {
        return retry().until(d -> d.findElements(by));
    }

    protected WebElement findUnique(By by) {
        return findUnique(driver, by);
    }

    protected WebElement findUnique(SearchContext root, By by) {
        List<WebElement> elements = retry().until(d -> root.findElements(by));
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("No elements found, expected 1");
        }
        if (elements.size() > 1) {
            throw new IllegalArgumentException(elements.size() + " elements found, expected 1");
        }
        return elements.get(0);
    }

    protected void get(String url) {
        log.info("Open page {}", url);
        driver.get(url);
    }

    protected WebElement getByClassUnique(SearchContext root, String className) {
        return findUnique(root, By.className(className));
    }

    protected WebElement getByClassUnique(String className) {
        return getByClassUnique(driver, className);
    }

    protected void init() {
        driver = new FirefoxDriver();
    }

    protected WebDriverWait retry() {
        return new WebDriverWait(driver, Duration.ofSeconds(3));
    }
}
