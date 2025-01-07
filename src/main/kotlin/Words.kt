package org.example

import java.io.File

fun main() {
    val file = File("words.txt")
    val listOfStrings = file.readLines()
    for (i in listOfStrings) {
        println(i)
    }
}