import React, { useState } from 'react';
import {
  Avatar,
  Button,
  TextField,
  Link,
  Paper,
  Box,
  Grid,
  Typography,
  IconButton,
  InputAdornment,
  Divider
} from '@mui/material';
import { Visibility, VisibilityOff, LockOutlined, Google } from '@mui/icons-material';
import { GoogleLogin } from '@react-oauth/google';
import CircularProgress from '@mui/material/CircularProgress';
import { useNavigate } from 'react-router-dom';
import { Link as RouterLink } from 'react-router-dom';

const placeholderLogo = 'https://via.placeholder.com/80x80?text=Logo';

function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleShowPassword = () => setShowPassword((show) => !show);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const response = await fetch('http://localhost:8080/resmgmtsys/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: identifier, password }),
      });
      const data = await response.json();
      if (response.ok && data.token) {
        localStorage.setItem('jwt', data.token);
        navigate('/account');
      } else {
        setError(data.error || 'Login failed. Please check your credentials.');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Grid container component="main" sx={{ height: '100vh' }}>
      <Grid item xs={12} sm={8} md={6} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.main', width: 80, height: 80 }} src={placeholderLogo} />
          <Typography component="h1" variant="h5" sx={{ mt: 2 }}>
            Sign in to ResMgmtSys
          </Typography>
          <Box component="form" onSubmit={handleLogin} sx={{ mt: 1, width: '100%' }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="identifier"
              label="Username, Email, or Phone"
              name="identifier"
              autoComplete="username"
              autoFocus
              value={identifier}
              onChange={(e) => setIdentifier(e.target.value)}
              disabled={loading}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={handleShowPassword} edge="end" aria-label="toggle password visibility">
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 1 }}>
              <Link href="#" variant="body2">
                Forgot password?
              </Link>
            </Box>
            {error && (
              <Typography color="error" sx={{ mt: 2 }}>
                {error}
              </Typography>
            )}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
              startIcon={loading ? <CircularProgress size={20} /> : null}
            >
              {loading ? 'Signing In...' : 'Sign In'}
            </Button>
            <Divider sx={{ my: 2 }}>or</Divider>
            <GoogleLogin
              onSuccess={credentialResponse => {
                // TODO: Handle Google login
              }}
              onError={() => {
                // TODO: Handle error
              }}
              width="100%"
            />
            <Box sx={{ mt: 2, textAlign: 'center' }}>
              <Link component={RouterLink} to="/register" variant="body2">
                {"Don't have an account? Sign Up"}
              </Link>
            </Box>
          </Box>
        </Box>
      </Grid>
      <Grid
        item
        xs={false}
        sm={4}
        md={6}
        sx={{
          backgroundColor: (theme) =>
            theme.palette.mode === 'light' ? theme.palette.grey[50] : theme.palette.grey[900],
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <Box sx={{ p: 4, maxWidth: 360, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 3 }}>
          <Typography variant="h6" gutterBottom>
            What our users say
          </Typography>
          <Typography variant="body1" sx={{ fontStyle: 'italic' }}>
            "ResMgmtSys made managing my reservations effortless. The intuitive interface and secure login give me peace of mind every time I book!"
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', mt: 2 }}>
            <Avatar sx={{ mr: 1 }}>A</Avatar>
            <Box>
              <Typography variant="subtitle2">Alex Johnson</Typography>
              <Typography variant="caption" color="text.secondary">Property Owner</Typography>
            </Box>
          </Box>
        </Box>
      </Grid>
    </Grid>
  );
}

export default LoginPage; 