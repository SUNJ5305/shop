import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container } from '@mui/material';
import Register from './components/Register';
import Login from './components/Login';
import ProductList from './components/ProductList';
import Cart from './components/Cart';
import Order from './components/Order';

function App() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" style={{ flexGrow: 1 }}>
                        온라인 쇼핑몰
                    </Typography>
                    <Button color="inherit" onClick={() => navigate('/products')}>
                        상품 목록
                    </Button>
                    <Button color="inherit" onClick={() => navigate('/cart')}>
                        장바구니
                    </Button>
                    <Button color="inherit" onClick={() => navigate('/orders')}>
                        주문
                    </Button>
                    {localStorage.getItem('token') ? (
                        <Button color="inherit" onClick={handleLogout}>
                            로그아웃
                        </Button>
                    ) : (
                        <>
                            <Button color="inherit" onClick={() => navigate('/login')}>
                                로그인
                            </Button>
                            <Button color="inherit" onClick={() => navigate('/register')}>
                                회원가입
                            </Button>
                        </>
                    )}
                </Toolbar>
            </AppBar>
            <Container style={{ marginTop: '20px' }}>
                <Routes>
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/products" element={<ProductList />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route path="/orders" element={<Order />} />
                    <Route path="/" element={<ProductList />} />
                </Routes>
            </Container>
        </div>
    );
}

export default function AppWrapper() {
    return (
        <Router>
            <App />
        </Router>
    );
}