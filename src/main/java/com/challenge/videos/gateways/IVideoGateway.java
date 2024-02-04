package com.challenge.videos.gateways;


import com.challenge.videos.entities.VideoEntity;
import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import java.time.LocalDate;

import com.challenge.videos.records.VideoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface Adapter\Port para implementar o Gateway, L do Solid.
 */


public interface IVideoGateway {

  public Mono<VideoRecord> registrarVideo(Mono<VideoRecord> videoRecord);

  public Mono<VideoModel> buscarVideoPorId(Integer id);

  public Mono<Void> deletarVideoPorId(Integer id);

  public Mono<Page<VideoModel>> listarVideos(Pageable pageable);

  public Flux<VideoModel> listarVideosPorTitulo(String titulo);

  public Flux<VideoModel> listarVideosPorDataPublicacao(LocalDate dataDePublicacao);

  public Flux<VideoModel> listarVideosPorCategoria(VideosCategorias categorias);

  Flux<VideoModel> listarVideosRecomendados(VideosCategorias categoria, Integer qtdFavoritados);

  Mono<VideoEstatisticasModel> listarEstatisticas();
}
