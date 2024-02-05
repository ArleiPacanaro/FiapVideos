package com.challenge.videos.external.api;

import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.restassured.RestAssured;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SpringBootTest
@Testcontainers
public class VideoRestControllerIT {

    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    // Por algum motivo que ainda desconheço a WebTestClient dá conflito com SpringBootTests

    @Autowired
    private VideoRepository videoRepository;

    static JSONObject jsonObject;


    @BeforeAll
    static void setup(@Autowired VideoRepository videoRepository) throws JSONException {
        mongoDBContainer.start();

        RestAssured.port = 8080;

        Integer id = 4;
        String titulo = "Rambo 02";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao =  LocalDate.of(1985,10,01);

        String sData = "01/10/1985";
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;


        jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("titulo", titulo);
        jsonObject.put("descricao", descricao);
        jsonObject.put("urlVideo", urlVideo);
        jsonObject.put("dataPublicacao", sData);
        jsonObject.put("categoria", "ACAO");
        jsonObject.put("favorito", 3);
        jsonObject.put("visualizacoes", 4);


        VideoModel video1  = new VideoModel(1, "Rambo", "Filme de Guerra Rambo1","http://filmes.com.br/rambo", dataPublicacao, VideosCategorias.GUERRA, 1 ,2);
        VideoModel video2 = new VideoModel(2, "Uma linda Mulher", "Filme Romance", "http://filmes.com.br/linda-mulher", LocalDate.of(1999,12,01),VideosCategorias.ROMANCE, 2, 3);
        VideoModel video3 = new VideoModel(3, "Tropa de Elite", "Filme policial brasileiro","http://filmes.com.br/tropa-elite", LocalDate.of(2007,10,05),VideosCategorias.ACAO, 3,4);


        var registro1 = videoRepository.save(video1);
        var registro2 = videoRepository.save(video2);
        var registro3 =videoRepository.save(video3);

        assertThat(registro1.block()).isEqualTo(video1);
        assertThat(registro2.block()).isEqualTo(video2);
        assertThat(registro3.block()).isEqualTo(video3);
    }

    @AfterAll
    static void tearDown(){

        mongoDBContainer.close();
    }

    @Test
    void deveRegistrarVideo() throws JSONException {

        // Biblioteca do Rest Assured para testar de forma integrada o Controller, tratar o exception por conflito no POM xml
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonObject.toString())
                .log().all()
        .when()
                .post("/videos")

        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
                // aqui usamos o schema e a configuração do pom io.rest-assured  \ json-schema-validator
                // Vaida o retorno dos campos do Json
                //.body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"));
                // assertThat(1).isEqualTo(1);
    }


    @Test
    void deveAtualizarVideo() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonObject.toString())
                .log().all()
        .when()
                .put("/videos/4")

        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    void deveDeletarVideo() {

            Integer id = 3;
            when()
                    .delete("/videos/{id}",id)
            .then()
                    .statusCode(HttpStatus.OK.value());

    }


    @Test
    void develistarVideos() {

        given()
                    .queryParam("page",0)
                    .queryParam("size",10)
        .when()
                    .get("/videos/listar")
            .then()
                    .statusCode(HttpStatus.OK.value());

    }


    @Test
    void develistarVideosPorTitulo() {
        given()
                .queryParam("titulo","Uma linda Mulher")
        .when()
                .get("/videos/listar/titulo")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void  develistarVideosPorData() {
            given()
                    .queryParam("data","01/12/1999")
             .when()
                    .get("/videos/listar/dataPublicacao")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }


    @Test
    void develistarVideosPorCategoria() {
        given()
                .queryParam("categoria","ACAO")
        .when()
                .get("/videos/listar/categoria")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void develistarVideosRecomendados() {
        given()
                .queryParam("categoria","ACAO")
        .when()
                .get("/videos/recomendados")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deveBuscarEstatistica() {
       when()
                .get("/videos/estatisticas")
       .then()
                .statusCode(HttpStatus.OK.value());
    }


}
