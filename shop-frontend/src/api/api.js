import axios from "axios";

const API = axios.create({
    baseURL: 'http://localhost:8080'
});

// 요청 인터셉터: 토큰 추가
API.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config
})

export const register = (email, password, name) =>
    API.post('/api/users/register', null, { params: { email, password, name }});

export const login = (email, password) =>
    API.post('/api/users/login', null, { params: { email, password }});

export const getCurrentUser = () => API.get('/api/users/me');

export const createProduct = (name, description, price, stock, categoryId) =>
    API.post('/api/products', null, { params: { name, description, price, stock, categoryId } });

export const getProducts = () => API.get('/api/products');

export const addToCart = (productId, quantity) =>
    API.post('api/cart-items', null, { params: {productId, quantity}});

export const getCartItems = () => API.get('/api/cart-items/user');

export const addAddress = (addressLine, city, postalCode, defaultAddress) =>
    API.post('/api/addresses', null, { params: {addressLine, city, postalCode, defaultAddress}});

export const getAddresses = () => API.get('/api/addresses/user');

export const createOrder = (addressId) =>
    API.post('/api/orders', null, { params: { addressId }});

export const getOrders = () => API.get('/api/orders/user');

export const deleteCartItem = (cartItemId) =>
    API.delete(`/api/cart-items/${cartItemId}`);

export const updateCartItem = (cartItemId, quantity) =>
    API.put(`/api/cart-items/${cartItemId}`, null, { params: { quantity } });