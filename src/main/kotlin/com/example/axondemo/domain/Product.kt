package com.example.axondemo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document
data class Product(
    @Id
    val productId: String,
    val name: String,
    val description: String,
    val price: BigDecimal
)
