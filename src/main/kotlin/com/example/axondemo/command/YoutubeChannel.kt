package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate


@Aggregate
class YoutubeChannel(): Channel() {

    private var subscriber: Int = 0

    @CommandHandler
    constructor(command: CreateYoutubeChannelCommand): this() {
        AggregateLifecycle.apply(
            ChannelCreatedEvent(command.channelId, command.name, command.description))
    }

    @CommandHandler
    fun handle(command: SubscribeChannelCommand) {
        AggregateLifecycle.apply(ChannelSubscribedEvent(command.channelId))
    }

    @EventSourcingHandler
    fun on(event: ChannelSubscribedEvent) {
        subscriber += 1
    }

    override fun on(event: VideoAddedEvent) {
        videos.add(
            MovieVideo(event.videoId, event.name, event.description, event.length))
    }
}