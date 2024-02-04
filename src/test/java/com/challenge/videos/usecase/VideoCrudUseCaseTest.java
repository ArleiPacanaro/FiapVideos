package com.challenge.videos.usecase;

import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import com.challenge.videos.usecase.impl.VideoCrudUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoCrudUseCaseTest {


    // Trabalhar com Fake - Mockito.
    AutoCloseable mock;

    private VideoCrudUseCase videoCrudUseCase;

    VideoRecord videoRecord;
    VideoModel videoModel;

    VideoModel videoModel1;
    VideoModel videoModel2;

    @Mock
    private VideoRepository videoRepository;

    @BeforeEach  // antes de cada teste
    void setup(){

       mock = MockitoAnnotations.openMocks(this);
       videoCrudUseCase = new VideoCrudUseCase();

        // Assert
        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 0;
        Integer visualizacoes = 100;

        videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes
        );

        videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);


        videoModel1 =  videoModel;
        videoModel2 = new VideoModel();



        videoModel2.setId(2);
        videoModel2.setTitulo("Uma linda Mulher");
        videoModel2.setDescricao("Filme Romance");
        videoModel2.setDataPublicacao(LocalDate.of(1999,12,01));
        videoModel2.setUrlVideo("http://filmes.com.br/lindamulher");
        videoModel2.setCategoria(VideosCategorias.ROMANCE);
        videoModel2.setFavorito(1000);
        videoModel2.setVisualizacoes(100001);


    }

    // Limpar o mock da memoria.
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }



    @Test
    public void deveRegistrarVideo() throws IllegalAccessException {


        when(videoRepository.save(any(VideoModel.class)))
                .thenReturn(Mono.just(videoModel));

        var monoVideo = videoCrudUseCase.registrarVideo(Mono.just(videoRecord),videoRepository);

        // Assert
        monoVideo.subscribe(mono -> {
            assertThat(mono.id()).isNotNull().isEqualTo(videoModel.getId());
        });

        verify(videoRepository, times(1)).save(any(VideoModel.class));


    }


    @Test
    public void GerarExcecao_NoDeveAtualizarVideo_RegistroNaoExiste() {

        // Assert
        Integer id = 10;

        assertThatThrownBy(() -> videoCrudUseCase.atualizarVideo(Mono.just(videoRecord),id,videoRepository))
                .isInstanceOf(RuntimeException.class);

        verify(videoRepository, times(1)).findById(any(Integer.class));

    }


    @Test
    public void DeveAtualizarVideo()  {

        // Assert
        Integer id = 1;
        Integer favorito = 1500; // Ajuste
        videoModel.setFavorito(favorito);

        when(videoRepository.findById(any(Integer.class)))
                .thenReturn(Mono.just(videoModel));

        var videoAtualizado = videoCrudUseCase.atualizarVideo(Mono.just(videoRecord),id,videoRepository);

        // Assert
        videoAtualizado.subscribe(mono -> {
            assertThat(favorito).isEqualTo(videoModel.getFavorito());
        });
        verify(videoRepository, times(1)).findById(any(Integer.class));

    }


    @Test
    public void GerarExcecao_NoDeveDeletarVideo_QuandoIdNaoExiste() {

        Integer id = 10;
        assertThatThrownBy(() -> videoCrudUseCase.deletarVideo(id,videoRepository))
                .isInstanceOf(RuntimeException.class);


        // Assert
        verify(videoRepository, times(1)).findById(any(Integer.class));
    }


    @Test
    public void DeveDeletarVideo() {


        Integer id = 1;

        when(videoRepository.findById(any(Integer.class)))
                .thenReturn(Mono.just(videoModel));

        var deletado = videoCrudUseCase.deletarVideo(id,videoRepository);

        // Assert
        verify(videoRepository, times(1)).findById(any(Integer.class));
        verify(videoRepository, times(1)).deleteById(any(Integer.class));
    }



    @Test
    public void DevelistarVideos() {



        Integer page = 0;
        Integer size = 10;

        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataPublicacao"));

        List<VideoModel> videos = Arrays.asList(videoModel);

        when(videoRepository.findAllBy(any(Pageable.class)))
                .thenReturn((Flux.fromIterable(videos)));


        var videoModelFlux = this.videoRepository.findAllBy(pageRequest);

        Mono<Page<VideoModel>> x =  videoModelFlux.collectList()
                .zipWith(videoRepository.findAllBy(pageRequest).count())
                .map(p -> new PageImpl<>(p.getT1(), pageRequest, p.getT2()));



        // Assert
        //assertEquals(videos, lista.collectList().block());
        assertThat(x.block().getTotalElements()).isEqualTo(1);
        verify(videoRepository, times(2)).findAllBy(any(Pageable.class));

    }



    @Test
    public  void  develistarVideosPorTitulo() {

        String nomeFilme = "Rambo1";

        List<VideoModel> videos = Arrays.asList(videoModel1);

        when(videoRepository.findByTituloContainingIgnoreCase(nomeFilme))
                .thenReturn((Flux.fromIterable(videos)));

        var result = videoCrudUseCase.listarVideosPorTitulo(nomeFilme,videoRepository);

        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByTituloContainingIgnoreCase(any(String.class));

    }


    @Test
    public  void develistarVideosPorData() {

        LocalDate dataPulicacao = LocalDate.of(1985,10,01);
        String nomeFilme = "Rambo1";

        List<VideoModel> videos = Arrays.asList(videoModel1);

        when(videoRepository.findByDataPublicacao(dataPulicacao))
                .thenReturn((Flux.fromIterable(videos)));

        var result = videoCrudUseCase.listarVideosPorData(dataPulicacao,videoRepository);

        // Assert
        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByDataPublicacao(any(LocalDate.class));

    }


    @Test
    public  void develistarVideosPorCategoria() {


        VideosCategorias categoria = VideosCategorias.GUERRA;

        List<VideoModel> videos = Arrays.asList(videoModel1);
        when(videoRepository.findByCategoria(categoria))
                .thenReturn((Flux.fromIterable(videos)));
        var result = videoCrudUseCase.listarVideosPorCategoria(categoria,videoRepository);

        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByCategoria(any(VideosCategorias.class));

    }



    @Test
    public void develistarVideosRecomendados() {

        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer qtd = 100;

        videoModel1.setCategoria(categoria);
        videoModel1.setFavorito(qtd);

        List<VideoModel> videos = Arrays.asList(videoModel1);

        when(videoRepository.ListarVideosRecomendados(categoria,qtd))
                .thenReturn((Flux.fromIterable(videos)));

        var result = videoCrudUseCase.listarVideosRecomendados(categoria,videoRepository);

 //assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1))
                .ListarVideosRecomendados(any(VideosCategorias.class),any(Integer.class));

    }



    @Test
    public void deveBuscarEstatisticas() {

        Integer qtd = 100;

        VideoEstatisticasModel videoEstatisticasModel = new VideoEstatisticasModel(qtd,10000);

        when(videoRepository.listarEstatisticas())
                .thenReturn((Mono.just(videoEstatisticasModel)));

        var result = videoCrudUseCase.buscarEstatisticas(videoRepository);
        // Assert

        result.subscribe(mono -> {
            assertThat(mono.getQtdTotalVideosFavoritos()).isNotNull()
                    .isEqualTo(videoEstatisticasModel.getQtdTotalVideosFavoritos());
        });

        verify(videoRepository, times(1))
                .listarEstatisticas();
    }



}
