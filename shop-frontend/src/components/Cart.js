import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCartItems, deleteCartItem, updateCartItem } from '../api/api';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Button,
    Typography,
    TextField,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const Cart = () => {
    const [cartItems, setCartItems] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const response = await getCartItems();
                setCartItems(response.data);
            } catch (error) {
                if (error.response?.status === 401) {
                    alert('로그인이 필요합니다.');
                    navigate('/login');
                } else {
                    alert('장바구니 조회 실패: ' + (error.response?.data || error.message));
                }
            }
        };
        fetchCartItems();
    }, []);

    const handleDelete = async (cartItemId) => {
        try {
            await deleteCartItem(cartItemId);
            setCartItems(cartItems.filter((item) => item.cartItemId !== cartItemId));
            alert('장바구니 항목이 삭제되었습니다.');
        } catch (error) {
            alert('삭제 실패: ' + (error.response?.data || error.message));
        }
    };

    const handleUpdateQuantity = async (cartItemId, newQuantity) => {
        try {
            const updatedItem = await updateCartItem(cartItemId, newQuantity);
            setCartItems(
                cartItems.map((item) =>
                    item.cartItemId === cartItemId ? { ...item, quantity: updatedItem.data.quantity } : item
                )
            );
            alert('수량이 업데이트되었습니다.');
        } catch (error) {
            alert('수량 업데이트 실패: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div>
            <Typography variant="h4" gutterBottom>
                장바구니
            </Typography>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>상품 ID</TableCell>
                            <TableCell>수량</TableCell>
                            <TableCell>작업</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {cartItems.map((item) => (
                            <TableRow key={item.cartItemId}>
                                <TableCell>{item.productId}</TableCell>
                                <TableCell>
                                    <TextField
                                        type="number"
                                        value={item.quantity}
                                        onChange={(e) => {
                                            const newQuantity = Math.max(1, parseInt(e.target.value));
                                            setCartItems(
                                                cartItems.map((cartItem) =>
                                                    cartItem.cartItemId === item.cartItemId
                                                        ? { ...cartItem, quantity: newQuantity }
                                                        : cartItem
                                                )
                                            );
                                            handleUpdateQuantity(item.cartItemId, newQuantity);
                                        }}
                                        inputProps={{ min: 1 }}
                                        sx={{ width: 80 }}
                                    />
                                </TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        color="error"
                                        startIcon={<DeleteIcon />}
                                        onClick={() => handleDelete(item.cartItemId)}
                                    >
                                        삭제
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default Cart;