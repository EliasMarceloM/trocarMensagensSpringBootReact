import React, { useState } from 'react';

const Registro = () => {
  // Estado para armazenar os valores do nome e senha
  const [nome, setNome] = useState('');
  const [senha, setSenha] = useState('');
  const [responseMessage, setResponseMessage] = useState(''); // Estado para armazenar a resposta do backend
  const [error, setError] = useState(''); // Estado para erros

  // Função para enviar os dados para o backend via POST
  const handleSubmit = async (event) => {
    event.preventDefault(); // Impede o comportamento padrão de recarregar a página

    if (nome.trim() === '' || senha.trim() === '') {
      setError('Os campos nome e senha não podem estar vazios');
      return;
    }

    setError(''); // Reseta o erro se os campos estiverem válidos

    try {
      // Envia os dados como corpo da requisição para o back-end (POST)
      const response = await fetch('http://localhost:8080/registrar', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nome, senha }), // Envia um objeto {nome, senha} para o backend
        credentials: 'include',
      });

      // Verifica se a resposta tem sucesso (status 200)
      if (!response.ok) {
        throw new Error(`Erro ao enviar dados para o servidor: ${response.status}`);
      }

      // Lê a resposta como JSON
      const parsedResponse = await response.json();
      console.log('Resposta recebida:', parsedResponse);

      if (parsedResponse && parsedResponse.message) {
        // Exibe a mensagem de sucesso
        setResponseMessage(parsedResponse.message);
        
        // Exibe os detalhes do usuário, se necessário
        if (parsedResponse.user) {
          setResponseMessage(`Usuário criado: ${parsedResponse.user.nome} com a role: ${parsedResponse.user.role}`);
        }
      } else {
        setError('Resposta inválida do servidor');
      }

      // Limpa os campos de input após a resposta
      setNome('');
      setSenha('');
    } catch (error) {
      console.error('Erro ao enviar dados para o servidor:', error);
      setError('Erro ao enviar dados para o servidor');
    }
  };

  return (
    <div>
      <h1>Criar Usuário</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            Nome:
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)} // Atualiza o estado do nome
              placeholder="Digite o nome"
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
              placeholder="Digite a senha"
            />
          </label>
        </div>
        <button type="submit">Criar Usuário</button>
      </form>

      {/* Exibe a resposta ou erro */}
      {responseMessage && <p>Resposta do servidor: {responseMessage}</p>}
      {error && <p style={{ color: 'red' }}>Erro: {error}</p>}
    </div>
  );
};

export default Registro;
