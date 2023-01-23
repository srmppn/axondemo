package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.UUID

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal
)
