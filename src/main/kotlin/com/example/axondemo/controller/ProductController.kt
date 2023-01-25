package com.example.axondemo.controller

import com.example.axondemo.api.CreateProductCommand
import com.example.axondemo.api.DeleteProductCommand
import com.example.axondemo.api.UpdateProductCommand
import com.example.axondemo.api.query.FetchAllProducts
import com.example.axondemo.api.query.FetchProductById
import com.example.axondemo.domain.Product
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture


@RestController
class ProductController {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var queryGateway: QueryGateway

    @PostMapping("/product")
    fun createProduct(@RequestBody request: CreateProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(CreateProductCommand(ObjectId.get().toHexString(), request.name, request.description, request.price))
    }

    @PutMapping("/product/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: String,
        @RequestBody request: UpdateProductRequest
    ): CompletableFuture<String> {
        return commandGateway.send<String>(UpdateProductCommand(productId, request.name, request.description, request.price))
    }

    @DeleteMapping("/product/{productId}")
    fun updateProduct(@PathVariable("productId") productId: String): CompletableFuture<String> {
        return commandGateway.send<String>(DeleteProductCommand(productId))
    }

    @GetMapping("/products")
    fun getAllProducts(): CompletableFuture<List<Product>> {
        return queryGateway.query(FetchAllProducts(), ResponseTypes.multipleInstancesOf(Product::class.java))
    }

    @GetMapping("/product/{productId}")
    fun getProductById(@PathVariable("productId") productId: String): CompletableFuture<Product> {
        return queryGateway.query(FetchProductById(productId), ResponseTypes.instanceOf(Product::class.java))
    }

    @GetMapping("/products/sub")
    fun allProducts(): Publisher<List<Product>> {
        val queryResult =
            queryGateway.subscriptionQuery(
                FetchAllProducts(),
                ResponseTypes.multipleInstancesOf(Product::class.java),
                ResponseTypes.multipleInstancesOf(Product::class.java)
            )

        return queryResult
            .initialResult()
            .concatWith(queryResult.updates())
            .scan { current, updates ->
                current.plus(updates)
                    .map { old -> updates.find { new -> old.productId == new.productId } }
                    .distinct()
            }
            .map { list -> list.filter { !it.isDeleted } }
            .doFinally { queryResult.close() }
    }

    @GetMapping("/product/{productId}/sub")
    fun productById(@PathVariable("productId") productId: String): Publisher<Product> {
        val queryResult =
            queryGateway.subscriptionQuery(
                FetchProductById(productId),
                ResponseTypes.instanceOf(Product::class.java),
                ResponseTypes.instanceOf(Product::class.java)
            )

        return queryResult
            .initialResult()
            .concatWith(queryResult.updates())
            .filter { it.productId == productId }
            .doFinally { queryResult.close() }
    }
}