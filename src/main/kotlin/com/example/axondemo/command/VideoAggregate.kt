package com.example.axondemo.command

import com.example.axondemo.api.UpdateVideoCommand
import com.example.axondemo.api.VideoUpdatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.EntityId


// child
abstract class VideoAggregate {

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