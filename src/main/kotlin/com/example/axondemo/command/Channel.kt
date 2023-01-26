package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.MetaData
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.modelling.command.CommandHandlerInterceptor
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.util.function.BiFunction


@Aggregate
abstract class Channel {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    @AggregateIdentifier
    lateinit var channelId: String
    lateinit var name: String
    lateinit var description: String

    var isConfirmed: Boolean = false

    @AggregateMember
    lateinit var videos: MutableList<VideoAggregate>

    lateinit var channelStatus: ChannelStatus

    @EventSourcingHandler
    fun on(event: CreateChannelRequestedEvent) {
        channelId = event.channelId
    }

    @CommandHandler
    fun handle(command: ConfirmChannelCreationCommand): String {
        AggregateLifecycle.apply(ConfirmChannelCreationRequestedEvent(command.channelId, command.passcode))
        return command.channelId
    }

    @CommandHandler
    fun handle(command: ApproveConfirmationCommand): String {
        AggregateLifecycle.apply(ChannelCreationConfirmedEvent(command.channelId))
        AggregateLifecycle.apply(ChannelCreatedEvent(command.channelId, command.name, command.description))
        return command.channelId
    }

    @EventSourcingHandler
    fun on(event: ChannelCreationConfirmedEvent) {
        isConfirmed = true
    }

    @EventSourcingHandler
    fun on(event: ChannelCreatedEvent) {
        channelId = event.channelId
        name = event.name
        description = event.description
        videos = mutableListOf<VideoAggregate>()
        channelStatus = ChannelStatus.Available
    }

    @CommandHandler
    fun handle(command: AddVideoCommand) {
        AggregateLifecycle.apply(
            VideoAddedEvent(command.channelId, command.videoId, command.name, command.description, command.length))
    }

    @EventSourcingHandler
    abstract fun on(event: VideoAddedEvent)

    @CommandHandler
    fun handle(command: BanChannelCommand) {
        AggregateLifecycle.apply(ChannelBannedEvent(command.channelId))
    }

    @EventSourcingHandler
    fun on(event: ChannelBannedEvent) {
        channelStatus = ChannelStatus.Banned
    }

    @CommandHandler
    fun handle(command: UnbanChannelCommand) {
        AggregateLifecycle.apply(ChannelUnbannedEvent(command.channelId))
    }

    @EventSourcingHandler
    fun on(event: ChannelUnbannedEvent) {
        channelStatus = ChannelStatus.Available
    }

    @CommandHandlerInterceptor
    fun intercept(command: CommandMessage<*>, interceptorChain: InterceptorChain): Any? {
        logger.info("Intercept command -> ${command.payload::class.java.simpleName}")
        if (channelStatus == ChannelStatus.Banned && command.payload !is UnbanChannelCommand) {
            throw IllegalArgumentException("Channel was banned.")
        }
        return interceptorChain.proceed()
    }
}

// request -----> intercept ------> handler(command, event, ...)

class TestDispatchInterceptor : MessageDispatchInterceptor<CommandMessage<*>> {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
    override fun handle(messages: MutableList<out CommandMessage<*>>): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
        return BiFunction { number, command ->
            logger.info("Intercept from test dispatch interceptor")
            command.withMetaData(MetaData.with("test", "1234"))
        }
    }

}

class TestHandlerInterceptor : MessageHandlerInterceptor<EventMessage<*>> {
    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
    override fun handle(unitOfWork: UnitOfWork<out EventMessage<*>>, interceptorChain: InterceptorChain): Any {
        logger.info("Intercept from handler interceptor with payload : ${unitOfWork.message.payloadType}")
        return interceptorChain.proceed()
    }
}