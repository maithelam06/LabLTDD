package com.example.test

fun checkResult(rollResult: Int, luckyNumber: Int) {
    when (rollResult) {
        luckyNumber -> println("Ban da do ra so $rollResult. Ban da thang!")
        1 -> println("Rat tiec ban do ra so 1")
        2 -> println("Rat tiec ban do ra so 2")
        3 -> println("Rat tiec ban do ra so 3")
        4 -> println("Rat tiec ban do ra so 4")
        5 -> println("Rat tiec ban do ra so 5")
        6 -> println("Rat tiec ban do ra so 6")
        else -> println("Loi roi! Lam sao ban do ra duoc so $rollResult vay")
    }
}
fun main() {
    checkResult((1..6).random(),3)
}