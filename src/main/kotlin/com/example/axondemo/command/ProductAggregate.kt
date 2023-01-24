package com.example.axondemo.command

import com.example.axondemo.api.CreateProductCommand
import com.example.axondemo.api.ProductCreatedEvent
import com.example.axondemo.api.ProductUpdatedEvent
import com.example.axondemo.api.UpdateProductCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

// 1..n
@Aggregate
class ProductAggregate() {

    @AggregateIdentifier
    private lateinit var productId: UUID

    private lateinit var name: String

    @CommandHandler
    constructor(command: CreateProductCommand): this() {
        AggregateLifecycle.apply(ProductCreatedEvent(command.productId,
                                                     command.name,
                                                     command.description,
                                                     command.price))
    }

    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        productId = event.productId
        name = event.name
    }

    @CommandHandler
    fun handle(command: UpdateProductCommand) {
        AggregateLifecycle.apply(ProductUpdatedEvent(command.productId,
                                                     command.name,
                                                     command.description,
                                                     command.price))
    }

    @EventSourcingHandler
    fun on(event: ProductUpdatedEvent) {
        name = event.name
    }
}