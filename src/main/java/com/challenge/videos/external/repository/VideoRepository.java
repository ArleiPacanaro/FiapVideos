package com.challenge.videos.external.repository;

import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Interface da Repositório de Videos com MONGODB Reativo.
 */

@Repository
public interface VideoRepository extends ReactiveMongoRepository<VideoModel, Integer> {

  Flux<VideoModel> findAllBy(Pageable pageable);

  Flux<VideoModel> findByTituloContainingIgnoreCase(String titulo);

  Flux<VideoModel> findByDataPublicacao(LocalDate dataDePublicacao);

  Flux<VideoModel> findByCategoria(VideosCategorias categorias);

  @Query("{ $and: [{'categoria':{$eq:?0}},{'favorito':{$gte:?1}}] }")
  Flux<VideoModel> ListarVideosRecomendados(VideosCategorias categorias, Integer qtdFavoritados);

  @Aggregation("{ $group : { _id : null ,qtdTotalVideosFavoritos : { $sum : $favorito }"
          + ", avgTotalVideosAssistidos : {$avg : $visualizacoes}  } }")
  Mono<VideoEstatisticasModel> listarEstatisticas();

}
