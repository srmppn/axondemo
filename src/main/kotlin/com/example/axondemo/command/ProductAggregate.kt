package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

// 1..n
@Aggregate
class ProductAggregate() {

    @AggregateIdentifier
    private lateinit var productId: String

    private lateinit var name: String

    @CommandHandler
    constructor(command: CreateProductCommand) : this() {
        AggregateLifecycle.apply(ProductCreatedEvent(command.productId, command.name, command.description, command.price))
    }

    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        productId = event.productId
        name = event.name
    }

    @CommandHandler
    fun handle(command: UpdateProductCommand): String {
        AggregateLifecycle.apply(ProductUpdatedEvent(command.productId, command.name, command.description, command.price))
        return command.productId
    }

    @EventSourcingHandler
    fun on(event: ProductUpdatedEvent) {
        name = event.name
    }

    @CommandHandler
    fun handle(command: DeleteProductCommand): String {
        AggregateLifecycle.apply(ProductDeletedEvent(command.productId))
        return command.productId
    }

    @EventSourcingHandler
    fun on(event: ProductDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}