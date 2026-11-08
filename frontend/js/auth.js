const config = window.EDUALERTA_CONFIG;

const clienteAuth = config.supabaseUrl && config.supabaseAnonKey && window.supabase
    ? window.supabase.createClient(config.supabaseUrl, config.supabaseAnonKey)
    : null;

async function verificarSecretaria() {
    if (!clienteAuth || !config.usuarioAtualPath) return false;

    const { data, error } = await clienteAuth.auth.getUser();

    if (error || !data.user) return false;

    const { data: sessao } = await clienteAuth.auth.getSession();
    const token = sessao.session?.access_token;

    if (!token) return false;

    const resposta = await fetch(
        config.apiBaseUrl.replace(/\/$/, "") + config.usuarioAtualPath,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!resposta.ok) return false;

    const usuario = await resposta.json();

    return usuario.perfil === "SECRETARIA" &&
        usuario.ativo === true &&
        usuario.alterarSenhaObrigatorio !== true;
}

async function protegerPagina() {
    try {
        if (!await verificarSecretaria()) {
            window.location.replace("../index.html");
            return false;
        }

        document.body.hidden = false;
        return true;
    } catch {
        window.location.replace("../index.html");
        return false;
    }
}

async function requisicaoAPI(caminho, opcoes = {}) {
    if (!clienteAuth) {
        throw new Error("Supabase Auth não configurado.");
    }

    const { data } = await clienteAuth.auth.getSession();
    const token = data.session?.access_token;

    if (!token) {
        throw new Error("Sessão expirada.");
    }

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
        throw new Error(`Erro da API: ${resposta.status}`);
    }

    return resposta.status === 204 ? null : resposta.json();
}

async function sair() {
    if (clienteAuth) {
        await clienteAuth.auth.signOut();
    }

    window.location.replace("../index.html");
}