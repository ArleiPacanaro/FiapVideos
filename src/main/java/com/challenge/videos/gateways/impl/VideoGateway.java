package com.challenge.videos.gateways.impl;

import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;

import java.time.LocalDate;

import com.challenge.videos.gateways.IVideoGateway;
import com.challenge.videos.mappers.VideoMapper;
import com.challenge.videos.records.VideoRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Gateway do padrão da arquitetura limpa.
 */


public class VideoGateway implements IVideoGateway {

  private final VideoRepository videoRepository;

  public VideoGateway(VideoRepository videoRepository) {

    this.videoRepository = videoRepository;
  }



  @Override
  public Mono<VideoRecord> registrarVideo(Mono<VideoRecord> videoRecord) {

    return (
            videoRecord
                    .map(VideoMapper::toModelByDTO)
                    .flatMap(videoRepository::save)
                    .map(VideoMapper::toDTOByModel));

  }

  @Override
  public Mono<VideoModel> buscarVideoPorId(Integer id) {
    return this.videoRepository.findById(id);
  }

  @Override
  public Mono<Void> deletarVideoPorId(Integer id) {

    return this.videoRepository.deleteById(id);
  }

  @Override
  public Mono<Page<VideoModel>> listarVideos(Pageable pageable) {

    var videoModelFlux = this.videoRepository.findAllBy(pageable);

    return  videoModelFlux.collectList()
            .zipWith(videoRepository.findAllBy(pageable).count())
            .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));


  }

  @Override
  public Flux<VideoModel> listarVideosPorTitulo(String titulo) {
    return this.videoRepository.findByTituloContainingIgnoreCase(titulo);
  }

  @Override
  public Flux<VideoModel> listarVideosPorDataPublicacao(LocalDate dataPublicacao) {
    return this.videoRepository.findByDataPublicacao(dataPublicacao);
  }

  @Override
  public Flux<VideoModel> listarVideosPorCategoria(VideosCategorias categorias) {
    return this.videoRepository.findByCategoria(categorias);
  }

  @Override
  public Flux<VideoModel> listarVideosRecomendados(VideosCategorias categoria,
                                                   Integer qtdFavoritados) {
    return this.videoRepository.ListarVideosRecomendados(categoria, qtdFavoritados);
  }

  @Override
  public Mono<VideoEstatisticasModel> listarEstatisticas() {
    return  this.videoRepository.listarEstatisticas();
  }

}
