package com.challenge.videos.external.api;


import com.challenge.videos.usecase.impl.VideoCrudUseCase;
import com.challenge.videos.enumeration.VideosCategorias;
import com.challenge.videos.external.model.VideoEstatisticasModel;
import com.challenge.videos.external.model.VideoModel;
import com.challenge.videos.external.repository.VideoRepository;
import com.challenge.videos.records.VideoRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * API Rest para interface entre FRONT END e camada de CONTROLLER da Arquitetura Limpa .
 */

@RestController
@RequestMapping("/videos")
public class VideoRestController {

  @Autowired
  private VideoCrudUseCase videoCrudUseCase;

  @Autowired
  private VideoRepository videoRepository;

  @Operation(summary = "Cadastrar Video")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Video Cadastrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<VideoRecord> registrarVideo(@Valid @RequestBody Mono<VideoRecord> videoRecord) {
    return videoCrudUseCase.registrarVideo(videoRecord,videoRepository);

  }


  @Operation(summary = "Atualizar Video")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Video editado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @PutMapping("/{id}")
    public Mono<VideoRecord> atualizarVideo(@PathVariable Integer id,@Valid @RequestBody Mono<VideoRecord> videoRecord) {
     return videoCrudUseCase.atualizarVideo(videoRecord,id,videoRepository);
  }

  @Operation(summary = "Deletar Video")
  @DeleteMapping("/{id}")
  public Mono<Void> deletarVideo(@PathVariable Integer id) throws  IllegalAccessException {
    return videoCrudUseCase.deletarVideo(id, videoRepository);
  }

  /**
   * Método de Listar de forma paginada os videos.
   */

  @Operation(summary = "Listar todos os videos")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @GetMapping("/listar")
  public Mono<Page<VideoModel>> listarVideos(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {


    return  videoCrudUseCase.listarVideos(page, size, videoRepository);

  }

  @Operation(summary = "Listar videos por Titulo")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @GetMapping("/listar/titulo")
  public Flux<VideoModel> listarVideosPorTitulo(@RequestParam String titulo) {
    return videoCrudUseCase.listarVideosPorTitulo(titulo, videoRepository);
  }

  @Operation(summary = "Listar videos por data de publicacao")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @GetMapping("/listar/dataPublicacao")
  public Flux<VideoModel> listarVideosPorData(@RequestParam("data")
                       @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
    return videoCrudUseCase.listarVideosPorData(data, videoRepository);
  }

  @Operation(summary = "Listar videos por categoria")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de videos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VideoRecord.class)) }),
    @ApiResponse(responseCode = "400", description = "Requisição Inválida",
                    content = @Content)})
  @GetMapping("/listar/categoria")
  public Flux<VideoModel> listarVideosPorCategoria(@RequestParam VideosCategorias categoria) {
    return videoCrudUseCase.listarVideosPorCategoria(categoria, videoRepository);
  }

  @Operation(summary = "Listar videos recomendados por categoria")
  @GetMapping("/recomendados")
  public Flux<VideoModel> listarVideosRecomendados(@RequestParam VideosCategorias categoria) {
    return videoCrudUseCase.listarVideosRecomendados(categoria, videoRepository);
  }

  @Operation(summary = "Apresentar Estatisticas")
  @GetMapping("/estatisticas")
  public Mono<VideoEstatisticasModel> buscarEstatistica() {
    return videoCrudUseCase.buscarEstatisticas(videoRepository);
  }

}
