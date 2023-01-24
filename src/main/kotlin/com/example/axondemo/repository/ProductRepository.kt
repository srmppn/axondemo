package com.example.axondemo.repository

import com.example.axondemo.domain.Product
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ProductRepository : ReactiveCrudRepository<Product, String> {
}