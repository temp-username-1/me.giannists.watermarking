package me.giannists.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import me.giannists.persistence.model.enums.BookTopic;
import me.giannists.persistence.model.enums.DocumentType;
import me.giannists.rest.dto.DocumentDto;
import me.giannists.rest.dto.WatermarkDto;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentControllerIntegrationTest {
    private final String dtoAuthor = "dtoAuthor";
    private final String dtoTitle = "dtoTitle";
    private final String dtoContent = "dtoContent";

    @LocalServerPort
    int port;

    @Before
    public void init(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
    }

    @Test
    public void postNewValidDocumentShouldReturn200() {
        DocumentDto dto = DocumentDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .type(DocumentType.JOURNAL)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
        .when()
                .post("/documents")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.JOURNAL.name()));
    }

    @Test
    public void postNewInvalidDocumentShouldReturn400() {
        DocumentDto dto = DocumentDto.builder().build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/documents")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postNewValidWatermarkShouldReturn200() {
        // First post a new document
        DocumentDto documentDto = DocumentDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .type(DocumentType.BOOK)
                .build();

        Integer documentTicket = given()
                .contentType(ContentType.JSON)
                .body(documentDto)
                .when()
                .post("/documents")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.BOOK.name()))
                .extract()
                .path("ticket");

        // Then make the actual test call
        WatermarkDto watermarkDto = WatermarkDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .content(dtoContent)
                .topic(BookTopic.BUSINESS)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(watermarkDto)
                .when()
                .post("/documents/" + documentTicket + "/watermarks")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("content", Matchers.is(dtoContent))
                .body("topic", Matchers.is(BookTopic.BUSINESS.name()));
    }

    @Test
    public void postNewInvalidWatermarkShouldReturn404() {
        WatermarkDto watermarkDto = WatermarkDto.builder().build();

        given()
                .contentType(ContentType.JSON)
                .body(watermarkDto)
                .when()
                .post("/documents/" + 0 + "/watermarks")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void findValidDocumentByIdShouldReturn200() {
        // First post a new document
        DocumentDto documentDto = DocumentDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .type(DocumentType.JOURNAL)
                .build();

        Integer documentTicket = given()
                .contentType(ContentType.JSON)
                .body(documentDto)
                .when()
                .post("/documents")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.JOURNAL.name()))
                .extract()
                .path("ticket");

        // Then find the document by ticket number
        when()
                .get("/documents/" + documentTicket)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("ticket", Matchers.is(documentTicket))
                .body("pendingWatermark", Matchers.is(true));
    }

    @Test
    public void findValidWatermarkedDocumentByIdShouldReturn200() {
        // First post a new document
        DocumentDto documentDto = DocumentDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .type(DocumentType.BOOK)
                .build();

        Integer documentTicket = given()
                .contentType(ContentType.JSON)
                .body(documentDto)
                .when()
                .post("/documents")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.BOOK.name()))
                .extract()
                .path("ticket");

        // Then assign watermark
        WatermarkDto watermarkDto = WatermarkDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .content(dtoContent)
                .topic(BookTopic.BUSINESS)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(watermarkDto)
                .when()
                .post("/documents/" + documentTicket + "/watermarks")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("content", Matchers.is(dtoContent))
                .body("topic", Matchers.is(BookTopic.BUSINESS.name()));

        // Then verify that find returns correct entity
        when()
                .get("/documents/" + documentTicket)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("ticket", Matchers.is(documentTicket))
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.BOOK.name()))
                .body("watermark.author", Matchers.is(dtoAuthor))
                .body("watermark.title", Matchers.is(dtoTitle))
                .body("watermark.content", Matchers.is(dtoContent))
                .body("watermark.topic", Matchers.is(BookTopic.BUSINESS.name()));
    }


    @Test
    public void setInvalidWatermarkToJournalShouldReturn400() {
        // First post a new document
        DocumentDto documentDto = DocumentDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .type(DocumentType.JOURNAL)
                .build();

        Integer documentTicket = given()
                .contentType(ContentType.JSON)
                .body(documentDto)
                .when()
                .post("/documents")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("author", Matchers.is(dtoAuthor))
                .body("title", Matchers.is(dtoTitle))
                .body("type", Matchers.is(DocumentType.JOURNAL.name()))
                .extract()
                .path("ticket");

        // Then assign watermark
        WatermarkDto watermarkDto = WatermarkDto.builder()
                .author(dtoAuthor)
                .title(dtoTitle)
                .content(dtoContent)
                .topic(BookTopic.BUSINESS)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(watermarkDto)
                .when()
                .post("/documents/" + documentTicket + "/watermarks")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void findInvalidDocumentByIdShouldReturn404() {
        when()
                .get("/documents/" + 0)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
