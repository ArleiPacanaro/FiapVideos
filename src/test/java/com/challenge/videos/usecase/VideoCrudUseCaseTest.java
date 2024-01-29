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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
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

    @Mock
    private VideoRepository videoRepository;

    @BeforeEach  // antes de cada teste
    void setup(){

       mock = MockitoAnnotations.openMocks(this);
       videoCrudUseCase = new VideoCrudUseCase();
    }

    // Limpar o mock da memoria.
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }



    @Test
    public void deveRegistrarVideo() throws IllegalAccessException {

        // Assert
        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 0;
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes
        );

        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

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
        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 0;
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes
        );


        assertThatThrownBy(() -> videoCrudUseCase.atualizarVideo(Mono.just(videoRecord),id,videoRepository))
                .isInstanceOf(RuntimeException.class);


        verify(videoRepository, times(1)).findById(any(Integer.class));


    }


    @Test
    public void DeveAtualizarVideo()  {

        // Assert
        Integer id = 1;
        String titulo = "Rambo1";
        String descricao = "Filme de Guerra";
        String urlVideo = "http://www.filmes.com.br/rambo1";
        LocalDate dataPublicacao = LocalDate.of(1985,10,01);
        VideosCategorias categoria = VideosCategorias.GUERRA;
        Integer favorito = 1500; // Ajuste
        Integer visualizacoes = 100;

        VideoRecord videoRecord = new VideoRecord(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes
        );


        VideoModel videoModel = new VideoModel(
                id, titulo,  descricao, urlVideo, dataPublicacao, categoria, favorito, visualizacoes);

        when(videoRepository.findById(any(Integer.class)))
                .thenReturn(Mono.just(videoModel));

        var videoAtualizado = videoCrudUseCase.atualizarVideo(Mono.just(videoRecord),id,videoRepository);

        verify(videoRepository, times(1)).findById(any(Integer.class));



    }


    @Test
    public void GerarExcecao_NoDeveDeletarVideo_QuandoIdNaoExiste() {

        Integer id = 1;
        assertThatThrownBy(() -> videoCrudUseCase.deletarVideo(id,videoRepository))
                .isInstanceOf(RuntimeException.class);


        // Assert
        verify(videoRepository, times(1)).findById(any(Integer.class));
    }


    @Test
    public void DeveDeletarVideo() {


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

        when(videoRepository.findById(any(Integer.class)))
                .thenReturn(Mono.just(videoModel));

        var deletado = videoCrudUseCase.deletarVideo(id,videoRepository);

        // Assert
        verify(videoRepository, times(1)).findById(any(Integer.class));
        verify(videoRepository, times(1)).deleteById(any(Integer.class));
    }



    @Test
    public void DevelistarVideos() {

        VideoModel videoModel1  = new VideoModel();
        VideoModel videoModel2  = new VideoModel();

        Integer page = 1;
        Integer size = 10;

        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataPublicacao"));

        videoModel1.setId(1);
        videoModel1.setTitulo("Rambo1");
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(VideosCategorias.GUERRA);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);


        videoModel2.setId(2);
        videoModel2.setTitulo("Uma linda Mulher");
        videoModel2.setDescricao("Filme Romance");
        videoModel2.setDataPublicacao(LocalDate.of(1999,12,01));
        videoModel2.setUrlVideo("http://filmes.com.br/lindamulher");
        videoModel2.setCategoria(VideosCategorias.ROMANCE);
        videoModel2.setFavorito(1000);
        videoModel2.setVisualizacoes(100001);

        List<VideoModel> videos = Arrays.asList(videoModel1,videoModel2);

        when(videoRepository.findAll())
                .thenReturn((Flux.fromIterable(videos)));

        var lista =  videoRepository.findAll();

        var lista2 = videoCrudUseCase.listarVideos(page,size,videoRepository);


        // Assert
        assertEquals(videos, lista.collectList().block());
        verify(videoRepository, times(1)).findAll();

    }



    @Test
    public  void  develistarVideosPorTitulo() {

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

        when(videoRepository.findByDataPublicacao(dataPulicacao))
                .thenReturn((Flux.fromIterable(videos)));

        var result = videoCrudUseCase.listarVideosPorData(dataPulicacao,videoRepository);


        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByDataPublicacao(any(LocalDate.class));

    }


    @Test
    public  void develistarVideosPorCategoria() {

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
        when(videoRepository.findByCategoria(categoria))
                .thenReturn((Flux.fromIterable(videos)));
        var result = videoCrudUseCase.listarVideosPorCategoria(categoria,videoRepository);

        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByCategoria(any(VideosCategorias.class));

    }



    @Test
    public void develistarVideosRecomendados() {

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

        List<VideoModel> videos = Arrays.asList(videoModel1);

        when(videoRepository.ListarVideosRecomendados(categoria,qtd))
                .thenReturn((Flux.fromIterable(videos)));

        var result = videoCrudUseCase.listarVideosRecomendados(categoria,videoRepository);


        // Assert

        //assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1))
                .ListarVideosRecomendados(any(VideosCategorias.class),any(Integer.class));

    }



    @Test
    public void deveBuscarEstatisticas() {

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
