package com.challenge.videos.external.respository;


import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoRespositoryTest {

    @Mock
    private VideoRepository videoRepository;

    AutoCloseable openMocks;

    /// Antes de cada execução, cria um novo mock , fake, ou seja inicia todas as variaves que tem a anotação Mock
    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    // Após cada execução...
    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }


    @Test
    void deveRegistrarVideo() {

    // Triple A
    // Arrange

        VideoModel videoModel1  = new VideoModel();

        videoModel1.setId(1);
        videoModel1.setTitulo("Rambo1");
        videoModel1.setDescricao("Filme de Guerra Rambo1");
        videoModel1.setDataPublicacao(LocalDate.of(1985,10,01));
        videoModel1.setUrlVideo("http://filmes.com.br/rambo1");
        videoModel1.setCategoria(VideosCategorias.GUERRA);
        videoModel1.setFavorito(100);
        videoModel1.setVisualizacoes(10000);

        Mono<VideoModel> videlModel1Mono = Mono.just(videoModel1);
        when(videoRepository.save(any(VideoModel.class))).thenReturn(videlModel1Mono);

        //Act -- executa
        var videoArmazenado1 = videoRepository.save(videoModel1);

        // Assert

        videoArmazenado1.subscribe(mono -> {
            assertThat(mono.getId()).isNotNull().isEqualTo(videoModel1.getId());
        });

        verify(videoRepository, times(1)).save(any(VideoModel.class));

    }

    @Test
    void deveListarTodosVideos() {

        VideoModel videoModel1  = new VideoModel();
        VideoModel videoModel2  = new VideoModel();

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

        Mono<VideoModel> videlModel1Mono = Mono.just(videoModel1);
        when(videoRepository.save(any(VideoModel.class))).thenReturn(videlModel1Mono);
        var videoArmazenado1 = videoRepository.save(videoModel1);

        Mono<VideoModel> videlModel2Mono = Mono.just(videoModel2);
        when(videoRepository.save(any(VideoModel.class))).thenReturn(videlModel2Mono);
        var videoArmazenado2 = videoRepository.save(videoModel2);

        when(videoRepository.findAll())
                .thenReturn((Flux.fromIterable(videos)));

        var lista = videoRepository.findAll();

        // Assert
        assertEquals(videos, lista.collectList().block());
        verify(videoRepository, times(2)).save(any(VideoModel.class));
        verify(videoRepository, times(1)).findAll();



    }

    @Test
    void deveListarVideoPorTitulo() {

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

        var result = videoRepository.findByTituloContainingIgnoreCase(nomeFilme);


        // Assert

       assertEquals(videos, result.collectList().block());
       verify(videoRepository, times(1)).findByTituloContainingIgnoreCase(any(String.class));

        }

    @Test
    void findByDataPublicacao() {


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

        var result = videoRepository.findByDataPublicacao(dataPulicacao);


        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByDataPublicacao(any(LocalDate.class));


    }

    @Test
    void findByCategoria() {

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
        var result = videoRepository.findByCategoria(categoria);

        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1)).findByCategoria(any(VideosCategorias.class));

    }

    @Test
    void ListarVideosRecomendados() {

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

        var result = videoRepository.ListarVideosRecomendados(categoria,qtd);


        // Assert

        assertEquals(videos, result.collectList().block());
        verify(videoRepository, times(1))
                .ListarVideosRecomendados(any(VideosCategorias.class),any(Integer.class));

    }

    @Test
    void listarEstatisticas() {


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

        var result = videoRepository.listarEstatisticas();
        // Assert

        result.subscribe(mono -> {
            assertThat(mono.getQtdTotalVideosFavoritos()).isNotNull()
                    .isEqualTo(videoEstatisticasModel.getQtdTotalVideosFavoritos());
        });

        verify(videoRepository, times(1))
                .listarEstatisticas();


    }

    @Test
    void devePermitirRemoverMensagem(){

        // Arrange
        Integer id = 1;

        // metodo do Mockito....

        when(videoRepository.deleteById(any(Integer.class)))
                .thenReturn(Mono.empty());

        // Act

        videoRepository.deleteById(id);

         //Assert

        verify(videoRepository,times(1)).deleteById(any(Integer.class));


    }


}
