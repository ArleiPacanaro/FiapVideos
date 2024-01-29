package com.challenge.videos.records;

import com.challenge.videos.enumeration.VideosCategorias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;


/**
 * Adapter\Port para saida.
 */

public record VideoRecord(

        @NotNull(message = "ID deve ser preenchido ")
        Integer id,
        @NotNull(message = "titulo deve ser preenchido ")
        String titulo,
        @NotNull(message = "descricao deve ser preenchida ")
        String descricao,
        @NotNull(message = "URL deve ser preenchida ")
        @Pattern(
                regexp =
                       "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                message = "[urlVideo] Contém caractere inválido")
        String urlVideo,
        @NotNull(message = "data deve ser preenchida ")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPublicacao,

        @NotNull(message = "Categoria deve ser preenchida ")
        @Enumerated(EnumType.STRING)
        VideosCategorias categoria,
        Integer favorito,
        Integer visualizacoes

) {

}
