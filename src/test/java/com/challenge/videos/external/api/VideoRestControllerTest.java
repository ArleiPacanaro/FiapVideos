package com.challenge.videos.external.api;


import com.challenge.videos.entities.VideoEntity;
import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import com.challenge.videos.usecase.impl.VideoCrudUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = VideoRestController.class)
public class VideoRestControllerTest {


    @MockBean
    private VideoCrudUseCase videoCrudUseCase;

    @MockBean
    private VideoRepository videoRepository;

    @Autowired
    private WebTestClient webTestClient;

    VideoRecord videoRecord;
    VideoModel videoModel;

    @BeforeEach
    public void setUp() {

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;

        videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);



    }

    @Test
    void deveRegistrarVideo() {


        when(videoRepository.save(any(VideoModel.class)))
                .thenReturn(Mono.just(videoModel));

        webTestClient.post()
                .uri("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(videoRecord), VideoRecord.class)
                .exchange()
                .expectStatus().isOk();

    }



    @Test
    void deveAtualizarVideo() {

        //Ajuste
        Integer favorito = 1700;
        Integer visualizacoes = 100;
        videoModel.setFavorito(favorito);
        videoModel.setVisualizacoes(visualizacoes);


        when(videoRepository.findById(any(Integer.class)))
                .thenReturn(Mono.just(videoModel));

        webTestClient.put()
                .uri("/videos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(videoRecord), VideoRecord.class)
                .exchange()
                .expectStatus().isOk();


    }


    @Test
    void deveDeletarVideo() {


        webTestClient.delete()
                .uri("/videos/1")
                .exchange()
                .expectStatus().isOk();

        verify(videoCrudUseCase, times(1))
                .deletarVideo(any(Integer.class),any(VideoRepository.class));

    }

    @Test
    void develistarVideos() {

        Integer page = 0;
        Integer size = 10;

        Pageable pageable = PageRequest.of(page, size);


        Page<VideoModel> pagina = new PageImpl<>(Collections.singletonList(videoModel));


        when(videoCrudUseCase.listarVideos(any(Integer.class),any(Integer.class),any(VideoRepository.class)))
                .thenReturn((Mono.just(pagina)));


        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/videos/listar?page=0&size=2")
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo("1");



    }



    @Test
    void develistarVideosPorTitulo() {


        List<VideoModel> videos = Arrays.asList(videoModel);

        when(videoCrudUseCase.listarVideosPorTitulo(any(String.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert
        webTestClient.get()
                .uri("/videos/listar/titulo?titulo=rambo1")
                .exchange()
                .expectStatus().isOk();

    }


    @Test
    void  develistarVideosPorData() {

        List<VideoModel> videos = Arrays.asList(videoModel);

        when(videoCrudUseCase.listarVideosPorData(any(LocalDate.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert

        webTestClient.get()
                .uri("/videos/listar/dataPublicacao?data=01/10/1985")
                .exchange()
                .expectStatus().isOk();
    }



    @Test
    void develistarVideosPorCategoria() {


        List<VideoModel> videos = Arrays.asList(videoModel);

        when(videoCrudUseCase.listarVideosPorCategoria(any(VideosCategorias.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert

        webTestClient.get()
                .uri("/videos/listar/categoria?categoria=GUERRA")
                .exchange()
                .expectStatus().isOk();


    }


    @Test
    void develistarVideosRecomendados() {

        List<VideoModel> videos = Arrays.asList(videoModel);

        when(videoCrudUseCase.listarVideosPorCategoria(any(VideosCategorias.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert

        webTestClient.get()
                .uri("/videos/recomendados?categoria=GUERRA")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void deveBuscarEstatistica() {


        Integer qtd = 100;

        videoModel.setFavorito(qtd);
        videoModel.setVisualizacoes(10000);

        VideoEstatisticasModel videoEstatisticasModel = new VideoEstatisticasModel(qtd,10000);

        when(videoCrudUseCase.buscarEstatisticas(any(VideoRepository.class)))
                .thenReturn((Mono.just(videoEstatisticasModel)));

        // Assert

        webTestClient.get()
                .uri("/videos/estatisticas")
                .exchange()
                .expectStatus().isOk();

    }

}
