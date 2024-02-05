package com.challenge.videos.external.respository;

import com.challenge.videos.entities.VideoEntity;
import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Testcontainers
public class VideoRespositoryIT {

    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Autowired
    private  VideoRepository videoRepository;


    @BeforeAll
   static void setup(@Autowired VideoRepository videoRepository) {
        mongoDBContainer.start();
        VideoModel video1 = new VideoModel(1, "Rambo", "Filme de Guerra Rambo1","http://filmes.com.br/rambo",LocalDate.of(1985,10,01),VideosCategorias.GUERRA, 1 ,2);
        VideoModel video2 = new VideoModel(2, "Uma linda Mulher", "Filme Romance", "http://filmes.com.br/linda-mulher", LocalDate.of(1999,12,01),VideosCategorias.ROMANCE, 2, 3);
        VideoModel video3 = new VideoModel(3, "Tropa de Elite", "Filme policial brasileiro","http://filmes.com.br/tropa-elite", LocalDate.of(2007,10,05),VideosCategorias.ACAO, 3,4);
        VideoEntity videoEntity = new VideoEntity(3, "Tropa de Elite", "Filme policial brasileiro","http://filmes.com.br/tropa-elite", LocalDate.of(2007,10,05),VideosCategorias.ACAO, 3,4);
        VideoModel video4 = new VideoModel
                (
                        videoEntity.getId(),
                        videoEntity.getTitulo(),
                        videoEntity.getDescricao(),
                        videoEntity.getUrlVideo(),
                        videoEntity.getDataPublicacao(),
                        videoEntity.getCategoria(),
                        videoEntity.getFavorito(),
                        videoEntity.getVisualizacoes()

                );

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
    void deveListarTodosVideos() {
        Pageable pageable = PageRequest.of(0, 3);
        Flux<VideoModel> lista = videoRepository.findAllBy(pageable);
        assertThat(lista.collectList().block().size()).isEqualTo(3);
    }

    @Test
    void deveListarVideoPorTitulo() {
        Flux<VideoModel> lista = videoRepository.findByTituloContainingIgnoreCase("Tropa de Elite");
        assertThat(lista.collectList().block().size()).isGreaterThan(0);
    }

    @Test
    void findByDataPublicacao() {
        Flux<VideoModel> lista = videoRepository.findByDataPublicacao(LocalDate.of(1999,12,01));
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    void findByCategoria() {
        Flux<VideoModel> lista = videoRepository.findByCategoria(VideosCategorias.ROMANCE);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    void ListarVideosRecomendados() {
        Flux<VideoModel> lista = videoRepository.ListarVideosRecomendados(VideosCategorias.ROMANCE, 1);
        assertThat(lista.collectList().block().size()).isEqualTo(1);
    }

    @Test
    void listarEstatisticas() {
        Mono<VideoEstatisticasModel> listarEstatisticas = videoRepository.listarEstatisticas();
        assertThat(listarEstatisticas.block().getQtdTotalVideosFavoritos()).isNotZero();
        //assertThat(listarEstatisticas.block().getAvgTotalVideosAssistidos()).isEqualTo(16.125f);
    }

    @Test
    void deveRegistrarVideo() {
        VideoModel novoVideo = new VideoModel(4, "Megatubarão 2", "Filme de tubarão","http://filmes.com.br/Megatubarao-2", LocalDate.of(2023,8,03),VideosCategorias.ACAO, 0,0);
        var registro = videoRepository.save(novoVideo);
        assertThat(registro.block()).isEqualTo(novoVideo);
    }

    @Test
    void devePermitirAtualizarVideo() {
        VideoModel videoEdicao = new VideoModel(1, "Megatubarão 2", "Filme de tubarão","http://filmes.com.br/Megatubarao-2", LocalDate.of(2023,8,03),VideosCategorias.ACAO, 1,2);
        var registro = videoRepository.save(videoEdicao);
        assertThat(registro.block()).isEqualTo(videoEdicao);
        //
    }

    @Test
    void devePermitirRemoverVideo() {
        StepVerifier.create(videoRepository.deleteById(1)).verifyComplete();
        var videoRemovido = videoRepository.existsById(1);
        assertThat(videoRemovido.block()).isEqualTo(false);

    }

}
