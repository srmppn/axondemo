package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision

data class SubscribeChannelCommand(
    @TargetAggregateIdentifier
    val channelId: String
)

@Revision("1.0")
data class ChannelSubscribedEvent(
    val channelId: String
)