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

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        //Page<VideoRecord> page = new PageImpl<>(Collections.singletonList(VideoRecord));



    }

    @Test
    void deveRegistrarVideo() {

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

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

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        //Ajuste
        Integer favorito = 1700;
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);


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

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);


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

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        VideoEntity videoEntity= new VideoEntity(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);


        List<VideoModel> videos = Arrays.asList(videoModel);
        List<VideoRecord> videosRecord = Arrays.asList(videoRecord);

        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataPublicacao"));


        Page<VideoModel> pagina = new PageImpl<>(Collections.singletonList(videoModel));

         /*

        when(videoCrudUseCase.listarVideos(page, size, videoRepository)).thenReturn(Mono.just(pagina));

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/videos")
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.content[0].id").isEqualTo("1")



        when(videoCrudUseCase.listarVideos(page, size, videoRepository).th(

        videoCrudUseCase.listarVideos(page, size, videoRepository)
                .collectList()
                .zipWith(videoRepository.findAllBy(pageable).count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()))
        );


        when(videoCrudUseCase.listarVideos(page, size, videoRepository)
                .collectList()
                .zipWith(videoRepository.findAllBy(pageable).count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2())))
                .thenReturn(Mono<Page<videos>>);




         */


        webTestClient.get()
                .uri("/videos/listar?page=0&size=2")
                .exchange()
                .expectStatus().is5xxServerError();




    }



    @Test
    void develistarVideosPorTitulo() {

        VideoModel videoModel1  = new VideoModel();
        String nomeFilme = "Rambo1";

        videoModel1.setId(1);
        videoModel1.setTitulo(nomeFilme);
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(VideosCategorias.GUERRA);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);

        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500;
        Integer visualizacoes = 100;

        List<VideoModel> videos = Arrays.asList(videoModel1);




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


        VideoModel videoModel1  = new VideoModel();
        LocalDate dataPulicacao = LocalDate.of(1985,10,01);
        String nomeFilme = "Rambo1";

        videoModel1.setId(1);
        videoModel1.setTitulo(nomeFilme);
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(VideosCategorias.GUERRA);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);

        List<VideoModel> videos = Arrays.asList(videoModel1);


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

        VideoModel videoModel1  = new VideoModel();
        LocalDate dataPulicacao = LocalDate.of(1985,10,01);
        String nomeFilme = "Rambo1";
        VideosCategorias categoria = VideosCategorias.GUERRA;

        videoModel1.setId(1);
        videoModel1.setTitulo(nomeFilme);
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(categoria);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);

        List<VideoModel> videos = Arrays.asList(videoModel1);


        when(videoCrudUseCase.listarVideosPorCategoria(any(VideosCategorias.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert

        webTestClient.get()
                .uri("/videos/listar/categoria?categoria=ACAO")
                .exchange()
                .expectStatus().isOk();


    }


    @Test
    void develistarVideosRecomendados() {


        VideoModel videoModel1  = new VideoModel();
        LocalDate dataPulicacao = LocalDate.of(1985,10,01);
        String nomeFilme = "Rambo1";
        VideosCategorias categoria = VideosCategorias.GUERRA;

        videoModel1.setId(1);
        videoModel1.setTitulo(nomeFilme);
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(categoria);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);

        List<VideoModel> videos = Arrays.asList(videoModel1);


        when(videoCrudUseCase.listarVideosPorCategoria(any(VideosCategorias.class),any(VideoRepository.class)))
                .thenReturn((Flux.fromIterable(videos)));

        // Assert

        webTestClient.get()
                .uri("/videos/recomendados?categoria=ACAO")
                .exchange()
                .expectStatus().isOk();


    }

    @Test
    void deveBuscarEstatistica() {

        VideoModel videoModel1  = new VideoModel();
        LocalDate dataPulicacao = LocalDate.of(1985,10,01);
        String nomeFilme = "Rambo1";
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer qtd = 100;

        videoModel1.setId(1);
        videoModel1.setTitulo(nomeFilme);
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(categoria);
        videoModel1.setFavorito(qtd);
        videoModel1.setVisualizacoes(10000);


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
