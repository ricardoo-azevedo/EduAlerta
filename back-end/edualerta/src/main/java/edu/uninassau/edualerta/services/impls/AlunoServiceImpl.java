package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.AlunoCadastroDto;
import edu.uninassau.edualerta.dtos.AlunoResponseDto;
import edu.uninassau.edualerta.dtos.AlunoStatusDto;
import edu.uninassau.edualerta.dtos.AlunoUpdateDto;
import edu.uninassau.edualerta.models.Aluno;
import edu.uninassau.edualerta.repositorys.AlunoRepository;
import edu.uninassau.edualerta.services.interfaces.AlunoServiceInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlunoServiceImpl implements AlunoServiceInterface {

  private final AlunoRepository alunoRepository;
  private final UsuarioContext usuarioContext;

  @Override
  public AlunoResponseDto cadastrar(AlunoCadastroDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Aluno aluno = new Aluno(dto.getNome().trim(), dto.getDataNascimento());

    Aluno alunoSalvo = alunoRepository.save(aluno);

    return converterParaDto(alunoSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlunoResponseDto> listar(Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return alunoRepository.findAllByOrderByIdAsc()
        .stream()
        .map(this::converterParaDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public AlunoResponseDto buscarPorId(Long id, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    return converterParaDto(buscarAluno(id));
  }

  @Override
  public AlunoResponseDto atualizar(Long id, AlunoUpdateDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Aluno aluno = buscarAluno(id);

    aluno.setNome(dto.getNome().trim());
    aluno.setDataNascimento(dto.getDataNascimento());

    return converterParaDto(aluno);
  }

  @Override
  public AlunoResponseDto alterarStatus(Long id, AlunoStatusDto dto, Jwt jwt) {

    usuarioContext.getUsuario(jwt);

    Aluno aluno = buscarAluno(id);

    aluno.setAtivo(dto.getAtivo());

    return converterParaDto(aluno);
  }

  private Aluno buscarAluno(Long id) {

    return alunoRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Aluno não encontrado"));
  }

  private AlunoResponseDto converterParaDto(Aluno aluno) {

    return new AlunoResponseDto(aluno.getId(), aluno.getNome(),
        aluno.getDataNascimento(), aluno.isAtivo(),
        aluno.getCriadoEm());
  }
}
