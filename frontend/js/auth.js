
const config = window.EDUALERTA_CONFIG;

const clienteAuth = config.supabaseUrl &&
  config.supabaseAnonKey &&
  window.supabase
  ? window.supabase.createClient(
    config.supabaseUrl,
    config.supabaseAnonKey
  )
  : null;

window.usuarioEduAlerta = null;

async function obterToken() {
  if (!clienteAuth) {
    throw new Error("Supabase Auth não configurado.");
  }

  const { data, error } = await clienteAuth.auth.getSession();

  if (error) {
    throw new Error("Não foi possível recuperar a sessão.");
  }

  const token = data.session?.access_token;

  if (!token) {
    throw new Error("Sessão expirada. Entre novamente.");
  }

  return token;
}

async function requisicaoAPI(caminho, opcoes = {}) {
  const token = await obterToken();

  const resposta = await fetch(
    config.apiBaseUrl.replace(/\/$/, "") + caminho,
    {
      ...opcoes,
      headers: {
        ...(opcoes.headers || {}),
        Authorization: `Bearer ${token}`
      }
    }
  );

  if (!resposta.ok) {
    let mensagem = `Erro na comunicação com a API (${resposta.status}).`;

    try {
      const erro = await resposta.json();

      mensagem =
        erro.detail ||
        erro.message ||
        erro.mensagem ||
        mensagem;
    } catch {
      // Mantém a mensagem padrão quando a API não retorna JSON.
    }

    throw new Error(mensagem);
  }

  if (resposta.status === 204) {
    return null;
  }

  const texto = await resposta.text();
  return texto ? JSON.parse(texto) : null;
}

async function obterUsuarioAtual() {
  return requisicaoAPI(config.usuarioAtualPath);
}

function perfilPermitido(usuario) {
  return usuario &&
    (usuario.perfil === "ADM" || usuario.perfil === "SECRETARIA") &&
    usuario.ativo === true;
}

async function encerrarSessaoERedirecionar() {
  if (clienteAuth) {
    await clienteAuth.auth.signOut();
  }

  window.usuarioEduAlerta = null;
  window.location.replace("index.html");
}

async function protegerPagina() {
  try {
    if (!clienteAuth) {
      window.location.replace("index.html");
      return false;
    }

    const usuario = await obterUsuarioAtual();

    if (!perfilPermitido(usuario)) {
      await encerrarSessaoERedirecionar();
      return false;
    }

    if (usuario.alterarSenhaObrigatorio) {
      window.location.replace("alterar-senha.html");
      return false;
    }

    window.usuarioEduAlerta = usuario;
    document.body.hidden = false;

    return true;

  } catch {
    await encerrarSessaoERedirecionar();
    return false;
  }
}

async function protegerTrocaSenha() {
  try {
    if (!clienteAuth) {
      window.location.replace("index.html");
      return false;
    }

    const usuario = await obterUsuarioAtual();

    if (!perfilPermitido(usuario)) {
      await encerrarSessaoERedirecionar();
      return false;
    }

    if (!usuario.alterarSenhaObrigatorio) {
      window.location.replace("painel.html");
      return false;
    }

    window.usuarioEduAlerta = usuario;
    document.body.hidden = false;

    return true;

  } catch {
    await encerrarSessaoERedirecionar();
    return false;
  }
}

async function sair() {
  if (clienteAuth) {
    await clienteAuth.auth.signOut();
  }

  window.usuarioEduAlerta = null;
  window.location.replace("index.html");
}

