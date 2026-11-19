package edu.uninassau.edualerta.services.interfaces;

import edu.uninassau.edualerta.dtos.FrequenciaAcumuladaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaCadastroDto;
import edu.uninassau.edualerta.dtos.MatriculaEncerramentoDto;
import edu.uninassau.edualerta.dtos.MatriculaResponseDto;
import edu.uninassau.edualerta.dtos.MatriculaTransferenciaDto;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

public interface MatriculaServiceInterface {

  MatriculaResponseDto cadastrar(MatriculaCadastroDto dto, Jwt jwt);

  List<MatriculaResponseDto> listar(Jwt jwt);

  MatriculaResponseDto buscarPorId(Long id, Jwt jwt);

  List<MatriculaResponseDto> listarPorAluno(Long alunoId, Jwt jwt);

  FrequenciaAcumuladaResponseDto calcularFrequenciaAcumulada(Long id, Jwt jwt);

  MatriculaResponseDto encerrar(Long id, MatriculaEncerramentoDto dto, Jwt jwt);

  MatriculaResponseDto transferir(Long id, MatriculaTransferenciaDto dto,
                                  Jwt jwt);
}
