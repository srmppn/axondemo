package com.example.axondemo.query

import com.example.axondemo.api.query.FetchAllProducts
import com.example.axondemo.api.query.FetchProductById
import com.example.axondemo.domain.Product
import com.example.axondemo.repository.ProductRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ProductQuery {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @QueryHandler
    fun handle(query: FetchAllProducts): CompletableFuture<List<Product>> =
        productRepository.findAll()
            .collectList()
            .toFuture()

    @QueryHandler
    fun handle(query: FetchProductById): CompletableFuture<Product> =
        productRepository.findById(query.productId)
            .toFuture()
}