package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision
import java.util.UUID

data class DeleteProductCommand(
    @TargetAggregateIdentifier
    val productId: String
)

@Revision("1.0")
data class ProductDeletedEvent(
    val productId: String
)
