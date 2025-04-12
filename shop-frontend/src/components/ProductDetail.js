import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getProducts, addToCart } from '../api/api';
import {
    Typography,
    Card,
    CardContent,
    CardMedia,
    Button,
    Box,
    Divider,
    TextField,
} from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';

const ProductDetail = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await getProducts();
                const foundProduct = response.data.find((p) => p.productId === parseInt(productId));
                setProduct(foundProduct);
            } catch (error) {
                console.error('상품 조회 실패:', error);
            }
        };
        fetchProduct();
    }, [productId]);

    const handleAddToCart = async () => {
        try {
            await addToCart(product.productId, quantity);
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

    if (!product) return <Typography>상품을 찾을 수 없습니다.</Typography>;

    return (
        <Box>
            <Card>
                <Box display="flex">
                    <CardMedia
                        component="img"
                        sx={{ width: 300, height: 300 }}
                        image="https://via.placeholder.com/300?text=Product+Image"
                        alt={product.name}
                    />
                    <CardContent sx={{ flex: 1 }}>
                        <Typography variant="h4">{product.name}</Typography>
                        <Typography variant="h6" color="primary">
                            {product.price}원
                        </Typography>
                        <Typography color="textSecondary">재고: {product.stock}</Typography>
                        <Typography paragraph>{product.description}</Typography>
                        <Box display="flex" alignItems="center" mb={2}>
                            <TextField
                                label="수량"
                                type="number"
                                value={quantity}
                                onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value)))}
                                inputProps={{ min: 1, max: product.stock }}
                                sx={{ width: 100, mr: 2 }}
                            />
                            {localStorage.getItem('token') ? (
                                <Button
                                    variant="contained"
                                    color="primary"
                                    startIcon={<AddShoppingCartIcon />}
                                    onClick={handleAddToCart}
                                >
                                    장바구니에 추가
                                </Button>
                            ) : (
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={() => {
                                        alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.');
                                        navigate('/login');
                                    }}
                                >
                                    로그인 후 장바구니 추가
                                </Button>
                            )}
                        </Box>
                    </CardContent>
                </Box>
            </Card>

            <Divider sx={{ my: 3 }} />

            <Typography variant="h5" gutterBottom>
                리뷰
            </Typography>
            <Box>
                <Typography color="textSecondary">아직 리뷰가 없습니다.</Typography>
                {/* 리뷰 기능은 나중에 추가 가능 */}
            </Box>
        </Box>
    );
};

export default ProductDetail;