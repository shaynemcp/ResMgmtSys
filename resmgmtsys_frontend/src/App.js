import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link as RouterLink } from 'react-router-dom';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';
import AccountPage from './AccountPage';
import HomePage from './HomePage';
import { AppBar, Toolbar, Box } from '@mui/material';

function App() {
  return (
    <Router>
      <AppBar position="static" color="default" elevation={1}>
        <Toolbar>
          <Box sx={{ flexGrow: 1 }}>
            <RouterLink to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
              <img src="/logo192.png" alt="Logo" style={{ height: 40, marginRight: 8 }} />
              <span style={{ fontWeight: 600, color: '#1976d2', fontSize: 20 }}>ResMgmtSys</span>
            </RouterLink>
          </Box>
        </Toolbar>
      </AppBar>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/account" element={<AccountPage />} />
      </Routes>
    </Router>
  );
}

export default App; 