package sunjin.com.shop.controller;

import sunjin.com.shop.domain.Product;
import sunjin.com.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "상품 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "상품 이름") @RequestParam String name,
            @Parameter(description = "상품 설명") @RequestParam String description,
            @Parameter(description = "가격") @RequestParam int price,
            @Parameter(description = "재고") @RequestParam int stock,
            @Parameter(description = "카테고리 ID", required = false) @RequestParam(required = false) Integer categoryId) {
        Product product = productService.createProduct(name, description, price, stock, categoryId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "ID로 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 정보 반환")
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "상품 ID") @PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 목록 반환")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "상품 ID") @PathVariable int productId,
            @Parameter(description = "상품 이름") @RequestParam String name,
            @Parameter(description = "상품 설명") @RequestParam String description,
            @Parameter(description = "가격") @RequestParam int price,
            @Parameter(description = "재고") @RequestParam int stock,
            @Parameter(description = "카테고리 ID", required = false) @RequestParam(required = false) Integer categoryId) {
        Product product = productService.getProductById(productId);
        if (product == null) return ResponseEntity.notFound().build();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "ID로 상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "상품 ID") @PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) return ResponseEntity.notFound().build();
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}