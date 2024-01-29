package com.challenge.videos.usecase.impl;


import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.gateways.impl.VideoGateway;
import com.challenge.videos.gateways.IVideoGateway;
import com.challenge.videos.records.VideoRecord;
import java.time.LocalDate;

import com.challenge.videos.usecase.IVideoCrudUseCase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * API Rest para interface entre FRONT END e camada de CONTROLLER da Arquitetura Limpa .
 */

@Service
public class VideoCrudUseCase implements IVideoCrudUseCase {

  /**
   * Método de registrar os videos.
   */
  @Override
  public Mono<VideoRecord> registrarVideo(
          Mono<VideoRecord> videoRecord, VideoRepository videoRepository){

    IVideoGateway videoGateway = new VideoGateway(videoRepository);
    return videoGateway.registrarVideo(videoRecord);

  }


  /**
   * Método de atualizar os videos.
   */
  @Override
  public Mono<VideoRecord> atualizarVideo(
          Mono<VideoRecord> videoRecord, Integer id, VideoRepository videoRepository) {

    IVideoGateway videoGateway = new VideoGateway(videoRepository);

    var videoEncontrado = videoGateway.buscarVideoPorId(id);

    if (videoEncontrado != null) {
      return videoGateway.registrarVideo(videoRecord);
    } else {
      throw new RuntimeException("Não foi encontrado nenhum video com esse ID");
    }
  }
  /**
   * Método de deletar os videos.
   */

  @Override
  public Mono<Void> deletarVideo(Integer id, VideoRepository databaseClient) {
    IVideoGateway videoGateway = new VideoGateway(databaseClient);
    var videoEncontrado = videoGateway.buscarVideoPorId(id);
    if (videoEncontrado != null) {
      return videoGateway.deletarVideoPorId(id);
    } else {
      throw new RuntimeException(
              "Não foi encontrado na base um Video cadastrado com esse ID");
    }
  }

  /**
   * Método de deletar os videos.
   */

  @Override
  public Flux<VideoModel> listarVideos(int page, int size, VideoRepository databaseClient) {
    IVideoGateway videoGateway = new VideoGateway(databaseClient);

    PageRequest pageRequest =
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataPublicacao"));

    return videoGateway.listarVideos(pageRequest);
  }

  /**
   * Método de deletar os videos.
   */

  @Override
  public  Flux<VideoModel> listarVideosPorTitulo(String titulo, VideoRepository databaseClient) {
    IVideoGateway videoGateway = new VideoGateway(databaseClient);

    return videoGateway.listarVideosPorTitulo(titulo);
  }

  public  Flux<VideoModel> listarVideosPorData(LocalDate data, VideoRepository databaseClient) {
    IVideoGateway videoGateway = new VideoGateway(databaseClient);
    return videoGateway.listarVideosPorDataPublicacao(data);
  }

  public  Flux<VideoModel> listarVideosPorCategoria(VideosCategorias categoria,
                                                    VideoRepository databaseClient) {
    IVideoGateway videoGateway = new VideoGateway(databaseClient);
    return videoGateway.listarVideosPorCategoria(categoria);
  }

  /**
   * Método de listar os videos recomendados.
   */

  @Override
  public Flux<VideoModel> listarVideosRecomendados(VideosCategorias categoria,
                                                   VideoRepository databaseClient) {

    IVideoGateway videoGateway = new VideoGateway(databaseClient);
    Integer qtdFavoritados = 1;
    return videoGateway.listarVideosRecomendados(categoria, qtdFavoritados);
  }


  /**
   * Método de listar os videos recomendados.
   */

  @Override
  public Mono<VideoEstatisticasModel> buscarEstatisticas(VideoRepository databaseClient) {

    IVideoGateway videoGateway = new VideoGateway(databaseClient);

    return videoGateway.listarEstatisticas();

  }


}
