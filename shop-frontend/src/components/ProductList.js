import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProducts, addToCart } from '../api/api';
import {
    Card,
    CardContent,
    CardActions,
    Typography,
    Button,
    Grid,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem, Box, CardMedia,
} from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import { getProductImage } from '../utils/imageMapping';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [category, setCategory] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await getProducts();
                setProducts(response.data);
                setFilteredProducts(response.data);
            } catch (error) {
                alert('상품 목록 조회 실패: ' + (error.response?.data || error.message));
            }
        };
        fetchProducts();
    }, []);

    useEffect(() => {
        let filtered = products;
        if (searchTerm) {
            filtered = filtered.filter((product) =>
                product.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }
        if (category) {
            filtered = filtered.filter((product) => product.categoryId === parseInt(category));
        }
        setFilteredProducts(filtered);
    }, [searchTerm, category, products]);

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
            <Box display="flex" mb={3}>
                <TextField
                    label="검색"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    sx={{ mr: 2, width: 300 }}
                />
                <FormControl sx={{ width: 200 }}>
                    <InputLabel>카테고리</InputLabel>
                    <Select value={category} onChange={(e) => setCategory(e.target.value)}>
                        <MenuItem value="">모두</MenuItem>
                        <MenuItem value="1">카테고리 1</MenuItem>
                        <MenuItem value="2">카테고리 2</MenuItem>
                        {/* 실제 카테고리 데이터로 대체 필요 */}
                    </Select>
                </FormControl>
            </Box>
            <Grid container spacing={3}>
                {filteredProducts.map((product) => (
                    <Grid item xs={12} sm={6} md={4} key={product.productId}>
                        <Card>
                            <CardMedia
                                component="img"
                                height="140"
                                image={getProductImage(product.productId)}
                                alt={product.name}
                            />
                            <CardContent>
                                <Typography variant="h6">{product.name}</Typography>
                                <Typography color="textSecondary">{product.description}</Typography>
                                <Typography variant="h6" color="primary">
                                    {product.price}원
                                </Typography>
                                <Typography color="textSecondary">재고: {product.stock}</Typography>
                            </CardContent>
                            <CardActions>
                                {localStorage.getItem('token') ? (
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        startIcon={<AddShoppingCartIcon />}
                                        onClick={() => handleAddToCart(product.productId)}
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
                                <Button
                                    variant="outlined"
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

export default ProductList;