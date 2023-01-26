package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision

data class CreateYoutubeChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val name: String,
    val description: String
)

data class CreateVkChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val name: String,
    val description: String
)

@Revision("1.0")
data class ChannelCreatedEvent(
    val channelId: String,
    val name: String,
    val description: String
)