package com.example.axondemo

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

class ReactiveProgramming {

    // Mono 1 element
    // Flux 1..N element

    @Test
    fun test() {
        var test = 0
        reactiveAddNumber(1, 1) // 2
            .doOnNext { test = it + 1 } // 2 + 1 = 3
            .map { it + 1 } // 2 + 1 = 3
            .doOnNext { println("============ Result ${test} ==============") }
            .subscribe()

        Thread.sleep(7000)
    }

    @Test
    fun test2() {
        // 1000 1
        Flux.interval(Duration.ofSeconds(1))
            .flatMap { queryDataFromRestAPI() }
            .doOnNext { println("====== result ${it} ======") }
            .subscribe()

        Thread.sleep(10000)
    }

    @Test
    fun test3() {
        Mono.just(10)
            .flatMapMany { number ->
                Flux.interval(Duration.ofSeconds(1))
                    .doOnNext { println("====== Result ${number} ====== ") }
            }
            .subscribe()

        Thread.sleep(10000)
    }

    @Test
    fun test4() {
        Flux.just(1, 2, 3, 4)
            .doOnNext { println("====== Flux ${it} ======") }
            .collectList()
            .flatMap { Mono.just(it) }
            .doOnNext { println("======= Result ${it} ======") }
            .subscribe()

        Thread.sleep(5000)
    }

    fun addNumber(num1: Int, num2: Int): Int {
        return num1 + num2
    }

    fun reactiveAddNumber(num1: Int, num2: Int) =
        Mono.delay(Duration.ofSeconds((1..5).random().toLong()))
            .map { num1 }

    fun reactiveSubtractNumber(result: Int) =
        Mono.just(result)
            .map { it - 10 }

    fun queryDataFromRestAPI() =
        Mono.just((1..5).random().toLong())
}

class ProductAggregate {

}