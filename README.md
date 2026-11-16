
# 🎓 EduAlerta

**Sistema de monitoramento de frequência escolar e identificação de risco de evasão.**

O EduAlerta é uma aplicação web voltada ao acompanhamento da frequência dos alunos e à identificação de possíveis situações de risco de evasão escolar. A proposta é permitir que a instituição mantenha os dados escolares organizados, consolide os registros de frequência e, nas próximas etapas, acompanhe os indicadores de risco e as intervenções realizadas.

> 🚧 **Status:** Em desenvolvimento. O backend possui módulos funcionais de autenticação/autorização, gerenciamento de usuários, alunos, turmas, períodos letivos e matrículas. O frontend já está integrado a esses módulos, mas ainda possui telas de frequência, painel, alertas, acompanhamentos e relatórios sem integração com a API. Os módulos de frequência, responsáveis, alertas e acompanhamentos ainda não possuem endpoints implementados no backend enviado.

---

## Integrantes do Projeto:
- José Ricardo
- Francileidy Conceição
- Adrielly Kauany
- Itamar Kauan
- Pedro Cauã Vicente
- Miguel Azevedo

---

## 📌 Estado atual do desenvolvimento

### Implementado no backend

- Integração com Supabase Auth para autenticação por JWT.
- Validação dos tokens pelo Spring Security como OAuth2 Resource Server.
- Associação do usuário autenticado ao registro local da tabela `tb_usuario`.
- Bloqueio de contas inativas e de usuários que ainda precisam trocar a senha temporária.
- Cadastro administrativo de usuários com perfil `SECRETARIA`, geração de senha temporária e envio por e-mail.
- Consulta, edição de nome, desativação e reativação de usuários conforme as regras de perfil.
- CRUD de alunos, incluindo alteração de status.
- CRUD de turmas, incluindo alteração de status e validação do ano letivo.
- CRUD de períodos letivos, incluindo alteração de status e validação das datas.
- Gerenciamento de matrículas, incluindo cadastro, consulta, encerramento e transferência entre turmas.
- Tratamento centralizado de erros de validação, regras de negócio, acesso negado e recursos não encontrados.

### Implementado ou integrado no frontend

- Tela de login integrada ao Supabase Auth, com validação posterior do usuário pela API.
- Proteção das páginas internas por sessão, perfil permitido e situação da conta.
- Redirecionamento para troca obrigatória de senha no primeiro acesso.
- Consulta do usuário autenticado por `GET /usuarios/me`.
- Gestão de usuários integrada à API: listagem, cadastro de secretarias por ADM, alteração de nome e ativação/desativação de contas.
- Gestão de alunos integrada à API: cadastro, listagem, pesquisa local, edição e alteração de status.
- Gestão de turmas integrada à API: cadastro, listagem, pesquisa local, edição e alteração de status.
- Gestão de matrículas integrada à API: cadastro, listagem, pesquisa local, encerramento e transferência entre turmas.
- Gestão de períodos letivos integrada à API: cadastro, listagem, edição e alteração de status.
- Layout compartilhado com identificação do usuário, perfil, navegação entre páginas e encerramento da sessão.

### Ainda não implementado no backend enviado

- Cadastro e associação de responsáveis.
- Fechamento semanal e diário de frequência.
- Registro de faltas e faltas justificadas.
- Cálculo de percentual de faltas.
- Geração e consulta de alertas.
- Registro de acompanhamentos/intervenções.
- Notificações e encaminhamentos.
- Endpoints de dashboard ou relatórios.

A modelagem e o escopo funcional descritos nas seções futuras representam o direcionamento do projeto. Eles não devem ser interpretados como funcionalidades já disponíveis na API.

---

## 🎯 Objetivos do projeto

- Gerenciar alunos, turmas e matrículas.
- Preservar o histórico dos vínculos dos alunos com as turmas.
- Registrar e consolidar a frequência escolar semanalmente.
- Diferenciar faltas não justificadas de faltas justificadas.
- Acompanhar o percentual de faltas não justificadas por período letivo.
- Identificar alunos com frequência irregular.
- Gerar alertas preventivos e críticos.
- Registrar intervenções realizadas pela instituição.
- Apoiar a secretaria na identificação antecipada de possíveis situações de evasão escolar.

---

## ⚙️ Funcionalidades e situação

