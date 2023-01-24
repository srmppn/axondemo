package com.example.axondemo.controller

import com.example.axondemo.api.CreateProductCommand
import com.example.axondemo.api.UpdateProductCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture


@RestController
class ProductController {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @PostMapping("/product")
    fun createProduct(@RequestBody request: CreateProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(CreateProductCommand(UUID.randomUUID(), request.name, request.description, request.price))
    }

    @PutMapping("/product/{productId}")
    fun updateProduct(@PathVariable("productId") productId: UUID,
                      @RequestBody request: UpdateProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(UpdateProductCommand(productId, request.name, request.description, request.price))
    }
}

