import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import './App.css';
import Login from './components/Login';
import Registro from './components/Registro';
import Feed from './components/Feed';
import Home from './components/Home';  // Importe o componente Home

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <nav>
            <div className="menu">
              <h1>APP</h1>
              <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/login">Login</Link></li>
                <li><Link to="/registro">Registro</Link></li>
                <li><Link to="/feed">Feed</Link></li>
              </ul>
            </div>
          </nav>
          <div className="inputs">
            <Routes>
              <Route path="/" element={<Home />} />  {/* Definindo a rota para Home */}
              <Route path="/login" element={<Login />} />
              <Route path="/registro" element={<Registro />} />
              <Route path="/feed" element={<Feed />} />
            </Routes>
          </div>
        </header>
      </div>
    </Router>
  );
}

export default App;
