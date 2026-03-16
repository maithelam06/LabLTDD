package com.example.test

fun main() {
    // 1. Khởi tạo một Map rỗng để chứa dữ liệu nhập vào
    val userAges = mutableMapOf<String, Int>()

    println("--- Nhap du lieu nguoi dung ( Nhap 'done' de dung!) ---")

    while (true) {
        print("Nhap ten: ")
        val name = readln().trim()

        // Điều kiện để dừng việc nhập
        if (name.lowercase() == "done") break

        print("Nhap tuoi cho $name: ")
        val ageInput = readln()
        val age = ageInput.toIntOrNull()

        if (age == null) {
            println("Loi: Tuoi phai la mot so nguye. Vui long nhap lai.")
            continue // Quay lại đầu vòng lặp để nhập lại
        }

        // 2. Thêm vào Map (Key là name, Value là age)
        userAges[name] = age
    }

    // --- Sau khi nhập xong, thực hiện các thao tác hiển thị ---

    if (userAges.isEmpty()) {
        println("Danh sach trong!")
    } else {
        println("\n--- Danh sach nguoi dung ---")
        for ((name, age) in userAges) {
            println("$name: $age tuoi")
        }

        // Ví dụ: Truy xuất thử một người
        print("\nNhap ten nguoi ban muon tim tuoi: ")
        val searchName = readln().trim()
        val foundAge = userAges[searchName]

        if (foundAge != null) {
            println("Tuoi của $searchName là: $foundAge")
        } else {
            println("Khong tim thay ten '$searchName' trong danh sach.")
        }
    }
}