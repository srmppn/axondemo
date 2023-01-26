package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate


@Aggregate
abstract class Channel {

    @AggregateIdentifier
    lateinit var channelId: String
    lateinit var name: String
    lateinit var description: String


    @AggregateMember
    lateinit var videos: MutableList<VideoAggregate>

    lateinit var channelStatus: ChannelStatus

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
        if (channelStatus == ChannelStatus.Banned) {
            throw IllegalArgumentException("Channel was banned.")
        }
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
}