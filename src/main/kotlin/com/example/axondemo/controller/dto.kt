package com.example.axondemo.controller

import java.math.BigDecimal
import javax.validation.constraints.Max

data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: BigDecimal
)

data class UpdateProductRequest(
    val name: String,
    val description: String,
    val price: BigDecimal
)


data class CreateChannelRequest(
    val name: String,
    val description: String
)

data class AddVideoRequest(
    val name: String,
    val description: String,
    @Max(1500)
    val length: Long
)

data class UpdateVideoRequest(
    val name: String
)