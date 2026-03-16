package com.example.test

fun main() {
    print("Nhap diem cua ban: ")
    val input = readln()
    val score = input.toDoubleOrNull()

    if (score == null) {
        println("Loi: Vui long nhap 1 so hop le")
    } else {
        if (score >= 90) {
            println("Xep loai: Xuat sac")
        } else if (score >= 80) {
            println("Xep loai: Gioi")
        } else if (score >= 65) {
            println("Xep loai: Kha")
        } else {
            println("Xep loai: Trung binh/Yeu")
        }
    }
}