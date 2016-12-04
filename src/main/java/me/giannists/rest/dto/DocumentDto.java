package me.giannists.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.giannists.persistence.model.Document;
import me.giannists.persistence.model.enums.DocumentType;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DocumentDto {

    BigInteger ticket;

    @ApiModelProperty(position = 1, required = true)
    @NotNull
    private DocumentType type;

    @ApiModelProperty(position = 2, required = true)
    @NotNull
    private String title;

    @ApiModelProperty(position = 3, required = true)
    @NotNull
    private String author;

    @ApiModelProperty(hidden = true)
    @JsonProperty("watermark")
    private WatermarkDto watermarkDto;

    @ApiModelProperty(hidden = true)
    private Boolean pendingWatermark;

    public static DocumentDto of(Document document) {
        return DocumentDto.builder()
                .ticket(document.getId())
                .type(document.getType())
                .title(document.getTitle())
                .author(document.getAuthor())
                .build();
    }

    public static DocumentDto ofPendingWatermark(Document document) {
        return DocumentDto.builder()
                .ticket(document.getId())
                .pendingWatermark(true)
                .build();
    }

    public static DocumentDto ofWatermark(Document document) {
        return DocumentDto.builder()
                .ticket(document.getId())
                .type(document.getType())
                .title(document.getTitle())
                .author(document.getAuthor())
                .watermarkDto(WatermarkDto.of(document.getWatermark()))
                .build();
    }
}
