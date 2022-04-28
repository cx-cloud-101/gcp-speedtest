package com.speedtest.api

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.gson.Gson
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/trigger")
class TriggerResource(val repository: UserRepository, val pubSub: PubSubTemplate) {

    val gson: Gson = Gson()

    data class Device(val user: String, val device: Int)

    fun toDevice(user: String, device: Int): Device {
        return Device(user, device)
    }

    fun toDevices(user: User): List<Device> {
        return user.devices.map { toDevice(user.name, it) }
    }

    @GetMapping
    fun trigger(): List<User> {
        val users: List<User> = repository.findAll().toIterable().toList()
        val devices: List<Device> = users.flatMap { toDevices(it) }
        for (device in devices) {
            this.pubSub.publish("speedtest-trigger", gson.toJson(device))
        }
        return users
    }

    @PostMapping("/register")
    fun register(@RequestBody device: Device): User {
        var user: User? = repository.findById(device.user).block()
        return if (user != null) {
            user.devices.add(device.device)
            user.devices = user.devices.distinct().toMutableList()
            repository.save(user).block()
            user
        } else {
            var user: User = User(device.user)
            user.devices.add(device.device)
            user.devices = user.devices.distinct().toMutableList()
            repository.save(user).block()
            user
        }
    }

    @DeleteMapping("/register")
    fun delete(@RequestBody device: Device): User? {
        var user: User? = repository.findById(device.user).block()
        return if (user != null) {
            user.devices.removeIf { it == device.device }
            repository.save(user).block()
            user
        } else {
            null
        }
    }
}
