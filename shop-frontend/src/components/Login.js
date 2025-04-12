import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/api';
import { TextField, Button, Box, Typography, Paper } from '@mui/material';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await login(email, password);
            localStorage.setItem('token', response.data);
            alert('로그인 성공!');
            navigate('/products');
        } catch (error) {
            alert('로그인 실패: ' + (error.response?.data || error.message));
        }
    };

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
            <Paper elevation={3} style={{ padding: '20px', width: '400px' }}>
                <Typography variant="h5" align="center" gutterBottom>
                    로그인
                </Typography>
                <form onSubmit={handleLogin}>
                    <TextField
                        label="이메일"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="비밀번호"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: '20px' }}>
                        로그인
                    </Button>
                </form>
            </Paper>
        </Box>
    );
};

export default Login;