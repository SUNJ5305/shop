import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProducts, addToCart } from '../api/api';
import { Card, CardContent, CardActions, Typography, Button, Grid } from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await getProducts();
                setProducts(response.data);
            } catch (error) {
                alert('상품 목록 조회 실패: ' + (error.response?.data || error.message));
            }
        };
        fetchProducts();
    }, []);

    const handleAddToCart = async (productId) => {
        try {
            await addToCart(productId, 1);
            alert('장바구니에 추가되었습니다!');
        } catch (error) {
            if (error.response?.status === 401) {
                alert('로그인이 필요합니다.');
                navigate('/login');
            } else {
                alert('장바구니 추가 실패: ' + (error.response?.data || error.message));
            }
        }
    };

    return (
        <div>
            <Typography variant="h4" gutterBottom>
                상품 목록
            </Typography>
            <Grid container spacing={3}>
                {products.map((product) => (
                    <Grid item xs={12} sm={6} md={4} key={product.productId}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6">{product.name}</Typography>
                                <Typography color="textSecondary">{product.description}</Typography>
                                <Typography variant="h6" color="primary">
                                    {product.price}원
                                </Typography>
                                <Typography color="textSecondary">재고: {product.stock}</Typography>
                            </CardContent>
                            <CardActions>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    startIcon={<AddShoppingCartIcon />}
                                    onClick={() => handleAddToCart(product.productId)}
                                >
                                    장바구니에 추가
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </div>
    );
};

export default ProductList;