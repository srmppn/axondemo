package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.modelling.command.EntityId
import org.axonframework.spring.stereotype.Aggregate


@Aggregate
abstract class Channel() {

    @AggregateIdentifier
    lateinit var channelId: String
    lateinit var name: String
    lateinit var description: String


    @AggregateMember
    lateinit var videos: MutableList<Video>

    lateinit var channelStatus: ChannelStatus


//    @EventSourcingHandler
//    fun on(event: VkChannelCreatedEvent) {
//        channelId = event.channelId
//        name = event.name
//        description = event.description
//        videos = mutableListOf<Video>()
//        channelStatus = ChannelStatus.Available
//    }

    @CommandHandler
    fun handle(command: AddVideoCommand) {
        if (channelStatus == ChannelStatus.Banned) {
            throw IllegalArgumentException("Channel was banned.")
        }
        AggregateLifecycle.apply(
            VideoAddedEvent(command.channelId, command.videoId, command.name, command.description, command.length))
    }

    @EventSourcingHandler
    fun on(event: VideoAddedEvent) {
        videos.add(
            Video(event.videoId, event.name, event.description, event.length))
    }

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


// parent
@Aggregate
class YoutubeChannel(): Channel() {

    private var subscriber: Int = 0


    @CommandHandler
    constructor(command: CreateYoutubeChannelCommand): this() {
        AggregateLifecycle.apply(
            YoutubeChannelCreatedEvent(command.channelId, command.name, command.description))
    }

    @EventSourcingHandler
    fun on(event: YoutubeChannelCreatedEvent) {
        channelId = event.channelId
        name = event.name
        description = event.description
        videos = mutableListOf<Video>()
        channelStatus = ChannelStatus.Available
    }

    @CommandHandler
    fun handle(command: SubscribeChannelCommand) {
        AggregateLifecycle.apply(ChannelSubscribedEvent(command.channelId))
    }

    @EventSourcingHandler
    fun on(event: ChannelSubscribedEvent) {
        subscriber += 1
    }
}

@Aggregate
class VkChannel(): Channel() {

    @CommandHandler
    constructor(command: CreateVkChannelCommand): this() {
        AggregateLifecycle.apply(
            VkChannelCreatedEvent(command.channelId, command.name, command.description))
    }
}

// child
class Video {

    @EntityId
    private lateinit var videoId: String

    private lateinit var name: String
    private lateinit var description: String

    private var length: Long = 0

    constructor(videoId: String, name: String, description: String, length: Long) {
        this.videoId = videoId
        this.name = name
        this.description = description
        this.length = length
    }

    @CommandHandler
    fun handle(command: UpdateVideoCommand) {
        AggregateLifecycle.apply(
            VideoUpdatedEvent(command.channelId, command.videoId, command.name))
    }

    @EventSourcingHandler
    fun on(event: VideoUpdatedEvent) {
        this.name = event.name
    }
}

enum class ChannelStatus {
    Available,
    Banned
}