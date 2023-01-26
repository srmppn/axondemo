package com.example.axondemo.repository

import com.example.axondemo.domain.Channel
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ChannelRepository: ReactiveCrudRepository<Channel, String> {
}