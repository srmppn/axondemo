package com.example.axondemo.command

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SomeClassTest {

    @InjectMocks
    private lateinit var someClass: SomeClass

    @Mock
    private lateinit var service: Service

    // AAA
    // Arrange, Act, Assert
    @Test
    fun whenDoSomething_ShouldIncreaseNumberByOne() {
        // Arrange
        val testNumber = 10
        val changedNumber = 5
        Mockito.`when`(service.changeNumber(Mockito.anyInt()))
            .thenReturn(changedNumber)

        // Act
        val result = someClass.doSomething(testNumber)

        // Assert
        val expectedResult = 6
        assert(result == expectedResult, { "Result should equal to 6" })
    }
}