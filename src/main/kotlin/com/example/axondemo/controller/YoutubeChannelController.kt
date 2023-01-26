package com.example.axondemo.controller

import com.example.axondemo.api.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
class YoutubeChannelController {

    @Autowired
    private lateinit var commandGateway: CommandGateway

    @PostMapping("/channel/youtube")
    fun createYoutubeChannel(@RequestBody request: CreateChannelRequest): CompletableFuture<String> {
        return commandGateway.send<String>(CreateYoutubeChannelCommand(UUID.randomUUID().toString(), request.name, request.description))
    }

    @PostMapping("/channel/vk")
    fun createVkChannel(@RequestBody request: CreateChannelRequest): CompletableFuture<String> {
        return commandGateway.send<String>(CreateVkChannelCommand(UUID.randomUUID().toString(), request.name, request.description))
    }

    @PutMapping("/channel/{channelId}/confirm/{passCode}")
    fun confirmChannelCreation(
        @PathVariable("channelId") channelId: String,
        @PathVariable("passCode") passCode: String
    ): CompletableFuture<String> {
        return commandGateway.send<String>(ConfirmChannelCreationCommand(channelId, passCode))
    }

    @PutMapping("/channel/{channelId}/subscribe")
    fun subscribeChannel(@PathVariable("channelId") channelId: String): CompletableFuture<String> {
        return commandGateway.send<String>(
            SubscribeChannelCommand(channelId = channelId)
        )
    }

    @PutMapping("/channel/{channelId}/ban")
    fun banChannel(@PathVariable("channelId") channelId: String): CompletableFuture<String> {
        return commandGateway.send<String>(BanChannelCommand(channelId))
    }

    @PutMapping("/channel/{channelId}/unban")
    fun unbanChannel(@PathVariable("channelId") channelId: String): CompletableFuture<String> {
        return commandGateway.send<String>(UnbanChannelCommand(channelId))
    }


    @PostMapping("/channel/{channelId}/video")
    fun addVideo(
        @PathVariable("channelId") channelId: String,
        @RequestBody request: AddVideoRequest
    ): CompletableFuture<String> {
        return commandGateway.send<String>(
            AddVideoCommand(channelId, UUID.randomUUID().toString(), request.name, request.description, request.length)
        )
    }


    @PutMapping("/channel/{channelId}/video/{videoId}")
    fun updateVideo(
        @PathVariable("channelId") channelId: String,
        @PathVariable("videoId") videoId: String,
        @RequestBody request: UpdateVideoRequest
    ): CompletableFuture<String> {
        return commandGateway.send<String>(
            UpdateVideoCommand(channelId, videoId, request.name)
        )
    }
}