package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.PeriodoLetivoCadastroDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoResponseDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoStatusDto;
import edu.uninassau.edualerta.dtos.PeriodoLetivoUpdateDto;
import edu.uninassau.edualerta.models.PeriodoLetivo;
import edu.uninassau.edualerta.repositorys.PeriodoLetivoRepository;
import edu.uninassau.edualerta.services.interfaces.PeriodoLetivoServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PeriodoLetivoServiceImpl implements PeriodoLetivoServiceInterface {

  private final PeriodoLetivoRepository periodoLetivoRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public PeriodoLetivoResponseDto cadastrar(PeriodoLetivoCadastroDto dto,
                                            Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    validarDatas(dto.getDataInicio(), dto.getDataFim());

    PeriodoLetivo periodo = new PeriodoLetivo(
        dto.getNome().trim(), dto.getAnoLetivo(), dto.getNumeroSemestre(),
        dto.getDataInicio(), dto.getDataFim());

    PeriodoLetivo periodoSalvo = periodoLetivoRepository.save(periodo);

    return converterParaDto(periodoSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PeriodoLetivoResponseDto> listar(Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return periodoLetivoRepository
        .findAllByOrderByAnoLetivoDescNumeroSemestreDesc()
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PeriodoLetivoResponseDto buscarPorId(Long id, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return converterParaDto(buscarPeriodo(id));
  }

  @Override
  public PeriodoLetivoResponseDto atualizar(Long id, PeriodoLetivoUpdateDto dto,
                                            Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    validarDatas(dto.getDataInicio(), dto.getDataFim());

    PeriodoLetivo periodo = buscarPeriodo(id);

    periodo.setNome(dto.getNome().trim());
    periodo.setAnoLetivo(dto.getAnoLetivo());
    periodo.setNumeroSemestre(dto.getNumeroSemestre());
    periodo.setDataInicio(dto.getDataInicio());
    periodo.setDataFim(dto.getDataFim());

    return converterParaDto(periodo);
  }

  @Override
  public PeriodoLetivoResponseDto
  alterarStatus(Long id, PeriodoLetivoStatusDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    PeriodoLetivo periodo = buscarPeriodo(id);

    periodo.setAtivo(dto.getAtivo());

    return converterParaDto(periodo);
  }

  private PeriodoLetivo buscarPeriodo(Long id) {

    return periodoLetivoRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Período letivo não encontrado"));
  }

  private void validarDatas(java.time.LocalDate dataInicio,
                            java.time.LocalDate dataFim) {

    if (dataFim.isBefore(dataInicio)) {
      throw new IllegalArgumentException(
          "A data de término não pode ser anterior à data de início");
    }
  }

  private PeriodoLetivoResponseDto converterParaDto(PeriodoLetivo periodo) {

    return new PeriodoLetivoResponseDto(
        periodo.getId(), periodo.getNome(), periodo.getAnoLetivo(),
        periodo.getNumeroSemestre(), periodo.getDataInicio(),
        periodo.getDataFim(), periodo.isAtivo());
  }
}
