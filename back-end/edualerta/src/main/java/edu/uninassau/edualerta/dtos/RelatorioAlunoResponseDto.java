package edu.uninassau.edualerta.dtos;

import edu.uninassau.edualerta.models.NivelAlerta;
import edu.uninassau.edualerta.models.SituacaoAlerta;
import edu.uninassau.edualerta.models.StatusEncaminhamento;
import edu.uninassau.edualerta.models.StatusMatricula;
import edu.uninassau.edualerta.models.StatusNotificacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioAlunoResponseDto {

  private AlunoResumo aluno;
  private LocalDateTime geradoEm;
  private Resumo resumo;
  private List<MatriculaRelatorio> matriculas;
  private List<MudancaTurma> mudancasTurma;
  private List<ResponsavelRelatorio> responsaveis;
  private List<AlertaRelatorio> alertas;

  @Getter
  @AllArgsConstructor
  public static class AlunoResumo {

    private String nome;
    private boolean ativo;
  }

  @Getter
  @AllArgsConstructor
  public static class Resumo {

    private int quantidadeMatriculas;
    private int quantidadeMudancasTurma;
    private int quantidadeFechamentos;
    private long aulasRealizadas;
    private long faltasNaoJustificadas;
    private long faltasJustificadas;
    private BigDecimal percentualFaltasNaoJustificadas;
    private int quantidadeAlertas;
    private int quantidadeIntervencoes;
    private int quantidadeEncaminhamentos;
    private int notificacoesEnviadas;
    private int notificacoesComFalha;
  }

  @Getter
  @AllArgsConstructor
  public static class MatriculaRelatorio {

    private String turma;
    private Integer anoLetivo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusMatricula status;
    private List<String> periodosLetivos;
    private List<FechamentoRelatorio> fechamentos;
    private long aulasRealizadas;
    private long faltasNaoJustificadas;
    private long faltasJustificadas;
    private BigDecimal percentualFaltasNaoJustificadas;
  }

  @Getter
  @AllArgsConstructor
  public static class FechamentoRelatorio {

    private String periodoLetivo;
    private LocalDate dataInicioSemana;
    private LocalDate dataFimSemana;
    private long aulasRealizadas;
    private long faltasNaoJustificadas;
    private long faltasJustificadas;
    private BigDecimal percentualFaltasNaoJustificadas;
  }

  @Getter
  @AllArgsConstructor
  public static class MudancaTurma {

    private String turmaOrigem;
    private String turmaDestino;
    private LocalDate ultimoDiaNaTurmaAnterior;
    private LocalDate primeiroDiaNaTurmaNova;
    private String observacao;
  }

  @Getter
  @AllArgsConstructor
  public static class ResponsavelRelatorio {

    private String nome;
    private String parentesco;
    private boolean principal;
    private boolean ativo;
  }

  @Getter
  @AllArgsConstructor
  public static class AlertaRelatorio {

    private String periodoLetivo;
    private BigDecimal percentualFaltas;
    private NivelAlerta nivel;
    private SituacaoAlerta situacao;
    private String motivo;
    private LocalDateTime atualizadoEm;
    private List<NotificacaoRelatorio> notificacoes;
    private List<AcompanhamentoRelatorio> acompanhamentos;
    private List<EncaminhamentoRelatorio> encaminhamentos;
  }

  @Getter
  @AllArgsConstructor
  public static class NotificacaoRelatorio {

    private String responsavel;
    private StatusNotificacao status;
    private LocalDateTime registroEm;
    private LocalDateTime envioConcluidoEm;
    private String observacao;
  }

  @Getter
  @AllArgsConstructor
  public static class AcompanhamentoRelatorio {

    private LocalDateTime dataHora;
    private String tipoIntervencao;
    private String descricao;
    private String resultado;
    private String registradoPor;
  }

  @Getter
  @AllArgsConstructor
  public static class EncaminhamentoRelatorio {

    private String instituicaoDestino;
    private String finalidade;
    private String descricao;
    private StatusEncaminhamento situacao;
    private LocalDate dataEncaminhamento;
  }
}
