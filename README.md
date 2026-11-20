# Atenção: 
*Para acompanhar o projeto em estado de desenvolvimento, acesse a branch develop*

---

# 🎓 EduAlerta

**Sistema de monitoramento de frequência escolar e identificação de risco de evasão.**

O EduAlerta é uma aplicação web desenvolvida com o objetivo de auxiliar instituições de ensino no acompanhamento da frequência dos alunos, identificando possíveis situações de risco de evasão escolar.

A plataforma permite organizar os dados escolares, gerenciar alunos, turmas, matrículas e períodos letivos. O backend também permite criar e fechar semanas por turma, registrar aulas realizadas por dia, lançar faltas não justificadas e justificadas por matrícula, consultar a frequência acumulada de uma matrícula e gerar/atualizar automaticamente alertas por aluno e período letivo quando um fechamento semanal é concluído. A consulta de alertas já está disponível na API; acompanhamentos, notificações e relatórios permanecem em desenvolvimento.

O sistema busca utilizar a tecnologia como ferramenta de apoio à gestão escolar e à prevenção da evasão, facilitando a identificação antecipada de situações que exigem atenção e o registro das intervenções realizadas pela instituição.

> 🚧 **Status:** Em desenvolvimento. O backend possui autenticação, gerenciamento escolar, fechamento semanal, frequência por matrícula, cálculo percentual e geração/atualização automática de alertas por aluno e período letivo, com endpoint de consulta e filtros. O frontend possui integração com autenticação, gestão escolar, usuários e fechamento/frequência semanal. A tela de alertas ainda não está integrada à API; acompanhamentos, notificações e relatórios ainda não foram implementados.

------------------------------------------------------------------------

## 👥 Integrantes do projeto

- José Ricardo
- Francileidy Conceição
- Adrielly Kauany
- Itamar Kauan
- Pedro Cauã Vicente
- Miguel Azevedo

------------------------------------------------------------------------

## 📌 Estado atual do desenvolvimento

O desenvolvimento do EduAlerta acontece de forma incremental. Esta seção apresenta o estado real dos módulos considerando o backend e o frontend disponíveis no projeto.

### Implementado no backend

- Integração com Supabase Auth para autenticação por JWT.
- Validação dos tokens pelo Spring Security como OAuth2 Resource Server.
- Associação do usuário autenticado ao registro local da tabela `tb_usuario`.
- Bloqueio de contas inativas.
- Bloqueio de usuários que ainda precisam trocar a senha temporária.
- Cadastro administrativo de usuários com perfil `SECRETARIA`.
- Geração de senhas temporárias e envio por e-mail.
- Consulta, edição de nome, desativação e reativação de usuários conforme as regras de perfil.
- CRUD de alunos, incluindo alteração de status.
- CRUD de turmas, incluindo alteração de status e validação do ano letivo.
- CRUD de períodos letivos, incluindo alteração de status e validação das datas.
- Gerenciamento de matrículas, incluindo cadastro, consulta, encerramento e transferência entre turmas.
- Tratamento centralizado de erros de validação, regras de negócio, acesso negado e recursos não encontrados.
- Cadastro, consulta e fechamento de semanas por turma e período letivo.
- Registro, edição e remoção da quantidade de aulas realizadas em cada dia da semana, inclusive zero aulas.
- Registro, consulta, edição e remoção de frequência semanal por matrícula, separando faltas não justificadas e justificadas.
- Validações de vínculo da matrícula com a turma e com o intervalo semanal, limites de faltas em relação às aulas realizadas e bloqueio de alterações após o fechamento.
- Consulta da frequência acumulada de uma matrícula, considerando somente faltas não justificadas e registros de fechamentos `FECHADO` dentro do intervalo da matrícula.
- Cálculo do percentual de faltas não justificadas por aluno e período letivo, considerando somente fechamentos `FECHADO`.
- Geração e atualização automática de alertas ao fechar uma semana, com classificação `PREVENTIVO` ou `CRITICO`.
- Remoção do alerta quando o percentual acumulado fica abaixo de 20%.
- Consulta de alertas com filtros opcionais por aluno e período letivo.

### Implementado ou integrado no frontend

- Tela de login integrada ao Supabase Auth.
- Validação posterior do usuário autenticado pela API.
- Proteção das páginas internas por sessão, perfil permitido e situação da conta.
- Redirecionamento para troca obrigatória de senha no primeiro acesso.
- Consulta do usuário autenticado por `GET /usuarios/me`.
- Gestão de usuários integrada à API.
- Cadastro de secretarias por ADM.
- Alteração de nome e ativação/desativação de contas.
- Gestão de alunos integrada à API: cadastro, listagem, pesquisa, edição e alteração de status.
- Gestão de turmas integrada à API: cadastro, listagem, pesquisa, edição e alteração de status.
- Gestão de matrículas integrada à API: cadastro, listagem, pesquisa, encerramento e transferência.
- Gestão de períodos letivos integrada à API: cadastro, listagem, edição e alteração de status.
- Gestão de fechamentos semanais e frequências integrada à API: criação e consulta de fechamentos, registro/edição/remoção de dias, lançamento/edição/remoção de frequências e fechamento definitivo da semana.
- Layout compartilhado com identificação do usuário, perfil, navegação entre páginas e encerramento da sessão.

### Ainda não implementado no backend

- Cadastro e associação de responsáveis.
- Registro de acompanhamentos e intervenções.
- Notificações e encaminhamentos.
- Endpoints de dashboard e relatórios.
- Resolução manual de alertas por meio de acompanhamento; atualmente, a permanência do alerta é determinada automaticamente pelo percentual calculado.

As seções seguintes distinguem as funcionalidades já disponíveis na API daquelas que permanecem apenas previstas na modelagem ou no escopo futuro.

------------------------------------------------------------------------

## 🎯 Objetivos

- Gerenciar alunos, turmas e matrículas.
- Manter o histórico de vínculos dos alunos com as turmas.
- Registrar semanalmente a quantidade de aulas realizadas em cada dia.
- Registrar faltas e faltas justificadas dos alunos.
- Acompanhar o percentual de faltas não justificadas por período letivo.
- Identificar alunos com frequência irregular.
- Gerar alertas preventivos e críticos.
- Registrar intervenções realizadas pela instituição.
- Manter o histórico de frequência, alertas e acompanhamentos.
- Gerenciar os usuários internos por meio de perfis administrativos.
- Auxiliar a secretaria na identificação de possíveis situações de evasão escolar.

## ⚙️ Funcionalidades e situação

| Funcionalidade         | Situação no backend     | Descrição                                                                                                                                   |
|------------------------|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Autenticação           | Implementada            | Supabase Auth e validação de JWT pelo Spring Security.                                                                                      |
| Gestão de usuários     | Implementada            | Cadastro de secretarias, consulta, alteração de nome e alteração de status.                                                                 |
| Gestão de alunos       | Implementada            | Cadastro, listagem, consulta por ID, edição e ativação/desativação.                                                                         |
| Gestão de turmas       | Implementada            | Cadastro, listagem, consulta por ID, edição e ativação/desativação.                                                                         |
| Períodos letivos       | Implementada            | Cadastro, listagem, consulta por ID, edição e ativação/desativação.                                                                         |
| Matrículas             | Implementada            | Cadastro, listagem, consulta, histórico por aluno, encerramento e transferência.                                                            |
| Responsáveis           | Pendente                | Cadastro e associação de responsáveis aos alunos.                                                                                           |
| Fechamento semanal     | Implementada no backend | Criação, listagem, consulta, manutenção dos dias letivos e fechamento definitivo de uma semana por turma e período letivo.                  |
| Frequência semanal     | Implementada no backend | Registro, listagem, edição e remoção de faltas não justificadas e justificadas por matrícula, enquanto o fechamento estiver em rascunho.    |
| Frequência acumulada   | Implementada no backend | Consulta do total de faltas não justificadas, aulas realizadas e percentual da matrícula.                                                   |
| Classificação de risco | Implementada no backend | Classificação automática por aluno e período letivo: abaixo de 20% regular; de 20% até abaixo de 25% preventivo; 25% ou mais crítico.       |
| Alertas                | Implementada no backend | Geração/atualização automática ao fechar semanas e consulta por `GET /alertas`, com filtros opcionais. Alertas abaixo de 20% são removidos. |
| Acompanhamentos        | Pendente                | Registro das intervenções realizadas pela instituição.                                                                                      |
| Notificações           | Pendente                | Registro de notificações relacionadas aos alertas.                                                                                          |
| Encaminhamentos        | Opcional                | Registro de possíveis encaminhamentos relacionados aos alertas.                                                                             |

