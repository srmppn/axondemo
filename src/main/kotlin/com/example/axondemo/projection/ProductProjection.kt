package com.example.axondemo.projection

import com.example.axondemo.api.ProductCreatedEvent
import com.example.axondemo.api.ProductDeletedEvent
import com.example.axondemo.api.ProductUpdatedEvent
import com.example.axondemo.api.query.FetchAllProducts
import com.example.axondemo.api.query.FetchProductById
import com.example.axondemo.domain.Product
import com.example.axondemo.repository.ProductRepository
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Component

@Component
@EnableAutoConfiguration
class ProductProjection() {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var queryUpdateEmitter: QueryUpdateEmitter

    fun emitter(product: Product) {
        queryUpdateEmitter.emit(FetchAllProducts::class.java, { true }, listOf(product))
        queryUpdateEmitter.emit(FetchProductById::class.java, { it.productId == product.productId }, product)
    }

    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val product = Product(event.productId, event.name, event.description, event.price)

        productRepository.save(product)
            .doOnNext { emitter(it) }
            .block()
    }

    @EventHandler
    fun on(event: ProductUpdatedEvent) {
        productRepository.findById(event.productId.toString())
            .map { it.copy(name = event.name, description = event.description, price = event.price) }
            .flatMap { productRepository.save(it) }
            .doOnNext { emitter(it) }
            .block()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        productRepository.findById(event.productId)
            .map { it.copy(isDeleted = true) }
            .flatMap { productRepository.save(it) }
            .doOnNext { emitter(it) }
            .flatMap { productRepository.delete(it) }
            .block()
    }
}