package com.example.axondemo.command

class SomeClass {

    private var service: Service

    constructor(service: Service) {
        this.service = service
    }

    fun doSomething(number: Int): Int {
        return service.changeNumber(number) + 1
    }
}

interface Service {
    fun changeNumber(number: Int): Int
}