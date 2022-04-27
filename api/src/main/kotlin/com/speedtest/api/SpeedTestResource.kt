package com.speedtest.api

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.gson.Gson
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    val gson: Gson = Gson()

    data class Speeds(val download: Number, val upload: Number)
    data class Client(val ip: String, val lat: Number, val lon: Number, val isp: String, val country: String)
    data class Server(val host: String, val lat: Number, val lon: Number, val country: String, val distance: Number, val ping: Number, val id: String)
    data class Result(val speeds: Speeds, val client: Client, val server: Server)
    data class TestResult(val user: String, val device: Int, val timestamp: Long, val data: Result)

    @PostMapping
    fun publishTestResult(@RequestBody speedTest: TestResult) {
        this.pubSub.publish("speedtest", gson.toJson(speedTest))
        println(speedTest.data.server.id)
    }
}