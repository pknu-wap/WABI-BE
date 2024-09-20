package com.wap.wabi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WabiApplication

fun main(args: Array<String>) {
    runApplication<WabiApplication>(*args)
}
