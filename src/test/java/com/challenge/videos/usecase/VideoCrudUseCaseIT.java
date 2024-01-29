package com.challenge.videos.usecase;


import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import com.challenge.videos.usecase.impl.VideoCrudUseCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class VideoCrudUseCaseIT {
    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Autowired
    public VideoCrudUseCase videoCrudUseCase;

    @Autowired
    private VideoRepository videoRepository;

    @BeforeAll
    static void setup(@Autowired VideoRepository videoRepository) {
        mongoDBContainer.start();
        VideoModel video1 = new VideoModel(1, "Rambo", "Filme de Guerra Rambo1","http://filmes.com.br/rambo", LocalDate.of(1985,10,01), VideosCategorias.GUERRA, 1 ,2);
        VideoModel video2 = new VideoModel(2, "Uma linda Mulher", "Filme Romance", "http://filmes.com.br/linda-mulher", LocalDate.of(1999,12,01),VideosCategorias.ROMANCE, 2, 3);
        VideoModel video3 = new VideoModel(3, "Tropa de Elite", "Filme policial brasileiro","http://filmes.com.br/tropa-elite", LocalDate.of(2007,10,05),VideosCategorias.ACAO, 3,4);

        var registro1 = videoRepository.save(video1);
        var registro2 = videoRepository.save(video2);
        var registro3 = videoRepository.save(video3);

        assertThat(registro1.block()).isEqualTo(video1);
        assertThat(registro2.block()).isEqualTo(video2);
        assertThat(registro3.block()).isEqualTo(video3);
    }

    @AfterAll
    static void tearDown(){
        mongoDBContainer.close();
    }


    @Test
    public void deveRegistrarVideo() {
        VideoRecord novoVideo = new VideoRecord(4, "Megatubarão 2", "Filme de tubarão","http://filmes.com.br/Megatubarao-2", LocalDate.of(2023,8,03),VideosCategorias.ACAO, 0,0);
        var registro = videoCrudUseCase.registrarVideo(Mono.just(novoVideo),videoRepository);
        assertThat(registro.block().id()).isEqualTo(novoVideo.id());
    }

    @Test
    public void DeveAtualizarVideo() {
        Integer id =1;
        VideoRecord videoEdicao = new VideoRecord(1, "Megatubarão 2", "Filme de tubarão","http://filmes.com.br/Megatubarao-2", LocalDate.of(2023,8,03),VideosCategorias.ACAO, 1,2);
        var registro = videoCrudUseCase.atualizarVideo(Mono.just(videoEdicao), id,videoRepository);
        assertThat(registro.block().titulo()).isEqualTo(videoEdicao.titulo());
    }


    @Test
    public void DevedeletarVideo() {
        StepVerifier.create(videoCrudUseCase.deletarVideo(1, videoRepository)).verifyComplete();
        var videoRemovido = videoRepository.existsById(1);
        assertThat(videoRemovido.block()).isEqualTo(false);
    }


    @Test
    public void develistarVideos() {
        var lista = videoCrudUseCase.listarVideos(0,3, videoRepository);
        assertThat(lista.collectList().block().size()).isEqualTo(3);
    }


    @Test
    public  void  develistarVideosPorTitulo() {
       var lista = videoCrudUseCase
               .listarVideosPorTitulo("Tropa de Elite", videoRepository);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    public  void develistarVideosPorData() {
        var lista = videoCrudUseCase
                .listarVideosPorData(LocalDate.of(1999,12,01), videoRepository);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    public  void develistarVideosPorCategoria() {
        var lista = videoCrudUseCase.listarVideosPorCategoria(VideosCategorias.ROMANCE, videoRepository);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    public void develistarVideosRecomendados() {
        var lista = videoCrudUseCase.listarVideosRecomendados(VideosCategorias.ROMANCE, videoRepository);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }


    @Test
    public void deveBuscarEstatisticas() {
        Mono<VideoEstatisticasModel> listarEstatisticas = videoCrudUseCase.buscarEstatisticas(videoRepository);
        assertThat(listarEstatisticas.block().getQtdTotalVideosFavoritos()).isNotZero();
         }



}
