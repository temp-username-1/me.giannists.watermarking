package me.giannists.persistence;

import me.giannists.persistence.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DocumentDao extends JpaRepository<Document, BigInteger> {
}
