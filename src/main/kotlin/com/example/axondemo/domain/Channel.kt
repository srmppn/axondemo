package com.example.axondemo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Channel(
    @Id
    val channelId: String,
    val name: String,
    val description: String
)