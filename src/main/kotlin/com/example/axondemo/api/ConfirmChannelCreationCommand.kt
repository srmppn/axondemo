package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.serialization.Revision

data class ConfirmChannelCreationCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val passcode: String
)

@Revision("1.0")
data class ConfirmChannelCreationRequestedEvent(
    val channelId: String,
    val passcode: String
)

@Revision("1.0")
data class ChannelCreationConfirmedEvent(
    val channelId: String
)