### Situação das telas no frontend

| Tela                                      | Situação                | Observação                                                                                         |
|-------------------------------------------|-------------------------|----------------------------------------------------------------------------------------------------|
| Login (`index.html`)                      | Integrada               | Autentica pelo Supabase Auth e consulta `/usuarios/me` para validar o acesso.                      |
| Alterar senha (`alterar-senha.html`)      | Integrada               | Envia a nova senha para `PUT /usuarios/me/senha`.                                                  |
| Usuários (`usuarios.html`)                | Integrada               | Lista usuários; ADM pode cadastrar secretarias, alterar nome e ativar/desativar contas.            |
| Alunos (`alunos.html`)                    | Integrada               | CRUD e alteração de status por meio de `/alunos`.                                                  |
| Turmas (`turmas.html`)                    | Integrada               | CRUD e alteração de status por meio de `/turmas`.                                                  |
| Matrículas e períodos (`matriculas.html`) | Integrada               | Cadastro, consulta, encerramento e transferência de matrículas; CRUD e status de períodos letivos. |
| Painel (`painel.html`)                    | Apenas estrutura visual | Os indicadores exibem estado de espera e não consultam dados da API.                               |
| Frequência (`frequencias.html`)           | Integrada               | Gerencia fechamentos semanais, dias/aulas e frequências por matrícula usando os endpoints da API.  |
| Alertas (`alertas.html`)                  | Apenas estrutura visual | O backend já disponibiliza `GET /alertas`, mas a tela ainda não consulta os alertas reais.         |
| Acompanhamentos (`acompanhamento.html`)   | Apenas estrutura visual | Não registra intervenções nem notificações.                                                        |
| Relatórios (`relatorios.html`)            | Apenas estrutura visual | Não consulta nem consolida dados da API.                                                           |

------------------------------------------------------------------------

## 🖥️ Frontend — funcionamento atual

O frontend é uma aplicação web estática construída com HTML, CSS, Bootstrap e JavaScript puro.

As páginas compartilham o cabeçalho e a navegação, montados pelo JavaScript após a validação da sessão.

### Autenticação e proteção das páginas

1.  O formulário de `index.html` autentica e-mail e senha pelo Supabase Auth, utilizando `signInWithPassword`.
2.  Após o login, o frontend consulta `GET /usuarios/me`, enviando o JWT no cabeçalho `Authorization: Bearer`.
3.  O acesso é aceito somente para os perfis `ADM` e `SECRETARIA`, com conta ativa.
4.  Se `alterarSenhaObrigatorio` estiver verdadeiro, o usuário é encaminhado para `alterar-senha.html`.
5.  As páginas internas permanecem ocultas até a validação do usuário terminar.
6.  A opção de sair encerra a sessão no Supabase e redireciona para `index.html`.

A proteção no frontend melhora o fluxo de navegação, mas não substitui a autorização do backend. As regras de acesso efetivas também são aplicadas pela API.

### Comunicação com a API

A comunicação é centralizada em `js/auth.js`.

As principais responsabilidades são:

- `obterToken()`: recupera o access token da sessão do Supabase.
- `requisicaoAPI(caminho, opcoes)`: executa `fetch` para a URL base configurada e acrescenta o cabeçalho `Authorization`.
- Respostas HTTP `204` são tratadas sem corpo.
- Em respostas de erro, o frontend tenta apresentar `detail`, `message` ou `mensagem` retornados pela API.

O arquivo `js/config.js` concentra:

- URL do Supabase.
- Chave pública de acesso.
- URL base da API.
- Caminho de consulta do usuário atual.

A configuração local enviada aponta a API para `http://localhost:9000`.

### Regras e comportamentos presentes nas telas integradas

**Alunos**

- Formulário com nome e data de nascimento.
- Listagem com pesquisa.
- Edição dos dados.
- Ativação e desativação.

**Turmas**

- Formulário com nome e ano letivo.
- Listagem com pesquisa.
- Edição dos dados.
- Ativação e desativação.

**Matrículas**

- Seleção de aluno e turma ativos.
- Data de início.
- Consulta do histórico de matrículas.
- Pesquisa.
- Encerramento de matrículas.
- Transferência entre turmas.

Encerramento e transferência solicitam datas no formato `DD/MM/AAAA` e convertem para `AAAA-MM-DD` antes de enviar à API.

**Períodos letivos**

- Cadastro e edição de nome, ano, semestre, data inicial e data final.
- A tela impede que a data final seja anterior à data inicial.
- Listagem e alteração de status.

**Frequência**

- Consulta fechamentos com filtros por turma e período letivo.
- Criação de fechamento semanal, com início em uma segunda-feira.
- Consulta dos dias e das frequências registrados.
- Inclusão, edição e remoção de dias e quantidades de aulas enquanto o fechamento está em rascunho.
- Lançamento, edição e remoção de faltas não justificadas e justificadas por matrícula.
- Fechamento definitivo da semana após as validações do backend.

**Usuários**

- Listagem de nome, e-mail, perfil, status e situação da troca de senha.
- Cadastro de secretarias.
- Alteração de nome.
- Ativação e desativação de contas.
- Ações administrativas exibidas somente quando o usuário logado possui perfil `ADM`.

### Código legado de frequência

O material do projeto também contém uma implementação anterior de controle de alunos e frequência baseada em `localStorage`, com cadastro por matrícula, marcação diária de presença/ausência, histórico, indicadores e relatórios locais.

Esse código não utiliza os endpoints atuais do EduAlerta e não representa a implementação integrada ao backend.

A página atual `frequencias.html` utiliza os endpoints novos de fechamento semanal e frequência. O código legado em `script.js` não representa esse fluxo integrado. As telas de alertas e relatórios ainda não consomem dados reais da API.

A lógica legada utiliza um critério de alerta de frequência inferior a 75%, diferente da regra de risco definida para o EduAlerta, baseada no percentual de faltas não justificadas por período letivo, com faixas de 20% e 25%.

Esse critério antigo não deve ser tratado como regra oficial do projeto.

------------------------------------------------------------------------

## 🔐 Perfis e permissões de acesso

O código atual possui dois perfis definidos no enum `Perfil`:

- `ADM`
- `SECRETARIA`

Não existe perfil `PROFESSOR` na implementação atual.

Cada perfil possui permissões específicas, definidas no backend.

| Operação                              |  ADM  | SECRETARIA |
|---------------------------------------|:-----:|:----------:|
| Acessar o sistema                     |  Sim  |    Sim     |
| Consultar a lista de usuários         |  Sim  |    Sim     |
| Buscar usuários                       |  Sim  |    Sim     |
| Cadastrar novas secretarias           |  Sim  |    Não     |
| Alterar o nome de outras secretarias  |  Sim  |    Não     |
| Ativar contas de secretaria           |  Sim  |    Não     |
| Desativar contas de secretaria        |  Sim  |    Não     |
| Gerenciar alunos, turmas e matrículas |  Sim  |    Sim     |
| Gerenciar períodos letivos            |  Sim  |    Sim     |
| Registrar e fechar frequências        | Sim\* |   Sim\*    |
| Consultar alertas                     | Sim\* |   Sim\*    |
| Registrar acompanhamentos             | Sim\* |   Sim\*    |

\* Permissões previstas para os módulos futuros, ainda sem endpoints implementados.

### ADM

