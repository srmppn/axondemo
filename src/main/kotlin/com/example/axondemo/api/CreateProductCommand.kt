package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.UUID

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val name: String,
    val description: String,
    val price: BigDecimal
)
