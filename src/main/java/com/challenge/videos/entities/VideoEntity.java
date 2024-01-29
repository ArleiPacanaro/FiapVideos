package com.challenge.videos.entities;

import com.challenge.videos.enumeration.VideosCategorias;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


/**
 * Componente Entity Video.
 */


@Getter
public class VideoEntity {


  private Integer id;
  private String titulo;
  private String descricao;
  private String urlVideo;
  private LocalDate dataPublicacao;
  private VideosCategorias categoria;
  private Integer favorito;
  private Integer visualizacoes;

  /**
   * Seguindo as convenções da Clean Architecture devemos ter  entity não acoplada a banco de dados.
   */

  public VideoEntity(Integer id, String titulo, String descricao,
                     String urlVideo, LocalDate dataPublicacao, VideosCategorias categoria,
                     Integer favorito, Integer visualizacoes) {

    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.urlVideo = urlVideo;
    this.dataPublicacao = dataPublicacao;
    this.categoria = categoria;
    this.favorito = favorito;
    this.visualizacoes = visualizacoes;

  }
}