O perfil `ADM` é responsável pelo gerenciamento das contas de secretaria.

Suas permissões incluem:

- Cadastrar novos usuários com perfil `SECRETARIA`.
- Consultar e buscar usuários cadastrados.
- Alterar o nome de outras contas `SECRETARIA`.
- Ativar e desativar contas `SECRETARIA`.
- Acessar as funcionalidades gerais do sistema.

As operações administrativas de alteração de nome e status não permitem que o ADM altere a própria conta por esses endpoints.

### SECRETARIA

O perfil `SECRETARIA` possui acesso às funcionalidades operacionais do EduAlerta.

Suas permissões atuais incluem:

- Consultar e buscar usuários cadastrados.
- Gerenciar alunos, turmas e matrículas.
- Gerenciar períodos letivos.

Para os módulos futuros, também está prevista a possibilidade de:

- Cadastrar e associar responsáveis.
- Registrar e fechar frequências semanais.
- Consultar alertas e registrar acompanhamentos.

A secretaria não pode cadastrar novos usuários, alterar o nome de outras secretarias ou modificar o status de suas contas.

### Provisionamento inicial

Não existe cadastro público de usuários.

A primeira conta administrativa é provisionada manualmente no Supabase Auth e vinculada ao registro correspondente na tabela `tb_usuario`, com perfil `ADM`.

O registro local deve possuir o mesmo UUID de identidade do usuário no Supabase Auth, armazenado em `auth_user_id`.

Após esse provisionamento, o ADM pode cadastrar novas contas com perfil `SECRETARIA`.

### Cadastro de secretaria e primeiro acesso

Ao cadastrar uma secretaria:

1.  O backend gera uma senha temporária.
2.  Cria a identidade do usuário no Supabase Auth.
3.  Persiste os dados locais em `tb_usuario`, com perfil `SECRETARIA`.
4.  Marca `alterar_senha_obrigatorio` como `true`.
5.  Envia a senha temporária por e-mail.
6.  Se ocorrer falha durante a persistência local ou o envio do e-mail, o backend tenta excluir a identidade criada no Supabase.

No primeiro acesso, o usuário deve alterar a senha pelo endpoint `PUT /usuarios/me/senha`.

Enquanto `alterarSenhaObrigatorio` estiver ativo, as operações protegidas que utilizam o contexto normal do usuário são bloqueadas. A consulta de `/usuarios/me` e a troca de senha permanecem disponíveis para permitir a conclusão do primeiro acesso.

### Contas desativadas

Uma conta de secretaria desativada não pode realizar login no sistema.

O bloqueio é aplicado no Supabase Auth e também é verificado pelo backend.

Quando o ADM altera o status de uma secretaria, o serviço atualiza o bloqueio correspondente no Supabase Auth e o status do registro local.

Assim, mesmo que o usuário possua um JWT emitido anteriormente, o backend deve rejeitar as requisições quando a conta estiver inativa.

A reativação da conta também é realizada pelo ADM.

A desativação da própria conta pelo endpoint `DELETE /usuarios/me` também desativa a identidade no Supabase Auth e marca o registro local como inativo.

Alunos e responsáveis não possuem acesso autenticado ao sistema na primeira versão.

### Regras gerais de segurança

- Todos os endpoints da API exigem autenticação.
- O token deve ser enviado no cabeçalho `Authorization: Bearer <JWT>`.
- O Spring Security valida o JWT recebido.
- O `UsuarioContext` converte o `sub` do JWT em UUID.
- O UUID é utilizado para localizar o usuário local por `auth_user_id`.
- Contas inativas são bloqueadas.
- A troca obrigatória de senha restringe o acesso às operações comuns até a atualização da credencial.
- As regras de acesso são verificadas pelo backend, independentemente da proteção visual do frontend.
- O CORS atual permite origens `localhost` e `127.0.0.1` com portas variáveis, métodos `GET`, `POST`, `PUT`, `DELETE` e `OPTIONS`, e cabeçalhos `Authorization` e `Content-Type`.

------------------------------------------------------------------------

## 🌐 API REST — Endpoints implementados

**Base URL local:** `http://localhost:9000`

Todos os endpoints abaixo exigem JWT válido.

Para as rotas protegidas, envie:

``` http
Authorization: Bearer SEU_TOKEN_JWT
Content-Type: application/json
```

A autenticação é realizada diretamente pelo Supabase Auth. O login não corresponde a um endpoint do backend Spring Boot.

Os endpoints e métodos desta seção representam os controllers presentes no backend documentado.

### Usuários — `/usuarios`

| Método   | Endpoint                       | Descrição                                                                | Regra de acesso                                                      |
|----------|--------------------------------|--------------------------------------------------------------------------|----------------------------------------------------------------------|
| `POST`   | `/usuarios`                    | Cadastra uma nova secretaria.                                            | Somente `ADM`.                                                       |
| `GET`    | `/usuarios/me`                 | Consulta os dados do usuário autenticado.                                | Usuário autenticado, inclusive durante a troca obrigatória de senha. |
| `PUT`    | `/usuarios/me`                 | Atualiza o nome do próprio usuário.                                      | Usuário autenticado que já concluiu a troca obrigatória de senha.    |
| `PUT`    | `/usuarios/me/senha`           | Atualiza a senha no Supabase Auth.                                       | Usuário autenticado.                                                 |
| `DELETE` | `/usuarios/me`                 | Desativa a própria conta.                                                | Usuário autenticado que já concluiu a troca obrigatória de senha.    |
| `GET`    | `/usuarios`                    | Lista os usuários cadastrados.                                           | `ADM` e `SECRETARIA`.                                                |
| `GET`    | `/usuarios/buscar?nome={nome}` | Busca usuários por parte do nome, sem diferenciar maiúsculas/minúsculas. | `ADM` e `SECRETARIA`.                                                |
| `PUT`    | `/usuarios/{id}/nome`          | Altera o nome de uma secretaria.                                         | Somente `ADM`; não permite alterar a própria conta.                  |
| `PUT`    | `/usuarios/{id}/status`        | Ativa ou desativa uma secretaria.                                        | Somente `ADM`; não permite alterar a própria conta.                  |

#### Corpos das requisições

**POST `/usuarios`**

``` json
{
  "nome": "Secretaria Exemplo",
  "email": "secretaria@escola.com"
}
```

O backend define o perfil como `SECRETARIA`. O cliente não envia perfil nem senha.

**PUT `/usuarios/me` e PUT `/usuarios/{id}/nome`**

``` json
{
  "nome": "Novo Nome"
}
```

**PUT `/usuarios/me/senha`**

``` json
{
  "novaSenha": "UmaSenhaForte123"
}
```

A senha deve possuir entre 8 e 72 caracteres.

**PUT `/usuarios/{id}/status`**

``` json
{
  "ativo": false
}
```

O endpoint `GET /usuarios/buscar` exige o parâmetro de consulta `nome`.

### Alunos — `/alunos`

| Método | Endpoint              | Descrição                                 |
|--------|-----------------------|-------------------------------------------|
| `POST` | `/alunos`             | Cadastra um aluno.                        |
| `GET`  | `/alunos`             | Lista os alunos em ordem crescente de ID. |
| `GET`  | `/alunos/{id}`        | Consulta um aluno pelo ID.                |
| `PUT`  | `/alunos/{id}`        | Atualiza nome e data de nascimento.       |
| `PUT`  | `/alunos/{id}/status` | Ativa ou desativa um aluno.               |

#### Corpos das requisições

**POST `/alunos`**

``` json
{
  "nome": "Nome do Aluno",
  "dataNascimento": "2012-05-20"
}
```

**PUT `/alunos/{id}`**

``` json
{
  "nome": "Nome Atualizado",
  "dataNascimento": "2012-05-20"
}
```

**PUT `/alunos/{id}/status`**

``` json
{
  "ativo": true
}
```

### Turmas — `/turmas`