| Funcionalidade | Situação no backend | Descrição |
|---|---|---|
| Autenticação | Implementada | Supabase Auth e validação de JWT pelo Spring Security. |
| Gestão de usuários | Implementada | Cadastro de secretarias, consulta, alteração de nome e alteração de status. |
| Gestão de alunos | Implementada | Cadastro, listagem, consulta por ID, edição e ativação/desativação. |
| Gestão de turmas | Implementada | Cadastro, listagem, consulta por ID, edição e ativação/desativação. |
| Períodos letivos | Implementada | Cadastro, listagem, consulta por ID, edição e ativação/desativação. |
| Matrículas | Implementada | Cadastro, listagem, consulta, consulta por aluno, encerramento e transferência. |
| Responsáveis | Pendente | Cadastro e associação de responsáveis aos alunos. |
| Fechamento semanal | Pendente | Consolidação das informações de frequência de uma turma por semana. |
| Frequência semanal | Pendente | Registro de faltas e faltas justificadas por matrícula. |
| Monitoramento | Pendente | Cálculo do percentual acumulado de faltas não justificadas. |
| Alertas | Pendente | Identificação e acompanhamento de situações de risco. |
| Acompanhamentos | Pendente | Registro das intervenções realizadas pela instituição. |
| Notificações | Pendente | Registro de notificações relacionadas aos alertas. |

### Situação das telas no frontend

| Tela | Situação no frontend | Observação |
|---|---|---|
| Login (`index.html`) | Integrada | Autentica pelo Supabase Auth e consulta `/usuarios/me` para validar o acesso. |
| Alterar senha (`alterar-senha.html`) | Integrada | Envia a nova senha para `PUT /usuarios/me/senha`. |
| Usuários (`usuarios.html`) | Integrada | Lista usuários; ADM pode cadastrar secretarias, alterar nome e ativar/desativar contas. |
| Alunos (`alunos.html`) | Integrada | CRUD e alteração de status por meio de `/alunos`. |
| Turmas (`turmas.html`) | Integrada | CRUD e alteração de status por meio de `/turmas`. |
| Matrículas e períodos (`matriculas.html`) | Integrada | Cadastro, consulta, encerramento e transferência de matrículas; CRUD e status de períodos letivos. |
| Painel (`painel.html`) | Apenas estrutura visual | Os indicadores exibem estado de espera; não consultam dados da API. |
| Frequência (`frequencias.html`) | Apenas estrutura visual | Apresenta os conceitos de rascunho/fechado, mas não registra fechamentos nem frequências na API. |
| Alertas (`alertas.html`) | Apenas estrutura visual | Exibe as faixas de risco previstas, sem consultar alertas reais. |
| Acompanhamentos (`acompanhamento.html`) | Apenas estrutura visual | Não registra intervenções nem notificações. |
| Relatórios (`relatorios.html`) | Apenas estrutura visual | Não consulta nem consolida dados da API. |

---

## 🖥️ Frontend — funcionamento atual

O frontend é uma aplicação web estática construída com HTML, CSS, Bootstrap e JavaScript puro. As páginas compartilham o cabeçalho e a navegação, montados pelo JavaScript após a validação da sessão.

### Autenticação e proteção das páginas

1. O formulário de `index.html` autentica e-mail e senha pelo Supabase Auth (`signInWithPassword`).
2. Após o login, o frontend consulta `GET /usuarios/me` enviando o JWT no cabeçalho `Authorization: Bearer`.
3. O acesso é aceito somente para os perfis `ADM` e `SECRETARIA`, com conta ativa.
4. Se `alterarSenhaObrigatorio` estiver verdadeiro, o usuário é encaminhado para `alterar-senha.html`.
5. As páginas internas permanecem ocultas até a validação do usuário terminar.
6. A opção de sair encerra a sessão no Supabase e redireciona para `index.html`.

A proteção no frontend melhora o fluxo de navegação, mas não substitui a autorização do backend. As regras de acesso efetivas também precisam ser aplicadas pela API.

### Comunicação com a API

A comunicação é centralizada em `js/auth.js`:

- `obterToken()` recupera o access token da sessão do Supabase.
- `requisicaoAPI(caminho, opcoes)` executa `fetch` para a URL base configurada e acrescenta o cabeçalho `Authorization`.
- Respostas HTTP `204` são tratadas sem corpo.
- Em respostas de erro, o frontend tenta apresentar `detail`, `message` ou `mensagem` retornados pela API.

O arquivo `js/config.js` concentra a URL do Supabase, a chave pública de acesso, a URL base da API e o caminho de consulta do usuário atual. A configuração enviada aponta a API para `http://localhost:9000`.

### Regras e comportamentos presentes nas telas integradas

- Alunos: formulário com nome e data de nascimento; listagem com pesquisa, edição e ativação/desativação.
- Turmas: formulário com nome e ano letivo; listagem com pesquisa, edição e ativação/desativação.
- Matrículas: seleção de aluno e turma ativos, data de início, histórico, pesquisa, encerramento e transferência. Encerramento e transferência solicitam datas no formato `DD/MM/AAAA` e convertem para `AAAA-MM-DD` antes de enviar à API.
- Períodos letivos: cadastro e edição de nome, ano, semestre, data inicial e data final; a tela impede que a data final seja anterior à inicial.
- Usuários: listagem de nome, e-mail, perfil, status e situação da troca de senha. O cadastro de secretaria e as ações administrativas são exibidos somente quando o usuário logado possui perfil `ADM`.

