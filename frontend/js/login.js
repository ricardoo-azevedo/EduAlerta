const formulario = document.getElementById("formLogin");
const mensagem = document.getElementById("mensagemLogin");
const botao = document.getElementById("entrar");

formulario.addEventListener("submit", async event => {
    event.preventDefault();
    mensagem.textContent = "";

    if (!clienteAuth || !config.usuarioAtualPath) {
        mensagem.textContent = "Configure o Supabase e a rota de verificação da secretaria em js/config.js.";
        return;
    }

    botao.disabled = true;
    botao.textContent = "Verificando acesso...";

    try {
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("senha").value;

        const { error } = await clienteAuth.auth.signInWithPassword({
            email,
            password
        });

        if (error) {
            throw new Error("E-mail ou senha inválidos.");
        }

        if (!await verificarSecretaria()) {
            await clienteAuth.auth.signOut();

            throw new Error(
                "Acesso não autorizado. Verifique seu perfil ou a troca obrigatória de senha."
            );
        }

        window.location.replace("pages/painel.html");
    } catch (erro) {
        mensagem.textContent = erro.message || "Não foi possível entrar.";
    } finally {
        botao.disabled = false;
        botao.innerHTML = '<i class="bi bi-box-arrow-in-right me-2"></i>Entrar no sistema';
    }
});