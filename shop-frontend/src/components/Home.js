import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProducts } from '../api/api';
import { Typography, Grid, Card, CardContent, CardMedia, CardActions, Button } from '@mui/material';

const Home = () => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await getProducts();
                setProducts(response.data.slice(0, 3)); // 추천 상품으로 3개만 표시
            } catch (error) {
                console.error('추천 상품 조회 실패:', error);
            }
        };
        fetchProducts();
    }, []);

    return (
        <div>
            {/* 배너 */}
            <div style={{ marginBottom: '20px' }}>
                <img
                    src="https://via.placeholder.com/1200x300?text=Welcome+to+Our+Shop"
                    alt="배너"
                    style={{ width: '100%', height: 'auto' }}
                />
            </div>

            {/* 추천 상품 */}
            <Typography variant="h4" gutterBottom>
                추천 상품
            </Typography>
            <Grid container spacing={3}>
                {products.map((product) => (
                    <Grid item xs={12} sm={6} md={4} key={product.productId}>
                        <Card>
                            <CardMedia
                                component="img"
                                height="140"
                                image="https://via.placeholder.com/150?text=Product+Image"
                                alt={product.name}
                            />
                            <CardContent>
                                <Typography variant="h6">{product.name}</Typography>
                                <Typography color="textSecondary">{product.description}</Typography>
                                <Typography variant="h6" color="primary">
                                    {product.price}원
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={() => navigate(`/product/${product.productId}`)}
                                >
                                    상세 보기
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </div>
    );
};

export default Home;