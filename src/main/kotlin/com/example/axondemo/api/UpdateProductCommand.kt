package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

data class UpdateProductCommand(
    @TargetAggregateIdentifier
    val productId: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal
)