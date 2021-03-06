package me.giannists.service;

import lombok.extern.slf4j.Slf4j;
import me.giannists.persistence.DocumentDao;
import me.giannists.persistence.model.Document;
import me.giannists.persistence.model.Watermark;
import me.giannists.persistence.model.enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@Service
public class WatermarkService {

    @Autowired
    private DocumentDao documentDao;

    public Watermark createNewWatermark(Watermark watermark, BigInteger documentId) {

        Document document = Optional.ofNullable(documentDao.findOne(documentId))
                .orElseThrow(() -> new EntityNotFoundException("Document not found. Id: " + documentId));

        assertWatermarkValidity(watermark, document);

        document.setWatermark(watermark);
        documentDao.save(document);

        log.info("{class=WatermarkService, method=createNewWatermark, documentId={}}", documentId);
        return watermark;
    }

    private void assertWatermarkValidity(Watermark watermark, Document document) {
        if(document.getType() == DocumentType.JOURNAL && watermark.getTopic() != null) {
            throw new IllegalArgumentException("Journals not allowed to have topic");
        }
    }
}
