package com.ezral.personalinventory.data.repository

import com.ezral.personalinventory.data.local.dao.HouseDao
import com.ezral.personalinventory.data.local.dao.RoomDao
import com.ezral.personalinventory.data.local.entity.HouseEntity
import com.ezral.personalinventory.data.local.entity.RoomEntity
import com.ezral.personalinventory.data.local.entity.newEntityUuid
import com.ezral.personalinventory.domain.model.HouseInvite
import com.ezral.personalinventory.domain.model.ImportInviteResult
import com.ezral.personalinventory.domain.model.RoomInvite
import com.ezral.personalinventory.util.ShareCodec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareConnectRepository @Inject constructor(
    private val houseDao: HouseDao,
    private val roomDao: RoomDao,
) {
    fun houseInvite(house: HouseEntity): String =
        ShareCodec.encodeHouse(HouseInvite(uuid = house.uuid, name = house.name))

    fun roomInvite(room: RoomEntity, house: HouseEntity): String =
        ShareCodec.encodeRoom(
            RoomInvite(
                uuid = room.uuid,
                name = room.name,
                houseUuid = house.uuid,
                houseName = house.name,
            ),
        )

    suspend fun importInvite(raw: String): ImportInviteResult {
        return when (val payload = ShareCodec.decode(raw)) {
            is HouseInvite -> importHouse(payload)
            is RoomInvite -> importRoom(payload)
            else -> error("Unsupported invite")
        }
    }

    private suspend fun importHouse(invite: HouseInvite): ImportInviteResult {
        val existing = houseDao.getByUuid(invite.uuid)
        return if (existing != null) {
            houseDao.update(
                existing.copy(
                    name = invite.name,
                    updatedAt = System.currentTimeMillis(),
                ),
            )
            ImportInviteResult.Updated("House already linked — name updated")
        } else {
            val id = houseDao.insert(
                HouseEntity(
                    uuid = invite.uuid,
                    name = invite.name,
                ),
            )
            ImportInviteResult.HouseLinked(id, invite.name)
        }
    }

    private suspend fun importRoom(invite: RoomInvite): ImportInviteResult {
        val house = ensureHouse(invite.houseUuid, invite.houseName)
        val existing = roomDao.getByUuid(invite.uuid)
        return if (existing != null) {
            roomDao.update(
                existing.copy(
                    name = invite.name,
                    houseId = house.id,
                    houseUuid = house.uuid,
                ),
            )
            ImportInviteResult.RoomLinked(existing.id, invite.name, house.id)
        } else {
            val id = roomDao.insert(
                RoomEntity(
                    uuid = invite.uuid,
                    houseId = house.id,
                    houseUuid = house.uuid,
                    name = invite.name,
                ),
            )
            ImportInviteResult.RoomLinked(id, invite.name, house.id)
        }
    }

    private suspend fun ensureHouse(houseUuid: String, houseName: String): HouseEntity {
        return houseDao.getByUuid(houseUuid)
            ?: houseDao.getById(
                houseDao.insert(
                    HouseEntity(uuid = houseUuid, name = houseName),
                ),
            )!!
    }

    suspend fun linkRoomToHouseByUuid(roomId: Long, targetHouseUuid: String): Boolean {
        val house = houseDao.getByUuid(targetHouseUuid) ?: return false
        val room = roomDao.getById(roomId) ?: return false
        roomDao.update(
            room.copy(
                houseId = house.id,
                houseUuid = house.uuid,
            ),
        )
        return true
    }
}
