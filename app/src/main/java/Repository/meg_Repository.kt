package com.utn.rooms.repository

import com.utn.rooms.Service.meg_APIService
import com.utn.rooms.model.*


class meg_Repository {
    private val api = meg_APIService.api

    suspend fun auth(user: String, password: String) =
        api.auth(MegUserAuthRequest(user, password))

    suspend fun getUsers() = api.getUsers()

    suspend fun getRooms() = api.getRooms()


    suspend fun createRoom(name: String, capacity: Int) =
        api.createRoom(MegRoomCreateRequest(name, capacity))

    suspend fun bookRoom(roomId: String) =
        api.bookRoom(MegRoomBookingRequest(roomId))

    suspend fun unbookRoom(roomId: String) =
        api.unbookRoom(MegRoomBookingRequest(roomId))



}

