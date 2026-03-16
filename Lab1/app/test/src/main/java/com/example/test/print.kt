package com.example.test
// Xin chao
fun Xinchao() {
    println("xin chao!")
}
// in chuoi co bien
fun Inchuoi() {
    // Assign once, cannot change.
    val age = "5"
    val name = "Hai"

    // Assign and change as needed.
    var roll = 6
    var rolledValue: Int = 4
    println("Ban da ${age} tuoi roi!")
    println("Ban da ${age} tuoi roi, ${name}!")
}
fun main(){
    Xinchao()
    Inchuoi()
}
