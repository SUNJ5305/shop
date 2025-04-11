package sunjin.com.shop.service;

import sunjin.com.shop.domain.Product;
import sunjin.com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, String description, int price, int stock, Integer categoryId) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 재고 감소
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(int productId) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId);
        }
        productRepository.deleteById(productId);
    }
}