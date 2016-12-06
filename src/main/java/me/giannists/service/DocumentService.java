package me.giannists.service;

import me.giannists.persistence.DocumentDao;
import me.giannists.persistence.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentDao documentDao;

    public Document createNewDocument(Document document) {
        return documentDao.save(document);
    }

    public Optional<Document> findOne(BigInteger id) {
        return Optional.ofNullable(documentDao.findOne(id));
    }
}
