package com.example.axondemo.projection

import com.example.axondemo.api.ProductCreatedEvent
import com.example.axondemo.api.ProductDeletedEvent
import com.example.axondemo.api.ProductUpdatedEvent
import com.example.axondemo.domain.Product
import com.example.axondemo.repository.ProductRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Component

@Component
@EnableAutoConfiguration
class ProductProjection() {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val product = Product(event.productId.toString(), event.name, event.description, event.price)

        productRepository.save(product).block()
    }

    @EventHandler
    fun on(event: ProductUpdatedEvent) {
        productRepository.findById(event.productId.toString())
            .map { it.copy(name = event.name, description = event.description, price = event.price) }
            .flatMap { productRepository.save(it) }
            .block()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        productRepository.deleteById(event.productId.toString()).block()
    }
}