| Método | Endpoint              | Descrição                                 |
|--------|-----------------------|-------------------------------------------|
| `POST` | `/turmas`             | Cadastra uma turma.                       |
| `GET`  | `/turmas`             | Lista as turmas em ordem crescente de ID. |
| `GET`  | `/turmas/{id}`        | Consulta uma turma pelo ID.               |
| `PUT`  | `/turmas/{id}`        | Atualiza nome e ano letivo.               |
| `PUT`  | `/turmas/{id}/status` | Ativa ou desativa uma turma.              |

#### Corpos das requisições

**POST `/turmas`**

``` json
{
  "nome": "8º Ano A",
  "anoLetivo": 2026
}
```

**PUT `/turmas/{id}`**

``` json
{
  "nome": "8º Ano B",
  "anoLetivo": 2026
}
```

**PUT `/turmas/{id}/status`**

``` json
{
  "ativo": true
}
```

**Regra do ano letivo:** o backend rejeita o cadastro ou a alteração de uma turma quando `anoLetivo` é anterior ao ano corrente. A regra também é aplicada na atualização quando o ano informado é diferente do ano já registrado.

### Períodos letivos — `/periodos-letivos`

| Método | Endpoint                        | Descrição                                                       |
|--------|---------------------------------|-----------------------------------------------------------------|
| `POST` | `/periodos-letivos`             | Cadastra um período letivo.                                     |
| `GET`  | `/periodos-letivos`             | Lista períodos por ano letivo e semestre, em ordem decrescente. |
| `GET`  | `/periodos-letivos/{id}`        | Consulta um período letivo pelo ID.                             |
| `PUT`  | `/periodos-letivos/{id}`        | Atualiza os dados de um período letivo.                         |
| `PUT`  | `/periodos-letivos/{id}/status` | Ativa ou desativa um período letivo.                            |

#### Corpos das requisições

**POST `/periodos-letivos`**

``` json
{
  "nome": "1º Semestre de 2026",
  "anoLetivo": 2026,
  "numeroSemestre": 1,
  "dataInicio": "2026-02-02",
  "dataFim": "2026-06-30"
}
```

**PUT `/periodos-letivos/{id}`**

``` json
{
  "nome": "1º Semestre de 2026",
  "anoLetivo": 2026,
  "numeroSemestre": 1,
  "dataInicio": "2026-02-02",
  "dataFim": "2026-07-05"
}
```

**PUT `/periodos-letivos/{id}/status`**

``` json
{
  "ativo": true
}
```

**Validações implementadas:**

- Nome obrigatório.
- Ano letivo positivo.
- Semestre igual a `1` ou `2`.
- Datas obrigatórias.
- Data final igual ou posterior à data inicial.

O código atual não impõe que o ano do período letivo seja o ano corrente ou futuro.

### Matrículas — `/matriculas`

| Método | Endpoint                                | Descrição                                                                              |
|--------|-----------------------------------------|----------------------------------------------------------------------------------------|
| `POST` | `/matriculas`                           | Cria uma matrícula para um aluno em uma turma.                                         |
| `GET`  | `/matriculas`                           | Lista as matrículas em ordem crescente de ID.                                          |
| `GET`  | `/matriculas/{id}`                      | Consulta uma matrícula pelo ID.                                                        |
| `GET`  | `/matriculas/aluno/{alunoId}`           | Lista o histórico de matrículas de um aluno, da mais recente para a mais antiga.       |
| `GET`  | `/matriculas/{id}/frequencia-acumulada` | Calcula faltas não justificadas, aulas realizadas e percentual acumulado da matrícula. |
| `PUT`  | `/matriculas/{id}/encerramento`         | Encerra uma matrícula ativa.                                                           |
| `POST` | `/matriculas/{id}/transferencia`        | Encerra a matrícula atual e cria outra na turma de destino.                            |

#### Corpos das requisições

**POST `/matriculas`**

``` json
{
  "alunoId": 1,
  "turmaId": 2,
  "dataInicio": "2026-02-02"
}
```

**PUT `/matriculas/{id}/encerramento`**

``` json
{
  "dataFim": "2026-06-30"
}
```

**POST `/matriculas/{id}/transferencia`**

``` json
{
  "turmaId": 3,
  "dataTransferencia": "2026-07-01"
}
```

**GET `/matriculas/{id}/frequencia-acumulada`**

Não recebe corpo. Retorna os totais utilizados no cálculo da frequência acumulada da matrícula.

Exemplo de resposta:

``` json
{
  "matriculaId": 1,
  "alunoId": 1,
  "alunoNome": "Nome do Aluno",
  "faltasNaoJustificadas": 12,
  "aulasRealizadas": 80,
  "percentualFaltas": 15.00
}
```

O exemplo é ilustrativo. O cálculo considera somente faltas não justificadas de fechamentos `FECHADO` associados à turma da matrícula e dentro do intervalo do vínculo. As faltas justificadas não entram no numerador. As aulas realizadas são somadas a partir dos registros diários de fechamentos fechados no intervalo da matrícula. Quando não há aulas contabilizadas, o percentual retornado é zero.

#### Regras implementadas para matrículas

- Só é possível matricular alunos ativos em turmas ativas.
- Uma matrícula nova é criada com status `ATIVA` e sem data final.
- O aluno não pode possuir matrículas com períodos sobrepostos.
- A consulta do histórico de um aluno ordena as matrículas por data de início decrescente e, em caso de empate, por ID decrescente.
- Somente matrículas com status `ATIVA` podem ser encerradas ou transferidas.
- A data de encerramento não pode ser anterior à data de início.
- A data de transferência deve ser posterior à data de início da matrícula atual.
- A turma de destino deve estar ativa e não pode ser a mesma turma da matrícula atual.
- Na transferência, a matrícula anterior termina no dia anterior à transferência e recebe status `ENCERRADA`.
- Uma nova matrícula é criada na turma de destino, com início na data da transferência.
- A implementação utiliza bloqueio pessimista na busca do aluno durante operações de matrícula, para reduzir conflitos simultâneos sobre o histórico do mesmo aluno.

### Respostas e erros da API

Os controllers retornam, em linhas gerais:

| Código HTTP        | Significado                                                                         |
|--------------------|-------------------------------------------------------------------------------------|
| `201 Created`      | Cadastro ou transferência que cria um recurso.                                      |
| `200 OK`           | Consulta ou atualização que retorna conteúdo.                                       |
| `204 No Content`   | Operação sem corpo de resposta, como troca de senha e desativação da própria conta. |
| `400 Bad Request`  | Argumentos inválidos, regras de negócio rejeitadas e erros de validação dos DTOs.   |
| `401 Unauthorized` | Autenticação JWT inválida ou ausente.                                               |
| `403 Forbidden`    | Operação negada por regra de acesso ou bloqueio do usuário.                         |
| `404 Not Found`    | Recurso não encontrado quando a exceção correspondente é tratada.                   |

O formato utilizado pelo `GlobalExceptionHandler` para os erros tratados é:

``` json
{
  "status": 400,
  "mensagem": "Descrição do erro"
}
```

A resposta de erro de autenticação produzida diretamente pelo Spring Security pode seguir o formato padrão do framework, não necessariamente o objeto `RestError`.

### Fechamentos semanais — `/fechamentos-semanais`

| Método   | Endpoint                                  | Descrição                                                                  |
|----------|-------------------------------------------|----------------------------------------------------------------------------|
| `POST`   | `/fechamentos-semanais`                   | Cria um fechamento semanal em status `RASCUNHO`.                           |
| `GET`    | `/fechamentos-semanais`                   | Lista fechamentos; aceita filtros opcionais `turmaId` e `periodoLetivoId`. |
| `GET`    | `/fechamentos-semanais/{id}`              | Consulta um fechamento, incluindo os dias registrados.                     |
| `POST`   | `/fechamentos-semanais/{id}/dias`         | Adiciona um dia e sua quantidade de aulas.                                 |
| `PUT`    | `/fechamentos-semanais/{id}/dias/{diaId}` | Atualiza um dia do fechamento.                                             |
| `DELETE` | `/fechamentos-semanais/{id}/dias/{diaId}` | Remove um dia do fechamento.                                               |
| `PUT`    | `/fechamentos-semanais/{id}/fechar`       | Valida os registros e altera o status para `FECHADO`.                      |