### Código legado de frequência

O material enviado também contém uma implementação anterior de controle de alunos e frequência baseada em `localStorage`, com cadastro por matrícula, marcação diária de presença/ausência, histórico, indicadores e relatórios locais. Esse código não utiliza os endpoints atuais do EduAlerta e não representa a implementação integrada ao backend. As páginas atuais de frequência, alertas e relatórios não carregam esse fluxo como funcionalidade conectada à API.

A lógica legada utiliza um critério de alerta de frequência inferior a 75%, diferente da regra de risco definida para o EduAlerta (percentual de faltas não justificadas por período letivo, com faixas de 20% e 25%). Esse critério antigo não deve ser tratado como regra oficial do projeto.

---



## 🔐 Perfis e permissões de acesso

O código atual possui dois perfis definidos no enum `Perfil`:

- `ADM`
- `SECRETARIA`

Não existe perfil `PROFESSOR` na implementação enviada.

### ADM

O perfil `ADM` pode utilizar as funcionalidades gerais do sistema e possui operações administrativas específicas sobre contas de secretaria:

- Cadastrar usuários com perfil `SECRETARIA`.
- Consultar e buscar usuários.
- Alterar o nome de outras contas `SECRETARIA`.
- Ativar e desativar outras contas `SECRETARIA`.

As operações administrativas de alteração de nome e status não permitem que o ADM altere a própria conta por esses endpoints.

### SECRETARIA

O perfil `SECRETARIA` pode utilizar as funcionalidades gerais do sistema e consultar os usuários. Não pode:

- Cadastrar novos usuários.
- Alterar o nome de outras contas.
- Alterar o status de outras contas.

### Provisionamento inicial

Não existe endpoint público de cadastro de usuários.

A primeira conta `ADM` deve ser criada/provisionada no Supabase Auth e vinculada manualmente a um registro correspondente em `tb_usuario`, com `auth_user_id` igual ao identificador UUID do usuário no Supabase e perfil `ADM`.

Depois desse provisionamento, o ADM pode cadastrar novas contas de secretaria pela API.

### Cadastro de secretaria e primeiro acesso

Ao cadastrar uma secretaria:

1. O backend gera uma senha temporária.
2. Cria a identidade do usuário no Supabase Auth.
3. Persiste os dados locais em `tb_usuario`, com perfil `SECRETARIA`.
4. Marca `alterar_senha_obrigatorio` como `true`.
5. Envia a senha temporária por e-mail.
6. Se ocorrer falha durante a persistência local ou o envio do e-mail, o backend tenta excluir a identidade criada no Supabase.

No primeiro acesso, o usuário deve alterar a senha pelo endpoint `PUT /usuarios/me/senha`. Enquanto `alterarSenhaObrigatorio` estiver ativo, as operações protegidas que usam o contexto normal do usuário são bloqueadas. A consulta de `/usuarios/me` e a troca de senha permanecem disponíveis para permitir a conclusão do primeiro acesso.

### Contas inativas

O backend verifica o campo `ativo` do usuário local em toda operação que resolve o usuário pelo `UsuarioContext`. Quando o ADM altera o status de uma secretaria, o serviço também atualiza o bloqueio correspondente no Supabase Auth.

A desativação da própria conta pelo endpoint `DELETE /usuarios/me` também desativa o usuário no Supabase Auth e marca o registro local como inativo.

### Regras gerais de segurança

- Todos os endpoints da API exigem autenticação.
- O token deve ser enviado no cabeçalho `Authorization: Bearer <JWT>`.
- O Spring Security valida o JWT recebido.
- O `UsuarioContext` converte o `sub` do JWT em UUID e procura o registro local por `auth_user_id`.
- Contas inativas são bloqueadas.
- A troca obrigatória de senha restringe o acesso às operações comuns até a atualização da credencial.
- O CORS atual permite origens `localhost` e `127.0.0.1` com portas variáveis, métodos `GET`, `POST`, `PUT`, `DELETE` e `OPTIONS`, e cabeçalhos `Authorization` e `Content-Type`.

---

## 🌐 API REST — Endpoints atuais

**Base URL local:** `http://localhost:9000`

Todos os endpoints abaixo exigem JWT válido, salvo quando a autenticação é realizada diretamente pelo Supabase Auth (que não é um endpoint deste backend). Para as rotas protegidas, envie:

