package me.giannists.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private BigInteger id;

    @CreatedDate
    @Column(name = "created", updatable = false)
    private Date created;

    @Column(name = "updated")
    private Date updated;

    public AbstractEntity() {}

    @PreUpdate
    public void onUpdate() {
        this.updated = Date.from(Instant.now());
    }

    @PrePersist
    public void onCreate() {
        this.created = Date.from(Instant.now());
        this.updated = this.created;
    }

}
