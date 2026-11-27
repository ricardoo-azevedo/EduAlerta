package edu.uninassau.edualerta.services.impls;

import edu.uninassau.edualerta.config.security.UsuarioContext;
import edu.uninassau.edualerta.dtos.*;
import edu.uninassau.edualerta.models.*;
import edu.uninassau.edualerta.repositorys.*;
import edu.uninassau.edualerta.services.interfaces.ResponsavelServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponsavelServiceImpl implements ResponsavelServiceInterface {
  private final ResponsavelRepository responsavelRepository;
  private final AlunoRepository alunoRepository;
  private final AlunoResponsavelRepository associacaoRepository;
  private final UsuarioContext usuarioContext;

  public ResponsavelResponseDto cadastrar(ResponsavelCadastroDto dto, Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    String email = dto.getEmail().trim().toLowerCase();
    if (responsavelRepository.existsByEmailIgnoreCase(email))
      throw new IllegalArgumentException(
          "Já existe um responsável cadastrado com esse e-mail");
    return converter(responsavelRepository.save(
        new Responsavel(dto.getNome().trim(), email, limpar(dto.getTelefone()),
            limpar(dto.getParentesco()))));
  }

  @Transactional(readOnly = true)
  public List<ResponsavelResponseDto> listar(Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    return responsavelRepository.findAllByOrderByNomeAsc()
        .stream()
        .map(this::converter)
        .toList();
  }

  public ResponsavelResponseDto atualizar(Long id, ResponsavelCadastroDto dto,
      Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    Responsavel r = buscarResponsavel(id);
    String email = dto.getEmail().trim().toLowerCase();
    responsavelRepository.findAll()
        .stream()
        .filter(
            x -> !x.getId().equals(id) && x.getEmail().equalsIgnoreCase(email))
        .findAny()
        .ifPresent(x -> {
          throw new IllegalArgumentException(
              "Já existe outro responsável com esse e-mail");
        });
    r.setNome(dto.getNome().trim());
    r.setEmail(email);
    r.setTelefone(limpar(dto.getTelefone()));
    r.setParentesco(limpar(dto.getParentesco()));
    return converter(r);
  }

  public void alterarStatus(Long id, boolean ativo, Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    buscarResponsavel(id).setAtivo(ativo);
  }

  @Transactional(readOnly = true)
  public List<AlunoResponsavelResponseDto> listarPorAluno(Long alunoId,
      Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    buscarAluno(alunoId);
    return associacaoRepository
        .findByAlunoIdOrderByResponsavelPrincipalDescResponsavelNomeAsc(alunoId)
        .stream()
        .map(this::converterAssociacao)
        .toList();
  }

  public AlunoResponsavelResponseDto associar(Long alunoId, AssociacaoResponsavelDto dto, Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    Aluno aluno = buscarAluno(alunoId);
    Responsavel responsavel = buscarResponsavel(dto.getResponsavelId());
    if (!responsavel.isAtivo())
      throw new IllegalArgumentException(
          "Não é possível associar um responsável inativo");
    AlunoResponsavel associacao = associacaoRepository
        .findByAlunoIdAndResponsavelId(alunoId, responsavel.getId())
        .orElse(null);
    if (associacao == null)
      associacao = new AlunoResponsavel(aluno, responsavel,
          dto.isResponsavelPrincipal());
    if (dto.isResponsavelPrincipal()) {
      associacaoRepository.findByAlunoIdAndResponsavelPrincipalTrue(alunoId)
          .forEach(a -> a.setResponsavelPrincipal(false));
      associacaoRepository.flush();
    }
    associacao.setResponsavelPrincipal(dto.isResponsavelPrincipal());
    return converterAssociacao(associacaoRepository.save(associacao));
  }

  public void desassociar(Long alunoId, Long responsavelId, Jwt jwt) {
    usuarioContext.getUsuario(jwt);
    AlunoResponsavel a = associacaoRepository
        .findByAlunoIdAndResponsavelId(alunoId, responsavelId)
        .orElseThrow(
            () -> new EntityNotFoundException("Associação não encontrada"));
    associacaoRepository.delete(a);
  }

  private Aluno buscarAluno(Long id) {
    return alunoRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Aluno não encontrado"));
  }

  private Responsavel buscarResponsavel(Long id) {
    return responsavelRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Responsável não encontrado"));
  }

  private String limpar(String s) {
    return s == null || s.isBlank() ? null : s.trim();
  }

  private ResponsavelResponseDto converter(Responsavel r) {
    return new ResponsavelResponseDto(r.getId(), r.getNome(), r.getEmail(),
        r.getTelefone(), r.getParentesco(),
        r.isAtivo());
  }

  private AlunoResponsavelResponseDto converterAssociacao(AlunoResponsavel a) {
    Responsavel r = a.getResponsavel();
    return new AlunoResponsavelResponseDto(
        a.getId(), a.getAluno().getId(), r.getId(), r.getNome(), r.getEmail(),
        r.getTelefone(), r.getParentesco(), a.isResponsavelPrincipal());
  }
}
