package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.TurmaCadastroDto;
import edu.uninassau.edualerta.dtos.TurmaResponseDto;
import edu.uninassau.edualerta.dtos.TurmaStatusDto;
import edu.uninassau.edualerta.dtos.TurmaUpdateDto;
import edu.uninassau.edualerta.models.Turma;
import edu.uninassau.edualerta.repositorys.TurmaRepository;
import edu.uninassau.edualerta.services.interfaces.TurmaServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TurmaServiceImpl implements TurmaServiceInterface {

  private final TurmaRepository turmaRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public TurmaResponseDto cadastrar(TurmaCadastroDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Turma turma = new Turma(dto.getNome().trim(), dto.getAnoLetivo());

    Turma turmaSalva = turmaRepository.save(turma);

    return converterParaDto(turmaSalva);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TurmaResponseDto> listar(Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return turmaRepository.findAllByOrderByIdAsc()
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public TurmaResponseDto buscarPorId(Long id, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return converterParaDto(buscarTurma(id));
  }

  @Override
  public TurmaResponseDto atualizar(Long id, TurmaUpdateDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Turma turma = buscarTurma(id);

    turma.setNome(dto.getNome().trim());
    turma.setAnoLetivo(dto.getAnoLetivo());

    return converterParaDto(turma);
  }

  @Override
  public TurmaResponseDto alterarStatus(Long id, TurmaStatusDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Turma turma = buscarTurma(id);

    turma.setAtivo(dto.getAtivo());

    return converterParaDto(turma);
  }

  private Turma buscarTurma(Long id) {

    return turmaRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Turma não encontrada"));
  }

  private TurmaResponseDto converterParaDto(Turma turma) {

    return new TurmaResponseDto(turma.getId(), turma.getNome(),
                                turma.getAnoLetivo(), turma.isAtivo());
  }
}
