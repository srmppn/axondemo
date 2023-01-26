package com.example.axondemo.command

import com.example.axondemo.api.ChannelCreatedEvent
import com.example.axondemo.api.CreateVkChannelCommand
import com.example.axondemo.api.CreateYoutubeChannelCommand
import com.example.axondemo.api.VideoAddedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

// parent
@Aggregate
class VkChannel(): Channel() {

    @CommandHandler
    constructor(command: CreateVkChannelCommand): this() {
        AggregateLifecycle.apply(
            ChannelCreatedEvent(command.channelId, command.name, command.description))
    }

    override fun on(event: VideoAddedEvent) {
        // do nothing
    }
}