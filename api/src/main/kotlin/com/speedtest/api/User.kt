package com.speedtest.api

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document
import java.io.Serializable

@Document(collectionName = "users")
data class User (

    @DocumentId
    var name: String = "",
    var devices: MutableList<Int> = mutableListOf<Int>()

) : Serializable
