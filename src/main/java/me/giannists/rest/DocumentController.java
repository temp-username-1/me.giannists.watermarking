package me.giannists.rest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.giannists.persistence.model.Document;
import me.giannists.persistence.model.Watermark;
import me.giannists.rest.dto.DocumentDto;
import me.giannists.rest.dto.WatermarkDto;
import me.giannists.service.DocumentService;
import me.giannists.service.WatermarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigInteger;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private WatermarkService watermarkService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful document insertion", response = DocumentDto.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewDocument(@RequestBody @Valid DocumentDto documentDto) {

        return ResponseEntity.ok(
                DocumentDto.of(
                    documentService.createNewDocument(
                            Document.builder()
                                    .author(documentDto.getAuthor())
                                    .title(documentDto.getTitle())
                                    .type(documentDto.getType())
                                    .build())
                )
        )

        return documentService.createNewDocument(
                Document.builder()
                    .author(documentDto.getAuthor())
                    .title(documentDto.getTitle())
                    .type(documentDto.getType())
                    .build())
            .map(DocumentDto::of)
            .map(ResponseEntity::ok)
            .map(ResponseEntity.class::cast)
            .orElse(ResponseEntity.notFound().build());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful watermark insertion", response = DocumentDto.class),
            @ApiResponse(code = 404, message = "Document's id not found")
    })
    @RequestMapping(path = "{id}/watermarks", method = RequestMethod.POST)
    public ResponseEntity createWatermark(@RequestBody WatermarkDto watermarkDto,
                                          @PathVariable BigInteger id) {

        return watermarkService.createNewWatermark(
                Watermark.builder()
                        .content(watermarkDto.getContent())
                        .title(watermarkDto.getTitle())
                        .author(watermarkDto.getAuthor())
                        .topic(watermarkDto.getTopic())
                        .build(), id)
                .map(WatermarkDto::of)
                .map(ResponseEntity::ok)
                .map(ResponseEntity.class::cast)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful document retrieval. If the " +
                    "document is not watermarked, only \"ticket\" will be " +
                    "contained in response.", response = DocumentDto.class),
            @ApiResponse(code = 404, message = "Document's id not found")
    })
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity findDocumentByTicket(@PathVariable BigInteger id) {

        return documentService.findOne(id)
                .map(d -> d.getWatermark() == null ? DocumentDto.ofPendingWatermark(d) : DocumentDto.ofWatermark(d))
                .map(ResponseEntity::ok)
                .map(ResponseEntity.class::cast)
                .orElse(ResponseEntity.notFound().build());
    }
}
