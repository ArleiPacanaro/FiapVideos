package com.challenge.videos.usecase;

import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface IVideoCrudUseCase {

    /**
     * Método de Método de registrar videos.
     */
    public Mono<VideoRecord> registrarVideo(
            Mono<VideoRecord> videoRecord, VideoRepository videoRepository);

    /**
     * Método de Método de atualizar videos.
     */
    public Mono<VideoRecord> atualizarVideo(
            Mono<VideoRecord> videoRecord, Integer id, VideoRepository videoRepository);

    /**
     * Método de Método de deletar videos.
     */
    public Mono<Void> deletarVideo(Integer id, VideoRepository databaseClient);

    /**
     * Método de Método de listar os videos.
     */
    public Flux<VideoModel> listarVideos(int page, int size, VideoRepository videoRepository);

    /**
     * Método de Método de listar os videos por titulo.
     */
    public  Flux<VideoModel> listarVideosPorTitulo(String titulo, VideoRepository videoRepository);

    /**
     * Método de Método de listar os videos por data.
     */
    public  Flux<VideoModel> listarVideosPorData(LocalDate data, VideoRepository videoRepository);

    /**
     *  Método de listar os videos por categoria.
     */
    public  Flux<VideoModel> listarVideosPorCategoria(VideosCategorias categoria,
                                                      VideoRepository videoRepository);

    /**
     * Método de listar os videos recomendados.
     */
    public Flux<VideoModel> listarVideosRecomendados(VideosCategorias categoria,
                                                     VideoRepository videoRepository);


    /**
     * Método de listar os videos recomendados.
     */
    public Mono<VideoEstatisticasModel> buscarEstatisticas(VideoRepository videoRepository);


}
