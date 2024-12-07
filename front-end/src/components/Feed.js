import React, { useState, useEffect } from 'react';

function Feed() {
  const [messages, setMessages] = useState([]); // Inicializa como um array vazio
  const [messageContent, setMessageContent] = useState('');
  const [error, setError] = useState(null);

  // Função para buscar mensagens do feed
  const fetchMessages = async () => {
    try {
      const response = await fetch('http://localhost:8080/feed', {
        method: 'GET',
        credentials: 'include', // Incluir cookies na requisição
      });

      if (response.ok) {
        const data = await response.json();
        console.log("data é:"+JSON.stringify(data));
        setMessages(data.messages || []); // Assegura que messages nunca será undefine
      } else {
        throw new Error('Erro ao buscar mensagens');
      }
    } catch (error) {
      setError(error.message);
    }
  };

  // Função para postar uma nova mensagem
  const postMessage = async () => {
    try {
      if (!messageContent.trim()) {
        alert("O conteúdo da mensagem não pode estar vazio.");
        return;
      }

      const response = await fetch('http://localhost:8080/feedSalve', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', // Incluir cookies na requisição
        body: JSON.stringify(messageContent),
      });

      if (response.ok) {
        alert("Mensagem salva com sucesso!");
        setMessageContent('');
        fetchMessages(); // Recarregar as mensagens após enviar
      } else {
        throw new Error('Erro ao salvar a mensagem');
      }
    } catch (error) {
      setError(error.message);
    }
  };

  // Efeito para buscar as mensagens assim que o componente for montado
  useEffect(() => {
    fetchMessages();
  }, []);

  return (
    <div>
      <h2>Feed</h2>
      
      {/* Formulário para enviar novas mensagens */}
      <div>
        <textarea
          value={messageContent}
          onChange={(e) => setMessageContent(e.target.value)}
          placeholder="Digite sua mensagem..."
        />
        <button onClick={postMessage}>Enviar</button>
      </div>

      {/* Exibindo mensagens */}
      <div>
        <h3>Mensagens</h3>
        {error && <p style={{ color: 'red' }}>{error}</p>}
           <ul>
              {messages.length === 0 ? (
                <li>Nenhuma mensagem encontrada.</li>
              ) : (
                messages.map((message, index) => (
                  <li key={index}>
                    <strong>{message.author.nome}</strong> <br />
                    <em>{new Date(message.timestamp).toLocaleString()}</em> <br />
                    {message.content}
                  </li>
                ))
              )}
            </ul>

      </div>
    </div>
  );
}

export default Feed;
