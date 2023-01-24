package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision


data class BanChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String
)

@Revision("1.0")
data class ChannelBannedEvent(
    val channelId: String
)

data class UnbanChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String
)

@Revision("1.0")
data class ChannelUnbannedEvent(
    val channelId: String
)