package com.example.axondemo.command

class MovieVideo(
    val videoId: String,
    val name: String,
    val description: String,
    val length: Long
): VideoAggregate(videoId, name, description, length) {

}