**POST `/fechamentos-semanais`**

``` json
{
  "turmaId": 1,
  "periodoLetivoId": 1,
  "dataInicioSemana": "2026-11-16"
}
```

A data inicial deve ser uma segunda-feira. A data final é calculada pelo backend como seis dias depois. A semana precisa ter interseção com o período letivo informado, e não pode existir outro fechamento para a mesma turma, período e data inicial.

**POST `/fechamentos-semanais/{id}/dias`** e **PUT `/fechamentos-semanais/{id}/dias/{diaId}`**

``` json
{
  "data": "2026-11-16",
  "quantidadeAulas": 0
}
```

A quantidade de aulas deve ser maior ou igual a zero. O valor zero é válido para dias sem aula. A data deve pertencer ao intervalo do fechamento e não pode ser repetida dentro da mesma semana.

### Frequências semanais — `/fechamentos-semanais/{fechamentoId}/frequencias`

| Método   | Endpoint                                                          | Descrição                                                      |
|----------|-------------------------------------------------------------------|----------------------------------------------------------------|
| `POST`   | `/fechamentos-semanais/{fechamentoId}/frequencias`                | Registra a frequência de uma matrícula no fechamento.          |
| `GET`    | `/fechamentos-semanais/{fechamentoId}/frequencias`                | Lista as frequências do fechamento.                            |
| `PUT`    | `/fechamentos-semanais/{fechamentoId}/frequencias/{frequenciaId}` | Atualiza as quantidades de faltas.                             |
| `DELETE` | `/fechamentos-semanais/{fechamentoId}/frequencias/{frequenciaId}` | Remove a frequência enquanto o fechamento estiver em rascunho. |

**POST `/fechamentos-semanais/{fechamentoId}/frequencias`**

``` json
{
  "matriculaId": 1,
  "faltas": 2,
  "faltasJustificadas": 1
}
```

**PUT `/fechamentos-semanais/{fechamentoId}/frequencias/{frequenciaId}`**

``` json
{
  "faltas": 1,
  "faltasJustificadas": 2
}
```

O registro é associado à matrícula, que determina o aluno e o vínculo com a turma. Cada matrícula pode possuir apenas um registro de frequência por fechamento.

### Regras dos fechamentos e frequências implementadas

- O fechamento é criado em `RASCUNHO` e só pode ser fechado depois de possuir ao menos um dia registrado.
- A semana começa obrigatoriamente em uma segunda-feira e termina no domingo.
- A turma e o período letivo precisam estar ativos no momento da criação.
- Não pode haver dois fechamentos para a mesma turma, período letivo e semana.
- Cada data pode aparecer uma única vez no mesmo fechamento.
- A quantidade de aulas por dia aceita zero, mas não aceita valores negativos.
- Só podem ser registradas frequências de matrículas vinculadas à turma do fechamento e com vínculo vigente em algum momento daquela semana.
- Cada matrícula pode ter apenas uma frequência por fechamento.
- Faltas e faltas justificadas são campos separados e não podem ser negativas.
- A soma de faltas não justificadas e justificadas não pode ultrapassar o total de aulas realizadas durante o período em que a matrícula esteve vinculada à turma naquela semana.
- Ao fechar a semana, o backend exige um registro de frequência para cada matrícula da turma cujo vínculo se sobreponha ao intervalo semanal e valida novamente os totais.
- Depois de `FECHADO`, os dias e as frequências não podem ser incluídos, alterados ou removidos.
- O fechamento registra o usuário autenticado que o criou.

### Alertas — `/alertas`

| Método | Endpoint   | Descrição                                                                               |
|--------|------------|-----------------------------------------------------------------------------------------|
| `GET`  | `/alertas` | Lista os alertas existentes. Aceita os filtros opcionais `alunoId` e `periodoLetivoId`. |

Exemplos de consulta:

``` http
GET /alertas
GET /alertas?alunoId=1
GET /alertas?periodoLetivoId=2
GET /alertas?alunoId=1&periodoLetivoId=2
```

O endpoint exige autenticação JWT e retorna os dados do alerta, incluindo aluno, período letivo, percentual, nível, motivo e data da última atualização.

Não existem endpoints de cadastro, edição ou exclusão manual de alertas. A geração e a atualização são realizadas pelo backend durante o fechamento semanal.

### Endpoints ainda não implementados

Os seguintes módulos não possuem controllers ou endpoints disponíveis no backend atual:

- Responsáveis e associação entre responsáveis e alunos.
- Acompanhamentos e intervenções.
- Notificações e encaminhamentos.
- Dashboard e relatórios.

------------------------------------------------------------------------

## 🧩 Regras de negócio já implementadas

Esta seção reúne as validações e regras presentes nos módulos atualmente desenvolvidos.

### Alunos

- Nome obrigatório, com até 150 caracteres.
- Data de nascimento obrigatória.
- Aluno é criado ativo.
- O status pode ser alterado sem excluir o registro.

### Turmas

- Nome obrigatório, com até 255 caracteres.
- Ano letivo obrigatório e positivo.
- O ano letivo não pode ser anterior ao ano corrente.
- Turma é criada ativa.
- O status pode ser alterado sem excluir o registro.

### Períodos letivos

- Nome obrigatório, com até 100 caracteres.
- Ano letivo obrigatório e positivo.
- Número do semestre limitado a `1` ou `2`.
- Data inicial e data final são obrigatórias.
- A data final não pode ser anterior à data inicial.
- Período letivo é criado ativo.
- O status pode ser alterado sem excluir o registro.

### Matrículas

- Relação entre aluno e turma com data de início, data final opcional e status.
- Status possíveis: `ATIVA` e `ENCERRADA`.
- Alunos inativos não podem receber novas matrículas.
- Turmas inativas não podem receber novas matrículas.
- Sobreposição de períodos de matrícula para o mesmo aluno é impedida.
- Transferência preserva o histórico, encerrando o vínculo anterior e criando outro.
- O encerramento e a transferência respeitam a ordem cronológica das datas.

### Fechamentos semanais e frequência

- A semana é identificada pela segunda-feira como data inicial; a data final corresponde ao domingo.
- O fechamento pertence a uma turma, a um período letivo e ao usuário que o criou.
- O fechamento inicia em `RASCUNHO` e pode ser alterado apenas nesse estado.
- Os dias são registrados individualmente, com quantidade de aulas maior ou igual a zero; portanto, dias sem aula podem ser registrados explicitamente com `quantidadeAulas: 0`.
- O mesmo dia não pode ser cadastrado duas vezes no mesmo fechamento.
- A frequência é vinculada à matrícula, não diretamente ao aluno, e separa `faltas` de `faltasJustificadas`.
- A matrícula precisa pertencer à turma do fechamento e seu intervalo de vínculo precisa se sobrepor à semana.
- A soma das duas categorias de faltas não pode exceder as aulas realizadas durante o intervalo em que a matrícula esteve vinculada naquela semana.
- Para fechar a semana, todas as matrículas com vínculo sobreposto precisam possuir frequência registrada; os totais são validados novamente.
- Um fechamento `FECHADO` torna-se imutável pelos endpoints de manutenção de dias e frequências.

### Usuários

- Não existe cadastro público.
- O perfil de novas contas cadastradas pela API é definido como `SECRETARIA`.
- Somente `ADM` pode cadastrar secretarias.
- Somente `ADM` pode alterar o nome e o status de outras contas de secretaria.
- As operações administrativas não permitem alterar a própria conta.
- O usuário pode alterar o próprio nome após concluir a troca obrigatória de senha.
- A troca de senha é realizada no Supabase Auth.
- A senha temporária é gerada pelo backend e enviada por e-mail.
- Contas inativas são bloqueadas.
- O backend verifica o status e a situação da troca obrigatória de senha nas operações protegidas.

