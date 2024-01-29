package com.challenge.videos.mappers;

import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.records.VideoRecord;

public class VideoMapper {

    public static VideoModel toModelByDTO(VideoRecord dto) {

        VideoModel videoModel = new VideoModel();

        videoModel.setId(dto.id());
        videoModel.setTitulo(dto.titulo());
        videoModel.setDescricao(dto.descricao());
        videoModel.setUrlVideo(dto.urlVideo());
        videoModel.setDataPublicacao(dto.dataPublicacao());
        videoModel.setCategoria(dto.categoria());
        videoModel.setFavorito(dto.favorito());
        videoModel.setVisualizacoes(dto.visualizacoes());

        return videoModel;

    }

    public static VideoRecord toDTOByModel(VideoModel entity)
    {
        VideoRecord videoDTO = new VideoRecord
                (
                    entity.getId(),
                    entity.getTitulo(),
                    entity.getDescricao(),
                    entity.getUrlVideo(),
                    entity.getDataPublicacao(),
                    entity.getCategoria(),
                    entity.getFavorito(),
                    entity.getVisualizacoes()
        );

        return videoDTO;

    }




}
