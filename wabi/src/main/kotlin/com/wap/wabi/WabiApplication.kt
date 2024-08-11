package com.wap.wabi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WabiApplication

fun main(args: Array<String>) {
    runApplication<WabiApplication>(*args)
}
