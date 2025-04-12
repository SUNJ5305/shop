import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container, Box } from '@mui/material';
import Register from './components/Register';
import Login from './components/Login';
import ProductList from './components/ProductList';
import Cart from './components/Cart';
import Order from './components/Order';
import Home from './components/Home';
import ProductDetail from './components/ProductDetail';

function App() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div>
            <AppBar position="static">
                <Toolbar sx={{ flexWrap: 'wrap' }}>
                    <Typography variant="h6" sx={{ flexGrow: 1, cursor: 'pointer' }} onClick={() => navigate('/')}>
                        온라인 쇼핑몰
                    </Typography>
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                        <Button color="inherit" onClick={() => navigate('/products')}>
                            상품 목록
                        </Button>
                        {localStorage.getItem('token') && (
                        <Button color="inherit" onClick={() => navigate('/cart')}>
                            장바구니
                        </Button>
                        )}
                        {localStorage.getItem('token') && (
                        <Button color="inherit" onClick={() => navigate('/orders')}>
                            주문
                        </Button>
                        )}
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
                    </Box>
                </Toolbar>
            </AppBar>
            <Container style={{ marginTop: '20px' }}>
                <Routes>
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/products" element={<ProductList />} />
                    <Route path="/product/:productId" element={<ProductDetail />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route path="/orders" element={<Order />} />
                    <Route path="/" element={<Home />} />
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