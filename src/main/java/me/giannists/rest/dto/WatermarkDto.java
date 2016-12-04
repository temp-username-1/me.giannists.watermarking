package me.giannists.rest.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.giannists.persistence.model.Watermark;
import me.giannists.persistence.model.enums.BookTopic;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class WatermarkDto {

    private String content;

    private String title;

    private String author;

    private BookTopic topic;

    public static WatermarkDto of(Watermark watermark) {
        return WatermarkDto.builder()
                .content(watermark.getContent())
                .title(watermark.getTitle())
                .author(watermark.getAuthor())
                .topic(watermark.getTopic())
                .build();
    }
}