------------------------------------------------------------------------

## 🚨 Frequência acumulada, classificação de risco e alertas

O backend possui dois cálculos relacionados, mas com finalidades e recortes diferentes:

- `GET /matriculas/{id}/frequencia-acumulada` calcula a frequência acumulada de uma matrícula, considerando o intervalo do vínculo.
- Ao fechar um fechamento semanal, o backend recalcula o percentual do aluno no período letivo daquele fechamento e atualiza o alerta correspondente.

A classificação persistida no alerta é baseada na combinação **aluno + período letivo**, e não em uma matrícula isolada.

| Percentual de faltas não justificadas | Classificação |
|---------------------------------------|---------------|
| Abaixo de 20%                         | Regular       |
| De 20% até abaixo de 25%              | Preventivo    |
| 25% ou mais                           | Crítico       |

O cálculo implementado utiliza a seguinte fórmula:

\[ \text{Percentual de faltas} = \frac{\text{Faltas não justificadas}} {\text{Aulas realizadas}} \times 100 \]

### Regras aplicadas pelo cálculo

- Somente fechamentos semanais com status `FECHADO` serão considerados.
- Fechamentos em `RASCUNHO` não entrarão no cálculo.
- Faltas justificadas não serão contabilizadas no percentual.
- O denominador corresponderá à quantidade de aulas efetivamente realizadas.
- Para o endpoint de frequência acumulada, o recorte é o intervalo da matrícula e os fechamentos da turma vinculados a esse intervalo.
- Para os alertas, são somadas as faltas não justificadas de todas as matrículas do aluno no período letivo, em fechamentos `FECHADO`, respeitando o vínculo da matrícula com a turma e o intervalo de matrícula.
- Para os alertas, as aulas realizadas são somadas a partir dos registros diários dos fechamentos `FECHADO` do período letivo, considerando a relação entre aluno, matrícula, turma e período.
- Faltas justificadas não entram no numerador.
- O percentual é calculado com duas casas decimais e arredondamento `HALF_UP`; a classificação considera o percentual arredondado utilizado pelo serviço.

As faixas são aplicadas pelo serviço de alertas. O cálculo e a persistência da classificação são executados automaticamente ao fechar uma semana.

### Exemplo

Considere um aluno que acumulou 23 faltas não justificadas em um período com 100 aulas realizadas:

\[ \frac{23}{100}\times100=23% \]

O resultado corresponde à classificação **Preventivo**.

------------------------------------------------------------------------

## 📅 Controle de frequência — implementação atual

O registro da frequência é realizado por meio de fechamentos semanais por turma. O fechamento reúne as informações dos dias letivos e as frequências dos alunos vinculados às matrículas correspondentes. A interface web já consome esses endpoints.

### Informações previstas

**Fechamento diário:**

- Data do dia.
- Quantidade de aulas efetivamente realizadas.

**Frequência semanal por aluno:**

- Matrícula relacionada.
- Quantidade de faltas não justificadas.
- Quantidade de faltas justificadas.

Dias sem aulas, feriados e suspensões devem ser registrados com `quantidadeAulas: 0`; o backend não infere feriados nem cria automaticamente os registros diários.

### Status do fechamento

| Status     | Descrição                                                             |
|------------|-----------------------------------------------------------------------|
| `RASCUNHO` | Registro ainda não concluído. Não participa do cálculo de frequência. |
| `FECHADO`  | Registro concluído e considerado nos cálculos do período letivo.      |

A separação entre fechamento e frequência permite consolidar as informações sem precisar cadastrar individualmente cada aula ministrada. O endpoint de frequência acumulada utiliza os fechamentos concluídos para calcular o percentual da matrícula.

**Decisão de modelagem:** o sistema não utiliza uma entidade `AULA`. A quantidade de aulas realizadas será registrada por dia em `FECHAMENTO_DIA`, enquanto as faltas serão consolidadas por aluno em `FREQUENCIA_SEMANAL`.

As entidades `FechamentoSemanal`, `FechamentoDia` e `FrequenciaSemanal`, seus repositories, DTOs, services e controllers estão implementados no backend. A tela `frequencias.html` também está integrada aos endpoints de fechamento e frequência.

------------------------------------------------------------------------

## 🔔 Alertas e acompanhamentos

A geração e a atualização de alertas estão implementadas no backend. Cada alerta é associado a um aluno e a um período letivo, com restrição de unicidade para essa combinação.

### Funcionamento automático dos alertas

1.  A secretaria fecha um fechamento semanal.
2.  O backend valida os dias e as frequências registradas.
3.  O fechamento passa para `FECHADO`.
4.  Para cada aluno que possui frequência registrada naquele fechamento, o serviço recalcula o percentual acumulado no período letivo.
5.  Se o percentual for inferior a 20%, um alerta existente para o aluno e período é removido.
6.  Se o percentual for igual ou superior a 20%, o backend cria ou atualiza o alerta:
    - `PREVENTIVO`: de 20% até abaixo de 25%.
    - `CRITICO`: 25% ou mais.

Quando um alerta existente permanece dentro das faixas de risco, o mesmo registro é atualizado, sem duplicação. Se o percentual cair abaixo de 20%, o registro é excluído; caso o aluno volte a atingir 20% ou mais em um fechamento posterior, um novo alerta poderá ser criado com outro ID.

O alerta armazena o aluno, o período letivo, o percentual, o nível, o motivo descritivo e a data/hora da última atualização. A consulta é disponibilizada por `GET /alertas`, com filtros opcionais por aluno e período letivo.

### Ainda pendente

- Acompanhamentos e intervenções realizados pela instituição.
- Resolução manual de alertas por meio do registro de uma intervenção.
- Notificações relacionadas aos alertas.
- Encaminhamentos, previstos como funcionalidade opcional.

A entidade `ACOMPANHAMENTO` permanece prevista para registrar tipo de intervenção, descrição, resultado, data/hora e usuário responsável. A entidade `NOTIFICACAO` também permanece no escopo futuro; na primeira versão, não está prevista integração automática com WhatsApp.

------------------------------------------------------------------------

## 🛠️ Tecnologias

### Backend

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

### Banco de dados e autenticação

- PostgreSQL
- Supabase
- Supabase Auth
- JDBC

### Frontend

- HTML5
- CSS3
- Bootstrap
- JavaScript (Vanilla JS)
- Fetch API

### Envio de e-mails

- Gmail SMTP
- Jakarta Mail
- `JavaMailSender`

### Ferramentas

- Git e GitHub
- Maven / Maven Wrapper
- Neovim
- VSCodium

------------------------------------------------------------------------

## 🏗️ Arquitetura

O EduAlerta utiliza uma arquitetura cliente-servidor. O frontend consome os endpoints REST disponibilizados pelo backend, enquanto o Spring Boot concentra as regras de negócio e a comunicação com o banco de dados.

O backend utiliza arquitetura em camadas, separando as responsabilidades da aplicação.

| Camada                | Responsabilidade                                              |
|-----------------------|---------------------------------------------------------------|
| `controllers`         | Exposição dos endpoints REST.                                 |
| `dtos`                | Transferência e validação dos dados de entrada e saída.       |
| `models`              | Entidades e enums do domínio.                                 |
| `repositorys`         | Persistência e consultas com Spring Data JPA.                 |
| `services/interfaces` | Contratos dos serviços.                                       |
| `services/impls`      | Implementação das regras de negócio.                          |
| `config/security`     | Configuração de segurança e resolução do usuário autenticado. |
| `exceptionHandler`    | Tratamento centralizado de exceções da API.                   |

### Comunicação entre os componentes

1.  O usuário realiza login pelo Supabase Auth.
2.  O Supabase retorna um JWT de acesso.
3.  O frontend envia o token no cabeçalho `Authorization`.
4.  O Spring Security valida o JWT como OAuth2 Resource Server.
5.  O backend utiliza o `sub` do token para localizar o usuário local por `auth_user_id`.
6.  O backend verifica se a conta está ativa e se a troca de senha obrigatória foi concluída, conforme a operação.
7.  O controller encaminha a operação ao serviço.
8.  O serviço executa as regras de negócio e utiliza os repositories para acessar o PostgreSQL.
9.  A API retorna a resposta ao frontend em JSON.

