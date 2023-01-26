package com.example.axondemo.api

import org.axonframework.serialization.Revision
import java.math.BigDecimal
import java.util.*

@Revision("1.0")
data class ProductCreatedEvent(
    val productId: String,
    val name: String,
    val description: String,
    val price: BigDecimal
)