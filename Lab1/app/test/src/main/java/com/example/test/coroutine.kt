package com.example.test

import kotlinx.coroutines.*

suspend fun fetchUserInfo(): String {
    delay(1000) // Giả lập chờ mạng 1 giây
    return "Nguoi dung: Tuan"
}

suspend fun fetchUserPosts(): List<String> {
    delay(1500) // Giả lập chờ mạng 1.5 giây
    return listOf("Bai viet 1", "Bai viet 2", "Bai viet 3")
}

fun main() = runBlocking {
    println("--- Bat dau lay du lieu ---")
    val startTime = System.currentTimeMillis()

    val userDeferred = async { fetchUserInfo() }
    val postsDeferred = async { fetchUserPosts() }

    println("Dang xu li cac viec khac tren luong chinh...")

    // Đợi kết quả trả về bằng 'await'
    val userInfo = userDeferred.await()
    val userPosts = postsDeferred.await()

    val endTime = System.currentTimeMillis()

    // Hiển thị kết quả
    println("\n$userInfo")
    println("Danh sach bai viet: $userPosts")
    println("Tong thoi gian thuc hien: ${endTime - startTime} ms")
}