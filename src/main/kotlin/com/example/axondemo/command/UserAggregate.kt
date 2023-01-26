package com.example.axondemo.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import java.math.BigDecimal

class UserAggregate {
    lateinit var name: String
    val balance = BigDecimal(100)

    @CommandHandler
    fun handle(command: DeductBalanceCommand) {
        if (balance < command.amount) {
            throw IllegalArgumentException("Insufficient balance.")
        }
    }
}

class UserController {

    @Autowired
    private lateinit var commandGateway: CommandGateway

    @PutMapping
    fun deductBalance(command: DeductBalanceCommand) {
        val user = findUserById()
        if (user.name.equals(command.name)) {
            throw IllegalArgumentException("Insufficient balance.")
        }
        commandGateway.send<String>(command)
    }

    private fun findUserById() = UserAggregate()
}

data class DeductBalanceCommand(
    val userId: String,
    val amount: BigDecimal,
    val name: String
)

// command1, command2