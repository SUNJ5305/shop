package sunjin.com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * ProductService 생성자
     * @param productRepository 상품 리포지토리
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 새로운 상품을 생성한다.
     * @param name 상품 이름
     * @param description 상품 설명
     * @param price 상품 가격
     * @param stock 상품 재고
     * @param categoryId 카테고리 ID
     * @return 생성된 상품 객체
     */
    public Product createProduct(String name, String description, int price, int stock, Integer categoryId) {
        Product product = new Product();
        setProductDetails(product, name, description, price, stock, categoryId);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    /**
     * 상품 ID로 상품을 조회한다.
     * @param productId 상품 ID
     * @return 상품 객체, 없으면 null
     */
    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    /**
     * 모든 상품 목록을 조회한다.
     * @return 상품 리스트
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품 정보를 업데이트한다.
     * @param product 업데이트할 상품 객체
     * @return 업데이트된 상품 객체
     */
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 상품을 삭제한다.
     * @param productId 상품 ID
     * @throws IllegalArgumentException 상품이 존재하지 않을 경우
     */
    public void deleteProduct(int productId) {
        Product product = getProductById(productId);
        checkIfProductExists(product, productId);
        productRepository.deleteById(productId);
    }

    private void setProductDetails(Product product, String name, String description, int price, int stock, Integer categoryId) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
    }

    private void checkIfProductExists(Product product, int productId) {
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId);
        }
    }
}