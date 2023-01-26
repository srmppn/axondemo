package com.example.axondemo

import org.axonframework.common.AxonThreadFactory
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.deadline.SimpleDeadlineManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.scheduling.java.SimpleEventScheduler
import org.axonframework.spring.eventhandling.scheduling.java.SimpleEventSchedulerFactoryBean
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
}