package com.example.axondemo.projection

import com.example.axondemo.api.ChannelCreatedEvent
import com.example.axondemo.domain.Channel
import com.example.axondemo.repository.ChannelRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChannelProjection {
    @Autowired
    private lateinit var channelRepository: ChannelRepository

    @EventHandler
    fun on(event: ChannelCreatedEvent) {
        val channel = Channel(event.channelId, event.name, event.description)

        channelRepository.save(channel).block()
    }
}