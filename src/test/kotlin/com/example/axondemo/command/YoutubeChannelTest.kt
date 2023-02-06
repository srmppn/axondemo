package com.example.axondemo.command

import com.example.axondemo.api.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.aggregate.TestExecutor
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class YoutubeChannelTest {
    private lateinit var fixture: FixtureConfiguration<YoutubeChannel>

    private val random = EasyRandom()

    @BeforeEach
    fun setup() {
        fixture = AggregateTestFixture(YoutubeChannel::class.java)
    }

    private val channelId: String = "1234"

    fun createYoutubeChannel(): TestExecutor<YoutubeChannel> {
        val event1 = random.nextObject(CreateChannelRequestedEvent::class.java)
                            .copy(channelId = channelId)
        val event2 = random.nextObject(ChannelCreatedEvent::class.java)
                            .copy(channelId = channelId)
        return fixture.given(event1, event2)
    }

    @Test
    fun whenCreateYoutubeChannelSuccess_ShouldPublishEvent() {
        // Arrange
        val command = random.nextObject(CreateYoutubeChannelCommand::class.java)

        fixture.givenNoPriorActivity()
            .`when`(command)
            .expectEvents(
                CreateChannelRequestedEvent(
                    channelId = command.channelId,
                    name = command.name,
                    description = command.description
                )
            )
    }

    @Test
    fun whenSubscribeChannelSuccess_ShouldPublishEventAndIncreaseSubscriberByOne() {
        val command = random.nextObject(SubscribeChannelCommand::class.java)
                            .copy(channelId = channelId)
        createYoutubeChannel()
            .`when`(command)
            .expectEvents(
                ChannelSubscribedEvent(
                    channelId = command.channelId
                )
            )
            .expectState {
                assert(it.subscriber == 1, { "Subscriber should equal to 1" })
            }
    }
}