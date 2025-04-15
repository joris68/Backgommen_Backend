package com.joris.Backgammon.Controller


import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class BackgammonController {

    @GetMapping("/hello")
    fun hey(): String {
        return "hey"
    }
}
