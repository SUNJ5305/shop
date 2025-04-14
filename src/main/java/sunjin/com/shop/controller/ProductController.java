package sunjin.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sunjin.com.shop.domain.Product;
import sunjin.com.shop.dto.CreateProductRequest;
import sunjin.com.shop.dto.UpdateProductRequest;
import sunjin.com.shop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * ProductController 생성자
     * @param productService 상품 서비스
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 새로운 상품을 생성한다.
     * @param request 상품 생성 요청 데이터
     * @return 생성된 상품 객체
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@ModelAttribute CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 ID로 상품을 조회한다.
     * @param productId 상품 ID
     * @return 상품 객체, 없으면 404 응답
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /**
     * 모든 상품 목록을 조회한다.
     * @return 상품 리스트
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 정보를 업데이트한다.
     * @param productId 상품 ID
     * @param request 상품 업데이트 요청 데이터
     * @return 업데이트된 상품 객체
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int productId,
            @ModelAttribute UpdateProductRequest request
    ) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        updateProductDetails(product, request);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * 상품을 삭제한다.
     * @param productId 상품 ID
     * @return 성공 시 200 응답
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    private void updateProductDetails(Product product, UpdateProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategoryId(request.getCategoryId());
    }

}