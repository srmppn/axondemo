package com.example.axondemo.saga

import com.example.axondemo.api.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class ChannelCreationSaga {

    @Autowired
    private lateinit var eventBus: EventBus

    @Autowired
    private lateinit var commandGateway: CommandGateway

    private lateinit var channelId: String
    private lateinit var name: String
    private lateinit var description: String

    @StartSaga
    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: CreateChannelRequestedEvent) {
        println("Start saga ${event.javaClass.simpleName}")
        channelId = event.channelId
        name = event.name
        description = event.description
    }

    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: ConfirmChannelCreationRequestedEvent) {
        println(event.javaClass.simpleName)
        if (event.passcode == "1234") {
            commandGateway.send<String>(ApproveConfirmationCommand(event.channelId, name, description))
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "channelId")
    fun on(event : ChannelCreatedEvent) {
        println("End saga ${event.javaClass.simpleName}")
        // empty
    }
}