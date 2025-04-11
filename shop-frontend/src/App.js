import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Register from './components/Register';
import Login from './components/Login';
import ProductList from './components/ProductList';
import Cart from './components/Cart';
import Order from './components/Order';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/products" element={<ProductList />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/orders" element={<Order />} />
          <Route path="/" element={<ProductList />} />
        </Routes>
      </Router>
  );
}

export default App;
