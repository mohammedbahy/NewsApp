package com.bahy.nota

data class News(
    val articles : ArrayList<Article> = arrayListOf()
)

// Firestore-friendly: default values and nullable to allow deserialization
data class Article(
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
)
