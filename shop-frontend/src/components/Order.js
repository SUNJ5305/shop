import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { addAddress, getAddresses, createOrder, getOrders } from '../api/api';
import {
    TextField,
    Button,
    Box,
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Checkbox,
} from '@mui/material';

const Order = () => {
    const [addresses, setAddresses] = useState([]);
    const [orders, setOrders] = useState([]);
    const [addressLine, setAddressLine] = useState('');
    const [city, setCity] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [defaultAddress, setDefaultAddress] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const addressResponse = await getAddresses();
                setAddresses(addressResponse.data);
                const orderResponse = await getOrders();
                setOrders(orderResponse.data);
            } catch (error) {
                if (error.response?.status === 401) {
                    alert('로그인이 필요합니다.');
                    navigate('/login');
                } else {
                    alert('데이터 조회 실패: ' + (error.response?.data || error.message));
                }
            }
        };
        fetchData();
    }, []);

    const handleAddAddress = async (e) => {
        e.preventDefault();
        try {
            const response = await addAddress(addressLine, city, postalCode, defaultAddress);
            setAddresses([...addresses, response.data]);
            alert('주소가 추가되었습니다.');
        } catch (error) {
            alert('주소 추가 실패: ' + (error.response?.data || error.message));
        }
    };

    const handleCreateOrder = async (addressId) => {
        try {
            const response = await createOrder(addressId);
            setOrders([...orders, response.data]);
            alert('주문이 생성되었습니다.');
        } catch (error) {
            alert('주문 생성 실패: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div>
            <Typography variant="h4" gutterBottom>
                주문
            </Typography>

            <Paper elevation={3} style={{ padding: '20px', marginBottom: '20px' }}>
                <Typography variant="h6" gutterBottom>
                    주소 추가
                </Typography>
                <form onSubmit={handleAddAddress}>
                    <TextField
                        label="주소"
                        value={addressLine}
                        onChange={(e) => setAddressLine(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="도시"
                        value={city}
                        onChange={(e) => setCity(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="우편번호"
                        value={postalCode}
                        onChange={(e) => setPostalCode(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <Box display="flex" alignItems="center">
                        <Checkbox
                            checked={defaultAddress}
                            onChange={(e) => setDefaultAddress(e.target.checked)}
                        />
                        <Typography>기본 주소로 설정</Typography>
                    </Box>
                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        주소 추가
                    </Button>
                </form>
            </Paper>

            <Typography variant="h6" gutterBottom>
                주소 목록
            </Typography>
            <TableContainer component={Paper} style={{ marginBottom: '20px' }}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>주소</TableCell>
                            <TableCell>도시</TableCell>
                            <TableCell>우편번호</TableCell>
                            <TableCell>작업</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {addresses.map((address) => (
                            <TableRow key={address.addressId}>
                                <TableCell>{address.addressLine}</TableCell>
                                <TableCell>{address.city}</TableCell>
                                <TableCell>{address.postalCode}</TableCell>
                                <TableCell>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={() => handleCreateOrder(address.addressId)}
                                    >
                                        이 주소로 주문
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Typography variant="h6" gutterBottom>
                주문 목록
            </Typography>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>주문 ID</TableCell>
                            <TableCell>총액</TableCell>
                            <TableCell>상태</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {orders.map((order) => (
                            <TableRow key={order.orderId}>
                                <TableCell>{order.orderId}</TableCell>
                                <TableCell>{order.totalAmount}원</TableCell>
                                <TableCell>{order.status}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default Order;