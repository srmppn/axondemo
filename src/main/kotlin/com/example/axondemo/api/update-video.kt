package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision

data class UpdateVideoCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val videoId: String,
    val name: String
)

@Revision("1.0")
data class VideoUpdatedEvent(
    val channelId: String,
    val videoId: String,
    val name: String
)