```http
Authorization: Bearer SEU_TOKEN_JWT
Content-Type: application/json
````

Os endpoints e os métodos desta seção foram levantados dos controllers presentes no backend enviado.

### **Usuários —&#x20;**`/usuarios`

| Método   | Endpoint                       | Descrição                                                                | Regra de acesso                                                              |
| -------- | ------------------------------ | ------------------------------------------------------------------------ | ---------------------------------------------------------------------------- |
| `POST`   | `/usuarios`                    | Cadastra uma nova secretaria.                                            | Somente `ADM`.                                                               |
| `GET`    | `/usuarios/me`                 | Consulta os dados do usuário autenticado.                                | Usuário autenticado; disponível também durante a troca obrigatória de senha. |
| `PUT`    | `/usuarios/me`                 | Atualiza o nome do próprio usuário.                                      | Usuário autenticado que já concluiu a troca obrigatória de senha.            |
| `PUT`    | `/usuarios/me/senha`           | Atualiza a senha no Supabase Auth.                                       | Usuário autenticado; usado quando a troca é obrigatória.                     |
| `DELETE` | `/usuarios/me`                 | Desativa a própria conta.                                                | Usuário autenticado que já concluiu a troca obrigatória de senha.            |
| `GET`    | `/usuarios`                    | Lista os usuários cadastrados.                                           | `ADM` e `SECRETARIA`.                                                        |
| `GET`    | `/usuarios/buscar?nome={nome}` | Busca usuários por parte do nome, sem diferenciar maiúsculas/minúsculas. | `ADM` e `SECRETARIA`.                                                        |
| `PUT`    | `/usuarios/{id}/nome`          | Altera o nome de uma secretaria.                                         | Somente `ADM`; não permite alterar a própria conta por esta operação.        |
| `PUT`    | `/usuarios/{id}/status`        | Ativa ou desativa uma secretaria.                                        | Somente `ADM`; não permite alterar a própria conta por esta operação.        |

#### **Corpos das requisições de usuários**

`POST /usuarios`

```
{
  "nome": "Secretaria Exemplo",
  "email": "secretaria@escola.com"
}
```

O backend define o perfil como `SECRETARIA`; o cliente não envia perfil nem senha.

`PUT /usuarios/me` e `PUT /usuarios/{id}/nome`

```
{
  "nome": "Novo Nome"
}
```

`PUT /usuarios/me/senha`

```
{
  "novaSenha": "UmaSenhaForte123"
}
```

A senha deve possuir entre 8 e 72 caracteres.

`PUT /usuarios/{id}/status`

```
{
  "ativo": false
}
```

`GET /usuarios/buscar` exige o parâmetro de consulta `nome`.

### **Alunos —&#x20;**`/alunos`

| Método | Endpoint              | Descrição                                 |
| ------ | --------------------- | ----------------------------------------- |
| `POST` | `/alunos`             | Cadastra um aluno.                        |
| `GET`  | `/alunos`             | Lista os alunos em ordem crescente de ID. |
| `GET`  | `/alunos/{id}`        | Consulta um aluno pelo ID.                |
| `PUT`  | `/alunos/{id}`        | Atualiza nome e data de nascimento.       |
| `PUT`  | `/alunos/{id}/status` | Ativa ou desativa um aluno.               |

#### **Corpos das requisições de alunos**

`POST /alunos`

```
{
  "nome": "Nome do Aluno",
  "dataNascimento": "2012-05-20"
}
```

`PUT /alunos/{id}`

```
{
  "nome": "Nome Atualizado",
  "dataNascimento": "2012-05-20"
}
```

`PUT /alunos/{id}/status`

```
{
  "ativo": true
}
```

### **Turmas —&#x20;**`/turmas`

| Método | Endpoint              | Descrição                                 |
| ------ | --------------------- | ----------------------------------------- |
| `POST` | `/turmas`             | Cadastra uma turma.                       |
| `GET`  | `/turmas`             | Lista as turmas em ordem crescente de ID. |
| `GET`  | `/turmas/{id}`        | Consulta uma turma pelo ID.               |
| `PUT`  | `/turmas/{id}`        | Atualiza nome e ano letivo.               |
| `PUT`  | `/turmas/{id}/status` | Ativa ou desativa uma turma.              |

#### **Corpos das requisições de turmas**

`POST /turmas`

```
{
  "nome": "8º Ano A",
  "anoLetivo": 2026
}
```

`PUT /turmas/{id}`

```
{
  "nome": "8º Ano B",
  "anoLetivo": 2026
}
```

`PUT /turmas/{id}/status`

```
{
  "ativo": true
}
```

**Regra do ano letivo:** o backend rejeita o cadastro ou a alteração de uma turma quando `anoLetivo` é anterior ao ano corrente. A regra também é aplicada na atualização quando o ano informado é diferente do ano já registrado.

### **Períodos letivos —&#x20;**`/periodos-letivos`

| Método | Endpoint                        | Descrição                                                       |
| ------ | ------------------------------- | --------------------------------------------------------------- |
| `POST` | `/periodos-letivos`             | Cadastra um período letivo.                                     |
| `GET`  | `/periodos-letivos`             | Lista períodos por ano letivo e semestre, em ordem decrescente. |
| `GET`  | `/periodos-letivos/{id}`        | Consulta um período letivo pelo ID.                             |
| `PUT`  | `/periodos-letivos/{id}`        | Atualiza os dados de um período letivo.                         |
| `PUT`  | `/periodos-letivos/{id}/status` | Ativa ou desativa um período letivo.                            |

#### **Corpos das requisições de períodos letivos**

`POST /periodos-letivos`

```
{
  "nome": "1º Semestre de 2026",
  "anoLetivo": 2026,
  "numeroSemestre": 1,
  "dataInicio": "2026-02-02",
  "dataFim": "2026-06-30"
}
```

`PUT /periodos-letivos/{id}`

```
{
  "nome": "1º Semestre de 2026",
  "anoLetivo": 2026,
  "numeroSemestre": 1,
  "dataInicio": "2026-02-02",
  "dataFim": "2026-07-05"
}
```

`PUT /periodos-letivos/{id}/status`

```
{
  "ativo": true
}
```

**Validações:** nome obrigatório, ano letivo positivo, semestre igual a `1` ou `2`, datas obrigatórias e data final igual ou posterior à data inicial. O código atual não impõe que o ano do período letivo seja o ano corrente ou futuro.

### **Matrículas —&#x20;**`/matriculas`

| Método | Endpoint                         | Descrição                                                                        |
| ------ | -------------------------------- | -------------------------------------------------------------------------------- |
| `POST` | `/matriculas`                    | Cria uma matrícula para um aluno em uma turma.                                   |
| `GET`  | `/matriculas`                    | Lista as matrículas em ordem crescente de ID.                                    |
| `GET`  | `/matriculas/{id}`               | Consulta uma matrícula pelo ID.                                                  |
| `GET`  | `/matriculas/aluno/{alunoId}`    | Lista o histórico de matrículas de um aluno, da mais recente para a mais antiga. |
| `PUT`  | `/matriculas/{id}/encerramento`  | Encerra uma matrícula ativa.                                                     |
| `POST` | `/matriculas/{id}/transferencia` | Encerra a matrícula atual e cria outra na turma de destino.                      |

#### **Corpos das requisições de matrículas**

`POST /matriculas`

```
{
  "alunoId": 1,
  "turmaId": 2,
  "dataInicio": "2026-02-02"
}
```

`PUT /matriculas/{id}/encerramento`

```
{
  "dataFim": "2026-06-30"
}
```

`POST /matriculas/{id}/transferencia`

```
{
  "turmaId": 3,
  "dataTransferencia": "2026-07-01"
}
```

#### **Regras implementadas para matrículas**

- Só é possível matricular alunos ativos em turmas ativas.
- Uma matrícula nova é criada com status `ATIVA` e sem data final.
- O aluno não pode possuir matrículas com períodos sobrepostos.
- A consulta do histórico de um aluno ordena as matrículas por data de início decrescente e, em caso de empate, por ID decrescente.
- Somente matrículas com status `ATIVA` podem ser encerradas ou transferidas.
- A data de encerramento não pode ser anterior à data de início.
- A data de transferência deve ser posterior à data de início da matrícula atual.
- A turma de destino deve estar ativa e não pode ser a mesma turma da matrícula atual.
- Na transferência, a matrícula anterior termina no dia anterior à transferência e recebe status `ENCERRADA`; uma nova matrícula é criada na turma de destino, com início na data da transferência.
- A implementação utiliza bloqueio pessimista na busca do aluno durante operações de matrícula, para reduzir conflitos simultâneos sobre o histórico do mesmo aluno.

### **Respostas e erros da API**

Os controllers retornam, em linhas gerais:

- `201 Created` para cadastros e transferências que criam recurso.
- `200 OK` para consultas e atualizações que retornam conteúdo.
- `204 No Content` para operações sem corpo de resposta, como troca de senha e desativação da própria conta.
- `400 Bad Request` para argumentos inválidos, regras de negócio rejeitadas e erros de validação dos DTOs.
- `401 Unauthorized` quando a autenticação por JWT não é válida ou está ausente.
- `403 Forbidden` para operações negadas por regra de acesso ou por bloqueios do usuário.
- `404 Not Found` para recursos não encontrados quando a exceção correspondente é tratada como `EntityNotFoundException`.

O formato usado pelo `GlobalExceptionHandler` para os erros tratados é:

```
{
  "status": 400,
  "mensagem": "Descrição do erro"
}
```

A resposta de erro de autenticação produzida diretamente pelo Spring Security pode seguir o formato padrão do framework, não necessariamente o objeto `RestError`.

## **🧩 Regras de negócio já implementadas**

### **Alunos**

- Nome obrigatório, com até 150 caracteres.
- Data de nascimento obrigatória.
- Aluno é criado ativo.
- O status pode ser alterado sem excluir o registro.

### **Turmas**

- Nome obrigatório, com até 255 caracteres.
- Ano letivo obrigatório e positivo.
- O ano letivo não pode ser anterior ao ano corrente.
- Turma é criada ativa.
- O status pode ser alterado sem excluir o registro.

### **Períodos letivos**

- Nome obrigatório, com até 100 caracteres.
- Ano letivo obrigatório e positivo.
- Número do semestre limitado a `1` ou `2`.
- Data inicial e data final são obrigatórias.
- A data final não pode ser anterior à data inicial.
- Período letivo é criado ativo.
- O status pode ser alterado sem excluir o registro.

### **Matrículas**

- Relação entre aluno e turma com data de início, data final opcional e status.
- Status possíveis: `ATIVA` e `ENCERRADA`.
- Sobreposição de períodos de matrícula para o mesmo aluno é impedida.
- Transferência preserva o histórico, encerrando o vínculo anterior e criando outro.

## **🚨 Classificação de risco — regra prevista**

A classificação de risco está definida como regra funcional do projeto, mas **a rotina de cálculo e os endpoints correspondentes ainda não aparecem no backend enviado**.

| Percentual de faltas não justificadas | Classificação prevista |
| ------------------------------------- | ---------------------- |
| Abaixo de 20%                         | Regular                |
| De 20% até abaixo de 25%              | Preventivo             |
| 25% ou mais                           | Crítico                |

Fórmula prevista:

\\[ \text{Percentual de faltas} = \frac{\text{Faltas não justificadas}} {\text{Aulas realizadas}} \times 100 \\]

Regras previstas para a futura implementação:

- Considerar somente fechamentos semanais com status `FECHADO`.
- Desconsiderar fechamentos em `RASCUNHO`.
- Não contabilizar faltas justificadas no numerador.
- Utilizar a quantidade de aulas efetivamente realizadas como denominador.
- Acumular os dados dentro do período letivo correspondente.

## **📅 Controle de frequência — modelagem prevista**

O modelo funcional planejado utiliza fechamentos semanais por turma.

**Fechamento diário:**

- Data do dia.
- Quantidade de aulas efetivamente realizadas.

**Frequência semanal por aluno:**

- Matrícula relacionada.
- Quantidade de faltas não justificadas.
- Quantidade de faltas justificadas.

O fechamento semanal deverá possuir os estados `RASCUNHO` e `FECHADO`. Apenas os fechamentos concluídos deverão participar do cálculo de frequência.

**Decisão de modelagem:** não está prevista uma entidade `AULA`. A quantidade de aulas realizadas será consolidada por dia, e as faltas serão registradas por aluno/matrícula no fechamento semanal.

## **🔔 Alertas e acompanhamentos — modelagem prevista**

Os alertas deverão representar situações de risco identificadas a partir do percentual de faltas não justificadas. A identificação deverá considerar aluno e período letivo, evitando alertas duplicados para a mesma combinação.

Os acompanhamentos deverão registrar as intervenções realizadas pela instituição, incluindo tipo de intervenção, descrição, resultado, data/hora e usuário responsável.

A regra funcional prevista é que o registro de uma intervenção possa resolver o alerta correspondente, conforme as regras definitivas do módulo.

Notificações e encaminhamentos permanecem no escopo/modelagem planejados, mas não possuem controllers ou endpoints no backend enviado. Não há integração automática com WhatsApp nesta versão.

## **🛠️ Tecnologias**

### **Backend**

- Java 21
- Spring Boot 4.1.2
- Spring WebMVC
- Spring Data JPA
- Hibernate
- Spring Security
- Spring OAuth2 Resource Server
- Jakarta Validation
- API REST
- JWT
- Spring Boot Starter Mail

### **Banco de dados e autenticação**

- PostgreSQL
- Supabase
- Supabase Auth
- JDBC

### **Frontend**

- HTML5
- CSS3
- Bootstrap
- JavaScript (Vanilla JS)
- Fetch API

### **Envio de e-mails**

- Gmail SMTP
- Jakarta Mail
- `JavaMailSender`

### **Ferramentas**

- Git e GitHub
- Maven / Maven Wrapper
- Neovim
- VSCodium

### **Arquitetura**

O EduAlerta utiliza arquitetura cliente-servidor. O frontend consome os endpoints REST do backend, enquanto o Spring Boot concentra as regras de negócio e a comunicação com o banco de dados.

O backend está organizado em camadas:

| Camada                | Responsabilidade                                              |
| --------------------- | ------------------------------------------------------------- |
| `controllers`         | Exposição dos endpoints REST.                                 |
| `dtos`                | Transferência e validação dos dados de entrada e saída.       |
| `models`              | Entidades e enums do domínio.                                 |
| `repositorys`         | Persistência e consultas com Spring Data JPA.                 |
| `services/interfaces` | Contratos dos serviços.                                       |
| `services/impls`      | Implementação das regras de negócio.                          |
| `config/security`     | Configuração de segurança e resolução do usuário autenticado. |
| `exceptionHandler`    | Tratamento centralizado de exceções da API.                   |

### **Fluxo de autenticação e requisição**

No frontend, o Supabase Auth mantém a sessão e fornece o JWT. O JavaScript envia esse token em cada chamada protegida à API Spring Boot. O backend valida o JWT e identifica o usuário local associado ao identificador de autenticação do Supabase.

1. O usuário realiza login pelo Supabase Auth.
2. O Supabase retorna um JWT de acesso.
3. O frontend envia o token no cabeçalho `Authorization`.
4. O Spring Security valida o JWT como Resource Server.
5. O backend utiliza o `sub` do token para localizar o usuário local por `auth_user_id`.
6. O backend verifica se a conta está ativa e se a troca de senha obrigatória foi concluída, conforme a operação.
7. O controller encaminha a operação ao serviço.
8. O serviço executa as regras de negócio e utiliza os repositories para acessar o PostgreSQL.
9. A API retorna a resposta em JSON.

O frontend não acessa diretamente o PostgreSQL.

---

## 🗄️ Banco de dados

O banco utiliza PostgreSQL hospedado no Supabase. As entidades JPA apresentadas no backend enviado mapeiam as seguintes tabelas:

| Tabela | Entidade | Responsabilidade |
|---|---|---|
| `tb_usuario` | `Usuario` | Dados locais do usuário, UUID do Supabase Auth, perfil, status e indicador de troca obrigatória de senha. |
| `app.tb_aluno` | `Aluno` | Nome, data de nascimento, status e data de criação do aluno. |
| `app.tb_turma` | `Turma` | Nome, ano letivo e status da turma. |
| `app.tb_periodo_letivo` | `PeriodoLetivo` | Nome, ano letivo, semestre, datas inicial/final e status. |
| `app.tb_matricula` | `Matricula` | Vínculo entre aluno e turma, datas e status da matrícula. |

> Observação: `tb_usuario` está mapeada sem declaração explícita de schema na anotação `@Table`; as demais entidades acima declaram o schema `app` no código apresentado. A configuração efetiva do schema também depende das propriedades de conexão/JPA.

### Relacionamentos implementados

```mermaid
erDiagram
    ALUNO ||--o{ MATRICULA : possui
    TURMA ||--o{ MATRICULA : recebe

    ALUNO {
        bigint id PK
        string nome
        date data_nascimento
        boolean ativo
        datetime criado_em
    }

    TURMA {
        bigint id PK
        string nome
        int ano_letivo
        boolean ativo
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
        uuid auth_user_id UK
        string nome
        string email UK
        string perfil
        boolean ativo
        boolean alterar_senha_obrigatorio
    }

    PERIODO_LETIVO {
        bigint id PK
        string nome
        int ano_letivo
        int numero_semestre
        date data_inicio
        date data_fim
        boolean ativo
    }
````

`USUARIO` e `PERIODO_LETIVO` já possuem entidades e operações próprias no backend. Não há relacionamento JPA entre elas e as demais entidades no código enviado.

### **Decisões importantes**

**Matrícula e histórico escolar**

A turma não é armazenada diretamente no cadastro do aluno. O vínculo é representado por `Matricula`, permitindo encerrar uma matrícula e criar outra em uma transferência sem apagar o histórico anterior.

**Status em vez de exclusão**

Alunos, turmas e períodos letivos possuem indicador `ativo`. As operações disponibilizadas alteram esse status, preservando os registros.

**Identidade externa e usuário local**

O Supabase Auth gerencia a identidade e as credenciais. A tabela `tb_usuario` mantém os dados necessários à aplicação e associa o registro local ao UUID externo por `auth_user_id`.

### **Entidades ainda previstas, sem implementação no backend enviado**

A modelagem funcional do projeto também prevê:

- `FECHAMENTO_SEMANAL`
- `FECHAMENTO_DIA`
- `FREQUENCIA_SEMANAL`
- `RESPONSAVEL`
- `ALUNO_RESPONSAVEL`
- `ALERTA`
- `ACOMPANHAMENTO`
- `NOTIFICACAO`
- `ENCAMINHAMENTO` (opcional)

Essas entidades não aparecem como models JPA no código fornecido nesta atualização. Portanto, não são apresentadas como tabelas já implementadas.

## **📧 Envio de e-mails**

O backend utiliza Gmail SMTP por meio do `JavaMailSender`.

No fluxo atual, o e-mail é utilizado para enviar a senha temporária criada durante o cadastro de uma nova secretaria.

A configuração usada pelo projeto inclui servidor SMTP do Gmail, porta `587`, autenticação SMTP, STARTTLS e senha de aplicativo do Google. O envio depende de credenciais válidas configuradas na aplicação.

## **⚙️ Configuração do projeto**

O backend utiliza `application.properties` para configurar a conexão com o PostgreSQL/Supabase, a validação JWT, a integração administrativa com Supabase Auth e o envio de e-mails.

O arquivo com credenciais reais não deve ser versionado no Git. Utilize o modelo de configuração disponibilizado pelo projeto, quando presente, e preencha os valores da sua instalação.

Configurações necessárias para os recursos atuais incluem:

- Conexão JDBC com PostgreSQL.
- URL do projeto Supabase.
- Chave administrativa necessária às operações de criação, exclusão e bloqueio de usuários.
- Chave pública/anon ou publishable necessária à alteração de senha autenticada.
- Credenciais SMTP do Gmail.

Os nomes exatos das propriedades devem seguir o `application.properties` e as classes de configuração da versão do backend utilizada.

## **📂 Estrutura do projeto**

Estrutura observada no backend enviado:

```
EduAlerta/
├── back-end/
│   └── edualerta/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── edu/uninassau/edualerta/
│       │   │   │       ├── config/
│       │   │   │       │   └── security/
│       │   │   │       ├── controllers/
│       │   │   │       ├── dtos/
│       │   │   │       ├── exceptionHandler/
│       │   │   │       ├── models/
│       │   │   │       ├── repositorys/
│       │   │   │       └── services/
│       │   │   │           ├── interfaces/
│       │   │   │           └── impls/
│       │   │   └── resources/
│       │   └── test/
│       ├── mvnw
│       ├── mvnw.cmd
│       └── pom.xml
├── frontend/
│   ├── index.html
│   ├── alterar-senha.html
│   ├── painel.html
│   ├── usuarios.html
│   ├── alunos.html
│   ├── turmas.html
│   ├── matriculas.html
│   ├── frequencias.html
│   ├── alertas.html
│   ├── acompanhamento.html
│   ├── relatorios.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       ├── config.js
│       ├── auth.js
│       ├── layout.js
│       ├── login.js
│       ├── alterar-senha.js
│       ├── gestao-escolar.js
│       └── usuarios.js
└── README.md
```

## **🌐 Executando o frontend**

O frontend pode ser servido por qualquer servidor HTTP estático. Não é necessário compilar ou instalar um framework JavaScript.

1. Abra a pasta `frontend` em um servidor estático (por exemplo, a extensão Live Server do VS Code/VSCodium ou outro servidor HTTP local).
2. Confira `frontend/js/config.js`.
3. Configure `apiBaseUrl` para a URL em que o backend Spring Boot está acessível.
4. Configure `supabaseUrl` e `supabaseAnonKey` com os dados públicos do projeto Supabase.
5. Inicie o backend e acesse `index.html` pelo endereço HTTP fornecido pelo servidor estático.

Não é recomendado abrir as páginas diretamente pelo protocolo `file://`, pois a aplicação depende de requisições HTTP e da sessão do Supabase.

## **🚀 Executando o backend**

### **Pré-requisitos**

- Java 21.
- Maven (opcional, pois o projeto possui Maven Wrapper).
- Projeto Supabase configurado.
- Banco PostgreSQL acessível.
- Conta Gmail com senha de aplicativo, caso queira testar o envio de credenciais por e-mail.

### **1. Clone o repositório**

```
git clone URL_DO_REPOSITORIO
```

### **2. Entre no diretório do backend**

```
cd EduAlerta/back-end/edualerta
```

### **3. Configure a aplicação**

Crie/preencha `src/main/resources/application.properties` com os dados da sua instalação. Não publique credenciais reais no repositório.

### **4. Execute**

Linux:

```
./mvnw spring-boot:run
```

Windows:

```
.\mvnw.cmd spring-boot:run
```

O backend utiliza a porta `9000` na configuração atual descrita pelo projeto.

## **🧭 Próximas etapas do projeto**

Com base no escopo funcional, as próximas etapas de desenvolvimento incluem:

1. Implementar o cadastro e a associação de responsáveis.
2. Implementar fechamento semanal e seus registros diários.
3. Implementar os registros de frequência por matrícula.
4. Implementar o cálculo acumulado de faltas por período letivo.
5. Implementar a geração e consulta de alertas.
6. Implementar o registro de acompanhamentos.
7. Integrar o painel, frequência, alertas, acompanhamentos e relatórios aos endpoints que ainda serão implementados.
8. Revisar a configuração de ambiente do frontend para separar a URL local da API da URL usada em publicação.

### **Fora do escopo inicial**

- Login e acesso de professores.
- Login de alunos.
- Login de responsáveis.
- Integração e envio automático de mensagens por WhatsApp.
- Aplicativo mobile nativo.

## **📖 Sobre o projeto**

O EduAlerta é um projeto acadêmico desenvolvido no contexto do curso de Análise e Desenvolvimento de Sistemas, com foco na aplicação prática de engenharia de software, desenvolvimento web, modelagem de dados, APIs REST, autenticação e persistência de dados.

O projeto busca demonstrar como sistemas de informação podem apoiar a gestão escolar, facilitar o acompanhamento da frequência e contribuir para a identificação antecipada de possíveis situações de evasão.

**Desenvolvimento:** Em andamento.
