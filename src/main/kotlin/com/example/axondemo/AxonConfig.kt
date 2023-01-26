package com.example.axondemo

import com.example.axondemo.command.TestDispatchInterceptor
import com.example.axondemo.command.TestHandlerInterceptor
import io.axoniq.axonserver.grpc.admin.EventProcessor
import org.axonframework.commandhandling.CommandBus
import org.axonframework.common.AxonThreadFactory
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configurer
import org.axonframework.deadline.SimpleDeadlineManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.scheduling.java.SimpleEventScheduler
import org.axonframework.spring.eventhandling.scheduling.java.SimpleEventSchedulerFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@Configuration
class AxonConfig {
    @Bean
    fun eventScheduler(eventBus: EventBus, transactionManager: TransactionManager): SimpleEventScheduler {
        val executor = Executors.newSingleThreadScheduledExecutor(AxonThreadFactory(SimpleEventSchedulerFactoryBean::class.java.simpleName))
        return SimpleEventScheduler
            .builder()
            .eventBus(eventBus)
            .transactionManager(transactionManager)
            .scheduledExecutorService(executor)
            .build()
    }

    @Bean
    fun deadlineManager(config: org.axonframework.config.Configuration,transactionManager: TransactionManager): SimpleDeadlineManager {
        return SimpleDeadlineManager.builder()
            .scopeAwareProvider(config.scopeAwareProvider())
            .transactionManager(transactionManager)
            .build()
    }

    @Autowired
    fun commandInterceptor(commandBus: CommandBus) {
        commandBus.registerDispatchInterceptor(TestDispatchInterceptor())
    }

    @Autowired
    fun eventInterceptor(config: Configurer) {
        config.eventProcessing()
            .registerDefaultHandlerInterceptor { t, u -> TestHandlerInterceptor() }
    }
}