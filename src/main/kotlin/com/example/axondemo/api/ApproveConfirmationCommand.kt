package com.example.axondemo.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ApproveConfirmationCommand(
    @TargetAggregateIdentifier
    val channelId: String,
    val name: String,
    val description: String
)