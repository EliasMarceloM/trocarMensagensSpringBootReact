import React, { useState } from 'react';

function Login() {
  // Estado para armazenar os valores do nome e senha
  const [nome, setNome] = useState('');
  const [senha, setSenha] = useState('');
  const [responseMessage, setResponseMessage] = useState(''); // Estado para armazenar a resposta do backend
  const [error, setError] = useState(''); // Estado para erros

  // Função para enviar os dados para o backend via POST
  const handleLogin = async (event) => {
    event.preventDefault(); // Impede o comportamento padrão de recarregar a página

    if (nome.trim() === '' || senha.trim() === '') {
      setError('Os campos nome e senha não podem estar vazios');
      return;
    }

    setError(''); // Reseta o erro se os campos estiverem válidos

    try {
      // Envia os dados de login como corpo da requisição para o back-end (POST)
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nome, senha }), // Envia um objeto {nome, senha} para o backend
        credentials: 'include',
    });

      // Verifica se a resposta foi bem-sucedida
      if (!response.ok) {
        throw new Error(`Erro ao fazer login: ${response.status}`);
      }

      // Lê a resposta como JSON
      const parsedResponse = await response.json();
      console.log('Login bem-sucedido:', parsedResponse);

      // Salva o token no cookie ou faz o que for necessário
      // Por exemplo, pode armazenar o token no localStorage ou em um cookie

      setResponseMessage(parsedResponse.message);  // Exibe a resposta de sucesso
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      setError('Erro ao fazer login');
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>
            Nome:
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)} // Atualiza o estado do nome
              placeholder="Digite seu nome"
            />
          </label>
        </div>
        <div>
          <label>
            Senha:
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)} // Atualiza o estado da senha
              placeholder="Digite sua senha"
            />
          </label>
        </div>
        <button type="submit">Entrar</button>
      </form>

      {/* Exibe a resposta ou erro */}
      {responseMessage && <p>Resposta do servidor: {responseMessage}</p>}
      {error && <p style={{ color: 'red' }}>Erro: {error}</p>}
    </div>
  );
}

export default Login;
