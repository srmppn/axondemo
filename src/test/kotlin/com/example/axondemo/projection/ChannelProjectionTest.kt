package com.example.axondemo.projection

import com.example.axondemo.api.ChannelCreatedEvent
import com.example.axondemo.repository.ChannelRepository
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChannelProjectionTest {

    @Autowired
    private lateinit var channelRepository: ChannelRepository

    @Autowired
    private lateinit var channelProjection: ChannelProjection

    val random = EasyRandom()

    @Test
    fun whenChannelCreatedEventWasPublished_ShouldSaveDataToTheDatabase() {
        val event = random.nextObject(ChannelCreatedEvent::class.java)

        channelProjection.on(event)

        val result = channelRepository.findById(event.channelId)
                                      .block()!!

        assert(result.channelId == event.channelId)
        assert(result.name == event.name)
    }
}