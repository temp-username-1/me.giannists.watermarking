package me.giannists.service;

import me.giannists.persistence.DocumentDao;
import me.giannists.persistence.model.Document;
import me.giannists.persistence.model.Watermark;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class WatermarkServiceTest {

    private Document TEST_DOCUMENT;

    private Watermark TEST_WATERMARK;

    @Mock
    private DocumentDao documentDao;

    @InjectMocks
    private WatermarkService watermarkService;

    @Before
    public void init() {
        TEST_DOCUMENT = new Document();
        TEST_DOCUMENT.setId(BigInteger.ONE);

        TEST_WATERMARK = new Watermark();
        TEST_WATERMARK.setId(BigInteger.valueOf(2));
    }

    @Test
    public void createNewWatermarkShouldStoreDocument() {
        // given
        when(documentDao.findOne(TEST_DOCUMENT.getId())).thenReturn(TEST_DOCUMENT);

        //when
        watermarkService.createNewWatermark(TEST_WATERMARK, TEST_DOCUMENT.getId());

        //then
        verify(documentDao, times(1)).save(TEST_DOCUMENT);
        assertEquals(TEST_WATERMARK, TEST_DOCUMENT.getWatermark());
    }

    @Test
    public void createNewWatermarkForInvalidDocumentShouldThrowException() {
        // given
        when(documentDao.findOne(TEST_DOCUMENT.getId())).thenReturn(null);

        //when
        assertThatThrownBy(() -> watermarkService.createNewWatermark(TEST_WATERMARK, TEST_DOCUMENT.getId()))
                .isInstanceOf(EntityNotFoundException.class);

    }
}
