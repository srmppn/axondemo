package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision


data class AddVideoCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val videoId: String,
    val name: String,
    val description: String,
    val length: Long
)

@Revision("1.0")
data class VideoAddedEvent(
    val channelId: String,
    val videoId: String,
    val name: String,
    val description: String,
    val length: Long
)