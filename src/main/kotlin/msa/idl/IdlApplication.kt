package msa.idl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IdlApplication

fun main(args: Array<String>) {
	runApplication<IdlApplication>(*args)
}
