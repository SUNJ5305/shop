package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.dto.CreateProductRequest;
import sunjin.com.shop.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(CreateProductRequest createProductRequest) {
        Product product = new Product();
        setProductDetails(product, createProductRequest);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(int productId) {
        Product product = getProductById(productId);
        checkIfProductExists(product, productId);
        productRepository.deleteById(productId);
    }

    private void setProductDetails(Product product, CreateProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategoryId(request.getCategoryId());
    }

    private void checkIfProductExists(Product product, int productId) {
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId);
        }
    }
}