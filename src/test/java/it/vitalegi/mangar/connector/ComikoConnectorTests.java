package it.vitalegi.mangar.connector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ComikoConnectorTests {

    @InjectMocks
    ComikoConnector comikoConnector;

    @DisplayName("GIVEN chapter label is complete WHEN retrieving chapter id THEN chapter id is retrieved")
    @Test
    void test_getChapterId_fullLabel_shouldReturnValue() {
        String chapterId = comikoConnector.getChapterId("Volume 35 Chapter 316");
        assertEquals("316", chapterId);
    }

    @DisplayName("GIVEN chapter label contains only the chapter WHEN retrieving chapter id THEN chapter id is " +
            "retrieved")
    @Test
    void test_getChapterId_onlyChapterLabel_shouldReturnValue() {
        String chapterId = comikoConnector.getChapterId("Chapter 316");
        assertEquals("316", chapterId);
    }

    @DisplayName("GIVEN chapter name exists, unknown format WHEN retrieving chapter name THEN chapter name is " +
            "retrieved as is")
    @Test
    void test_getChapterName_hasNameWithUnknownFormat_shouldReturnValue() {
        String value = comikoConnector.getChapterName("Test 123");
        assertEquals("Test 123", value);
    }

    @DisplayName("GIVEN chapter name exists WHEN retrieving chapter name THEN chapter name is retrieved")
    @Test
    void test_getChapterName_hasName_shouldReturnValue() {
        String value = comikoConnector.getChapterName(" : Test 123");
        assertEquals("Test 123", value);
    }

    @DisplayName("GIVEN chapter name doesn't exists WHEN retrieving chapter name THEN null is retrieved")
    @Test
    void test_getChapterName_noName_shouldReturnNull() {
        String value = comikoConnector.getChapterName((String)null);
        assertNull(value);
    }

    @DisplayName("GIVEN chapter label is complete WHEN retrieving volume id THEN volume id is retrieved")
    @Test
    void test_getVolumeId_fullLabel_shouldReturnValue() {
        String volumeId = comikoConnector.getVolumeId("Volume 35 Chapter 316");
        assertEquals("35", volumeId);
    }

    @DisplayName("GIVEN chapter label contains only the chapter WHEN retrieving volume id THEN default value is " +
            "retrieved")
    @Test
    void test_getVolumeId_onlyChapterLabel_shouldReturnValue() {
        String volumeId = comikoConnector.getVolumeId("Chapter 316");
        assertEquals(ComikoConnector.DEFAULT_VOLUME_ID, volumeId);
    }
}
