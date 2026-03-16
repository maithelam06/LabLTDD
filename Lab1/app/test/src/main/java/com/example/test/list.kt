package com.example.test

fun main() {
    val programmingLanguages = listOf("Kotlin", "Java", "Python", "C++", "JavaScript")
    val numbers = mutableListOf(10, 5, 8, 20, 15, 3)
    // Thêm/Xóa phần tử (Chỉ áp dụng với MutableList)
    numbers.add(100)
    numbers.removeAt(0) // Xóa phần tử đầu tiên
    // Lọc phần tử (Filtering)
    val bigNumbers = numbers.filter { it > 10 }
    println("Cac so > 10: $bigNumbers")
    // Biến đổi danh sách (Mapping)
    val upperCaseLanguages = programmingLanguages.map { it.uppercase() }
    println("Viet hoa: $upperCaseLanguages")
    // Tìm kiếm (Finding)
    val firstLongName = programmingLanguages.find { it.length > 5 }
    println("Ngon ngu dau tien co ten > 5 ky tu: $firstLongName")
    // Sắp xếp (Sorting)
    val sortedNumbers = numbers.sorted()
    val reverseSorted = numbers.sortedDescending()
    println("Sap xep tang dan: $sortedNumbers")
    // Tính toán (Aggregation)
    println("Tong cac so: ${numbers.sum()}")
    println("So lon nhat: ${numbers.maxOrNull()}")
    // Kiểm tra điều kiện (Predicates)
    val hasPython = programmingLanguages.any { it == "Python" }
    val allPositive = numbers.all { it > 0 }
    // Duyệt danh sách (Iterating)
    println("Duyet danh sach:")
    programmingLanguages.forEach { lang ->
        println("- Toi thich $lang")
    }
}