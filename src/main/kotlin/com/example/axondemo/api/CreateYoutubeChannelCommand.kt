package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision

data class CreateYoutubeChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val name: String,
    val description: String
)

@Revision("1.0")
data class YoutubeChannelCreatedEvent(
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
data class VkChannelCreatedEvent(
    val channelId: String,
    val name: String,
    val description: String
)