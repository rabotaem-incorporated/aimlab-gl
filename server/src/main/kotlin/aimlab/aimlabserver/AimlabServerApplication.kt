package aimlab.aimlabserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AimlabServerApplication

fun main(args: Array<String>) {
	runApplication<AimlabServerApplication>(*args)
}
