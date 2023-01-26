package com.example.axondemo.saga

import com.example.axondemo.api.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.serialization.Revision
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration

@Saga
class ChannelCreationSaga {

    companion object {
        const val TEST_DEADLINE = "test_deadline"
    }

    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var eventScheduler: EventScheduler

    @Autowired
    private lateinit var deadlineManager: DeadlineManager

    private lateinit var channelId: String
    private lateinit var name: String
    private lateinit var description: String

    private lateinit var token: ScheduleToken

    @StartSaga
    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: CreateChannelRequestedEvent) {
        println("Start saga ${event.javaClass.simpleName}")
        channelId = event.channelId
        name = event.name
        description = event.description
        token = eventScheduler.schedule(Duration.ofSeconds(15), ConfirmationExpiredEvent(channelId))

        deadlineManager.schedule(Duration.ofSeconds(10), TEST_DEADLINE, ConfirmationExpiredEvent(channelId))
    }

    @DeadlineHandler(deadlineName = TEST_DEADLINE)
    fun handleDeadline(payload: ConfirmationExpiredEvent) {
        println("Payload ${payload.channelId}")
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: ConfirmationExpiredEvent) {
        println("Confirmation expired.")
    }

    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: ConfirmChannelCreationRequestedEvent) {
        println(event.javaClass.simpleName)
        if (event.passcode == "1234") {
            commandGateway.send<String>(ApproveConfirmationCommand(event.channelId, name, description))
            eventScheduler.cancelSchedule(token)
            deadlineManager.cancelAllWithinScope(TEST_DEADLINE)
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "channelId")
    fun on(event: ChannelCreatedEvent) {
        println("End saga ${event.javaClass.simpleName}")
        // empty
    }
}

@Revision("1.0")
data class ConfirmationExpiredEvent(
    val channelId: String
)