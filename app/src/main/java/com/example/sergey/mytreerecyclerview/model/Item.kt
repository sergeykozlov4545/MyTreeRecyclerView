package com.example.sergey.mytreerecyclerview.model

data class Item(
        val id: Long = 0,
        val text: String = "",
        val subItems: List<Item> = emptyList(),
        var depth: Long = 0,
        var opened: Boolean = false
)

