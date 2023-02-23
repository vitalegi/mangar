package it.vitalegi.mangar.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class NamingServiceTests {

    @InjectMocks
    NamingService namingService;

    @Test
    void test_sanitize_reservedCharactersAreStripped() {
        String input = "abcà 123><:\"/\\|?*";
        assertEquals("abcà 123", namingService.sanitize(input));
    }

    @Test
    void test_sanitize_newlinesAreStripped() {
        String input = "abcà\n123><:\"/\\|?*";
        assertEquals("abcà123", namingService.sanitize(input));
    }
}