O frontend não acessa diretamente o PostgreSQL. Toda operação sobre os dados da aplicação passa pelo backend.

------------------------------------------------------------------------

## 🗄️ Banco de dados

O banco de dados utiliza PostgreSQL hospedado pelo Supabase.

A aplicação utiliza o schema `app` para organizar as tabelas escolares do sistema.

### Entidades e tabelas atualmente implementadas

As entidades JPA presentes no backend documentado mapeiam as seguintes tabelas:

| Tabela                      | Entidade            | Responsabilidade                                                                                          |
|-----------------------------|---------------------|-----------------------------------------------------------------------------------------------------------|
| `tb_usuario`                | `Usuario`           | Dados locais do usuário, UUID do Supabase Auth, perfil, status e indicador de troca obrigatória de senha. |
| `app.tb_aluno`              | `Aluno`             | Nome, data de nascimento, status e data de criação do aluno.                                              |
| `app.tb_turma`              | `Turma`             | Nome, ano letivo e status da turma.                                                                       |
| `app.tb_periodo_letivo`     | `PeriodoLetivo`     | Nome, ano letivo, semestre, datas inicial/final e status.                                                 |
| `app.tb_matricula`          | `Matricula`         | Vínculo entre aluno e turma, datas e status da matrícula.                                                 |
| `app.tb_fechamento_semanal` | `FechamentoSemanal` | Fechamento semanal de uma turma em um período letivo, com usuário responsável e status.                   |
| `app.tb_fechamento_dia`     | `FechamentoDia`     | Data e quantidade de aulas realizadas em cada dia do fechamento.                                          |
| `app.tb_frequencia_semanal` | `FrequenciaSemanal` | Faltas não justificadas e justificadas por matrícula e fechamento semanal.                                |
| `app.tb_alerta`             | `Alerta`            | Percentual acumulado, nível e motivo do alerta por aluno e período letivo, com data de atualização.       |

**Observação sobre schemas:** a entidade `Usuario` está mapeada sem declaração explícita de schema na anotação `@Table`. As demais entidades acima declaram o schema `app` no código apresentado. A localização efetiva de `tb_usuario` também depende das propriedades de conexão e configuração JPA.

### Relacionamentos implementados

O diagrama abaixo representa os relacionamentos JPA implementados no backend atual.

``` mermaid
erDiagram
    ALUNO ||--o{ MATRICULA : possui
    TURMA ||--o{ MATRICULA : recebe
    TURMA ||--o{ FECHAMENTO_SEMANAL : possui
    PERIODO_LETIVO ||--o{ FECHAMENTO_SEMANAL : organiza
    USUARIO ||--o{ FECHAMENTO_SEMANAL : registra
    FECHAMENTO_SEMANAL ||--o{ FECHAMENTO_DIA : detalha
    FECHAMENTO_SEMANAL ||--o{ FREQUENCIA_SEMANAL : contem
    MATRICULA ||--o{ FREQUENCIA_SEMANAL : recebe
    ALUNO ||--o{ ALERTA : possui
    PERIODO_LETIVO ||--o{ ALERTA : classifica

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

    FECHAMENTO_SEMANAL {
        bigint id PK
        bigint turma_id FK
        bigint periodo_letivo_id FK
        bigint usuario_id FK
        date data_inicio_semana
        date data_fim_semana
        string status
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
```

`USUARIO` e `PERIODO_LETIVO` já possuem entidades e operações próprias no backend. Entretanto, não há relacionamento JPA entre elas e as demais entidades no código enviado.

### Modelagem completa do projeto

O modelo entidade-relacionamento abaixo representa a estrutura funcional completa planejada para o EduAlerta.

Ele inclui tanto as entidades já implementadas quanto aquelas que ainda serão desenvolvidas.

``` mermaid
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
        uuid auth_user_id UK
        string nome
        string email UK
        string perfil
        boolean ativo
        boolean alterar_senha_obrigatorio
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
        decimal percentual_faltas
        string nivel
        string motivo
        datetime atualizado_em
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

    PERIODO_LETIVO ||--o{ FECHAMENTO_SEMANAL : organiza
    TURMA ||--o{ FECHAMENTO_SEMANAL : possui
    USUARIO ||--o{ FECHAMENTO_SEMANAL : registra

    ALUNO ||--o{ MATRICULA : possui
    TURMA ||--o{ MATRICULA : recebe

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

### Entidades ainda previstas, sem implementação no backend

| Entidade            | Responsabilidade                                               |
|---------------------|----------------------------------------------------------------|
| `RESPONSAVEL`       | Armazena os dados dos responsáveis pelos alunos.               |
| `ALUNO_RESPONSAVEL` | Relaciona alunos e responsáveis.                               |
| `ALERTA`            | Armazena situações de risco identificadas para os alunos.      |
| `ACOMPANHAMENTO`    | Registra as intervenções realizadas pela instituição.          |
| `NOTIFICACAO`       | Mantém os registros de notificações relacionadas aos alertas.  |
| `ENCAMINHAMENTO`    | Representa possíveis encaminhamentos relacionados aos alertas. |

Essas entidades ainda não aparecem como models JPA no backend documentado e, portanto, não devem ser consideradas tabelas implementadas.

### Decisões importantes da modelagem

**Matrícula e histórico escolar**

A turma não é armazenada diretamente no cadastro do aluno. O relacionamento é feito por meio de `MATRICULA`, permitindo registrar mudanças de turma sem perder os vínculos anteriores.

**Frequência vinculada à matrícula**

A entidade `FREQUENCIA_SEMANAL` referencia a matrícula, e não diretamente o aluno. Isso permite preservar a relação entre o registro de frequência e o vínculo escolar correspondente.

**Fechamento semanal**

A frequência não exige o cadastro individual de cada aula. O fechamento semanal agrupa os registros diários de aulas realizadas e os registros de frequência dos alunos.

**Status em vez de exclusão**

Alunos, turmas e períodos letivos possuem indicador `ativo`. As operações disponibilizadas alteram esse status, preservando os registros.

**Identidade externa e usuário local**

O Supabase Auth gerencia a identidade e as credenciais. A tabela `tb_usuario` mantém os dados necessários à aplicação e associa o registro local ao UUID externo por `auth_user_id`.

**Alertas por período letivo**

A identificação de risco considera o aluno e o período letivo, evitando a duplicação de alertas para a mesma combinação.

**Separação entre alerta e acompanhamento**

O alerta representa a situação identificada pelo sistema. O acompanhamento representa a intervenção realizada pela instituição.

------------------------------------------------------------------------

## 📧 Envio de e-mails

O envio de e-mails é realizado pelo backend utilizando o Gmail SMTP, por meio do `JavaMailSender`.

Na primeira versão, o envio é utilizado principalmente para encaminhar as credenciais temporárias geradas durante o cadastro de novos usuários.

A configuração utiliza:

- Servidor SMTP do Gmail.
- Porta `587`.
- Autenticação SMTP.
- STARTTLS.
- Senha de aplicativo do Google.

Não é necessário possuir domínio próprio para utilizar uma conta Gmail como remetente, respeitando os limites e as políticas do provedor.

O envio depende de credenciais válidas configuradas na aplicação.

------------------------------------------------------------------------

## ⚙️ Configuração do projeto

O backend utiliza arquivos `application.properties` para configurar:

- Conexão JDBC com PostgreSQL.
- Integração com Supabase Auth.
- Validação de JWT.
- Operações administrativas no Supabase Auth.
- Alteração de senha autenticada.
- Envio de e-mails pelo Gmail SMTP.

O repositório disponibiliza um arquivo de exemplo:

``` text
application.properties.example
```

Para executar a aplicação, copie esse modelo para:

``` text
src/main/resources/application.properties
```

Em seguida, preencha os dados da sua instalação.

As configurações necessárias incluem:

- URL e credenciais de conexão com PostgreSQL/Supabase.
- URL do projeto Supabase.
- Chave administrativa necessária às operações de criação, exclusão e bloqueio de usuários.
- Chave pública/anon ou publishable necessária à alteração de senha autenticada.
- Credenciais SMTP do Gmail.

Os nomes exatos das propriedades devem seguir o `application.properties` e as classes de configuração da versão do backend utilizada.

**O funcionamento do backend não depende de variáveis de ambiente na configuração atual documentada.**

O arquivo `application.properties` com credenciais reais não deve ser versionado no Git.

No frontend, as configurações ficam centralizadas em `js/config.js`, incluindo a URL da API e os dados públicos de conexão com o Supabase.

------------------------------------------------------------------------

## 📂 Estrutura do projeto

A estrutura geral do repositório separa o backend e o frontend.

### Backend

``` text
back-end/
└── edualerta/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── edu/uninassau/edualerta/
    │   │   │       ├── config/
    │   │   │       │   └── security/
    │   │   │       ├── controllers/
    │   │   │       ├── dtos/
    │   │   │       ├── exceptionHandler/
    │   │   │       ├── models/
    │   │   │       ├── repositorys/
    │   │   │       └── services/
    │   │   │           ├── interfaces/
    │   │   │           └── impls/
    │   │   └── resources/
    │   └── test/
    ├── src/main/resources/application.properties.example
    ├── mvnw
    ├── mvnw.cmd
    └── pom.xml
