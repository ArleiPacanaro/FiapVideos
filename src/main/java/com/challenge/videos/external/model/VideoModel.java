package com.challenge.videos.external.model;

import com.challenge.videos.enumeration.VideosCategorias;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Classe para Mapper das Repositório de Videos.
 */

@Document(collection = "videos")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VideoModel {

  @Id
  private Integer id;
  private String titulo;
  private String descricao;
  private String urlVideo;
  private LocalDate dataPublicacao;
  private VideosCategorias categoria;
  private Integer favorito;
  private Integer visualizacoes;

}
