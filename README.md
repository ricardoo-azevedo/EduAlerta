# EduAlerta

Sistema web de monitoramento de frequência escolar e identificação de risco de evasão.

O EduAlerta tem como objetivo auxiliar instituições de ensino no acompanhamento da frequência dos alunos, permitindo identificar situações de risco e registrar intervenções para prevenir a evasão escolar.

## Funcionalidades

- Cadastro de alunos e turmas.
- Gerenciamento de matrículas.
- Registro semanal de frequência.
- Cálculo do percentual de faltas por semestre.
- Geração automática de alertas.
- Registro de intervenções realizadas pela instituição.

## Tecnologias

**Backend:** Java, Spring Boot, Spring Data JPA e Spring Security.

**Frontend:** HTML, CSS, Bootstrap e JavaScript.

**Banco de dados:** PostgreSQL (Supabase).

**Autenticação:** Supabase Auth e JWT.

## Modelagem do banco de dados

A frequência é registrada por meio de fechamentos semanais por turma, sem a necessidade de cadastrar cada aula individualmente.

```mermaid
erDiagram
    PERIODO_LETIVO {
        bigint id PK
        string nome
        int ano_letivo
        int numero_semestre
        date data_inicio
        date data_fim
        boolean ativo
    }

    TURMA {
        bigint id PK
        string nome
        int ano_letivo
        boolean ativo
    }

    ALUNO {
        bigint id PK
        string nome
        date data_nascimento
        boolean ativo
        datetime criado_em
    }

    MATRICULA {
        bigint id PK
        bigint aluno_id FK
        bigint turma_id FK
        date data_inicio
        date data_fim
        string status
    }

    USUARIO {
        bigint id PK
        string auth_user_id UK
        string nome
        string email
        string perfil
        boolean ativo
    }

    FECHAMENTO_SEMANAL {
        bigint id PK
        bigint turma_id FK
        bigint periodo_letivo_id FK
        bigint usuario_id FK
        date data_inicio_semana
        date data_fim_semana
        string status
        datetime criado_em
        datetime atualizado_em
    }

    FECHAMENTO_DIA {
        bigint id PK
        bigint fechamento_semanal_id FK
        date data
        int quantidade_aulas
    }

    FREQUENCIA_SEMANAL {
        bigint id PK
        bigint fechamento_semanal_id FK
        bigint matricula_id FK
        int faltas
        int faltas_justificadas
    }

    RESPONSAVEL {
        bigint id PK
        string nome
        string telefone
        string email
    }

    ALUNO_RESPONSAVEL {
        bigint aluno_id PK, FK
        bigint responsavel_id PK, FK
        string parentesco
        boolean principal
    }

    ALERTA {
        bigint id PK
        bigint aluno_id FK
        bigint periodo_letivo_id FK
        string motivo
        string nivel_risco
        decimal percentual_faltas
        string status
        datetime gerado_em
        datetime atualizado_em
        datetime encerrado_em
    }

    ACOMPANHAMENTO {
        bigint id PK
        bigint alerta_id FK
        bigint usuario_id FK
        datetime data_hora
        string tipo_intervencao
        string descricao
        string resultado
    }

    NOTIFICACAO {
        bigint id PK
        bigint alerta_id FK
        bigint responsavel_id FK
        string canal
        string status
        datetime enviada_em
        datetime entregue_em
    }

    ENCAMINHAMENTO {
        bigint id PK
        bigint alerta_id FK
        string servico_destino
        string programa
        string status
        datetime encaminhado_em
        string observacao
    }

    ALUNO ||--o{ MATRICULA : possui
    TURMA ||--o{ MATRICULA : recebe
    PERIODO_LETIVO ||--o{ FECHAMENTO_SEMANAL : organiza
    TURMA ||--o{ FECHAMENTO_SEMANAL : possui
    USUARIO ||--o{ FECHAMENTO_SEMANAL : registra
    FECHAMENTO_SEMANAL ||--|{ FECHAMENTO_DIA : detalha
    FECHAMENTO_SEMANAL ||--o{ FREQUENCIA_SEMANAL : contem
    MATRICULA ||--o{ FREQUENCIA_SEMANAL : recebe
    ALUNO ||--o{ ALUNO_RESPONSAVEL : possui
    RESPONSAVEL ||--o{ ALUNO_RESPONSAVEL : associado
    ALUNO ||--o{ ALERTA : recebe
    PERIODO_LETIVO ||--o{ ALERTA : referencia
    ALERTA ||--o{ ACOMPANHAMENTO : possui
    USUARIO ||--o{ ACOMPANHAMENTO : registra
    ALERTA ||--o{ NOTIFICACAO : gera
    RESPONSAVEL ||--o{ NOTIFICACAO : recebe
    ALERTA ||--o{ ENCAMINHAMENTO : pode_gerar
```

## Status

Projeto em desenvolvimento.

