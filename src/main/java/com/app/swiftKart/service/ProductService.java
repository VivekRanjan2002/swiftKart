package com.app.swiftKart.service;

import com.app.swiftKart.dto.ProductRequest;
import com.app.swiftKart.dto.ProductResponse;
import com.app.swiftKart.model.Product;
import com.app.swiftKart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public String  createProduct(ProductRequest productRequest){
        Product product= new Product();
        updateProductFromRequest(product,productRequest);
        productRepository.save(product);
        return "Product added successfully";
    }
    public List<ProductResponse> fetchProducts(){
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .toList();
    }
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
                .map(existingProduct->{
                   updateProductFromRequest(existingProduct,productRequest);
                    Product savedProduct=productRepository.save(existingProduct);
                   return mapToProductResponse(savedProduct);
                });


    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse= new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setCategory(product.getCategory());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setActive(product.getActive());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setPrice(product.getPrice());
        return productResponse;

    }
    private void  updateProductFromRequest(Product product, ProductRequest productRequest){
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        product.setPrice(  productRequest.getPrice());
        product.setStockQuantity(Integer.parseInt(productRequest.getStockQuantity()));
        product.setImageUrl(productRequest.getImageUrl());

    }

    public ProductResponse getProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
            .map(this::mapToProductResponse)
                .orElse(null);

    }

    public boolean deleteProduct(Long id) {
       return  productRepository.findById(id)
                .map(product -> {
                    if(!product.getActive()) return false;
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                })
                 .orElse(false);
    }
    public List<ProductResponse> searchProducts(String Keyword){
        return productRepository.searchProducts(Keyword).stream()
                .map(this:: mapToProductResponse)
                .collect(Collectors.toList());
    }

}
