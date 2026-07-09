package com.ezral.personalinventory.domain.model

data class HouseInvite(
    val uuid: String,
    val name: String,
)

data class RoomInvite(
    val uuid: String,
    val name: String,
    val houseUuid: String,
    val houseName: String,
)

sealed class ImportInviteResult {
    data class HouseLinked(val houseId: Long, val houseName: String) : ImportInviteResult()
    data class RoomLinked(val roomId: Long, val roomName: String, val houseId: Long) : ImportInviteResult()
    data class Updated(val message: String) : ImportInviteResult()
}
