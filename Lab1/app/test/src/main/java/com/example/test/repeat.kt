package com.example.test

fun main() {
    fun printCakeBottom(age: Int, layers: Int) {
        repeat(layers) {
            repeat(age + 2) {
                print("!")
            }
            println()
        }
    }
    printCakeBottom(2,3)
}