```

### Frontend

``` text
frontend/
├── index.html
├── alterar-senha.html
├── painel.html
├── usuarios.html
├── alunos.html
├── turmas.html
├── matriculas.html
├── frequencias.html
├── alertas.html
├── acompanhamento.html
├── relatorios.html
├── css/
│   └── style.css
└── js/
    ├── config.js
    ├── auth.js
    ├── layout.js
    ├── login.js
    ├── alterar-senha.js
    ├── gestao-escolar.js
    └── usuarios.js
```

A estrutura apresentada representa a organização documentada do projeto e pode receber novos diretórios conforme os módulos forem implementados.

------------------------------------------------------------------------

## 🚀 Executando o projeto

### Pré-requisitos

- Java 21.
- Maven (opcional, pois o projeto possui Maven Wrapper).
- Conta e projeto no Supabase.
- PostgreSQL disponibilizado pelo Supabase.
- Conta Gmail com autenticação em duas etapas e senha de aplicativo configurada, caso queira testar o envio de e-mails.
- Navegador atualizado.
- Servidor HTTP estático para disponibilizar o frontend.

### 1. Clone o repositório

``` bash
git clone URL_DO_REPOSITORIO
```

### 2. Execute o backend

Entre no diretório:

``` bash
cd EduAlerta/back-end/edualerta
```

Copie o arquivo de exemplo:

``` bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Preencha as propriedades com os dados da sua instalação.

Execute a aplicação no Linux:

``` bash
./mvnw spring-boot:run
```

No Windows:

``` powershell
.\mvnw.cmd spring-boot:run
```

O backend utiliza a porta `9000` na configuração atual.

A aplicação depende de uma configuração válida do PostgreSQL e do Supabase para iniciar corretamente. O Hibernate está configurado com `ddl-auto=validate`: as tabelas precisam existir no banco e corresponder ao mapeamento das entidades; a aplicação não cria nem atualiza automaticamente o schema.

### 3. Configure o frontend

Entre na pasta `frontend` e abra o projeto em um servidor HTTP estático.

Pode ser utilizada a extensão Live Server do VS Code/VSCodium ou outro servidor HTTP local.

Confira o arquivo:

``` text
frontend/js/config.js
```

Configure:

- `apiBaseUrl`: URL em que o backend Spring Boot está acessível.
- `supabaseUrl`: URL do projeto Supabase.
- `supabaseAnonKey`: chave pública de acesso ao Supabase.

A configuração local atual utiliza:

``` text
http://localhost:9000
```

Não é recomendado abrir as páginas diretamente pelo protocolo `file://`, pois a aplicação depende de requisições HTTP e da sessão do Supabase.

### 4. Acesse o sistema

Com o backend e o frontend em execução, acesse `index.html` pelo endereço HTTP fornecido pelo servidor estático.

A autenticação inicial depende de uma conta previamente provisionada no Supabase Auth e vinculada ao registro correspondente em `tb_usuario`.

------------------------------------------------------------------------

## 🧪 Testes e validação

O projeto contém atualmente um teste automatizado de contexto (`contextLoads`). Ele verifica a inicialização do contexto Spring, mas não constitui uma suíte automatizada de testes das regras de negócio.

Os módulos de fechamento semanal, frequência acumulada e alertas foram exercitados manualmente por requisições HTTP durante o desenvolvimento. Os logs incluídos no ZIP registram inicializações da aplicação e operações de desenvolvimento; eles não substituem uma suíte automatizada.

#### Cenários manuais validados para os alertas

Os testes foram realizados fechando semanas sucessivas e consultando `GET /alertas`. Os valores abaixo representam o percentual acumulado do mesmo aluno no mesmo período letivo:

| Faltas / aulas | Percentual | Resultado observado                                    |
|---------------:|-----------:|--------------------------------------------------------|
|        11 / 26 |     42,31% | `CRITICO`                                              |
|        11 / 36 |     30,56% | `CRITICO`; alerta existente atualizado, sem duplicação |
|        11 / 46 |     23,91% | `PREVENTIVO`; transição de crítico para preventivo     |
|        11 / 56 |     19,64% | Sem alerta; abaixo do limite preventivo                |
|        12 / 60 |     20,00% | `PREVENTIVO`; validação do limite inferior exato       |
|        17 / 68 |     25,00% | `CRITICO`; validação do limite crítico exato           |

Os testes confirmaram também que o alerta existente foi atualizado ao passar de `20,00%` para `25,00%`, mantendo o mesmo ID. Quando o percentual caiu para `19,64%`, a consulta retornou uma lista vazia; ao voltar a `20,00%`, um novo alerta foi criado.

O projeto ainda possui apenas o teste automatizado de contexto (`contextLoads`). Portanto, os cenários acima são validações manuais da API, não testes automatizados.

## 🧭 Próximas etapas do projeto

Com base no estado atual do desenvolvimento e no escopo funcional, as próximas etapas incluem:

1.  Integrar a tela de alertas do frontend ao endpoint `GET /alertas`.
2.  Implementar o cadastro e a associação de responsáveis, caso permaneçam no escopo da entrega.
3.  Implementar o registro de acompanhamentos e intervenções.
4.  Integrar o painel aos dados reais da API.
5.  Integrar as telas de acompanhamentos e relatórios aos módulos correspondentes.
6.  Ampliar os testes automatizados para regras de negócio e endpoints.
7.  Revisar a configuração de ambiente do frontend para separar a URL local da API da URL utilizada em publicação.

### Funcionalidades fora do escopo inicial

- Login e acesso de professores.
- Login de alunos.
- Login de responsáveis.
- Integração com WhatsApp.
- Envio automático de mensagens por WhatsApp.
- Aplicativo mobile nativo.

A entidade `ENCAMINHAMENTO` permanece prevista na modelagem como funcionalidade opcional, sujeita à disponibilidade de tempo e à priorização da entrega.

------------------------------------------------------------------------

## 📖 Sobre o projeto

O EduAlerta é um projeto acadêmico desenvolvido no contexto do curso de Análise e Desenvolvimento de Sistemas, com foco na aplicação prática de conceitos de engenharia de software, desenvolvimento web, modelagem de dados, APIs REST, autenticação e persistência de dados.

O projeto busca demonstrar como sistemas de informação podem apoiar a gestão escolar, facilitar o acompanhamento da frequência e contribuir para a identificação antecipada de possíveis situações de evasão.

**Desenvolvimento:** Em andamento.
