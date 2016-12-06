package me.giannists.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.giannists.persistence.model.enums.BookTopic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "watermark")
public class Watermark extends AbstractEntity {

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "topic")
    @Enumerated(EnumType.STRING)
    private BookTopic topic;

}
