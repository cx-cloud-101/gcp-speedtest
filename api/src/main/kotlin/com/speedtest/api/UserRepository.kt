package com.speedtest.api

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository

interface UserRepository: FirestoreReactiveRepository<User>
