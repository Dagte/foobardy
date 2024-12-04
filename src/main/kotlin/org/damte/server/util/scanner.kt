package org.damte.org.damte.server.util

import java.util.Scanner

val scanner = Scanner(System.`in`)

fun getNonEmptyInput(prompt: String): String {
    while (true) {
        print(prompt)
        val input = scanner.nextLine().trim()
        if (input.isNotEmpty()) {
            return input
        }
        println("Input cannot be empty. Please try again.")
    }
}

fun getDoubleInput(prompt: String): Double {
    while (true) {
        print(prompt)
        val input = scanner.nextLine().trim()
        try {
            return input.toDouble()
        } catch (e: NumberFormatException) {
            println("Invalid number format. Please enter a valid number.")
        }
    }
}