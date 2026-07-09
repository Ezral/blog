package com.ezral.personalinventory.util

import com.ezral.personalinventory.domain.model.HouseInvite
import com.ezral.personalinventory.domain.model.RoomInvite
import org.json.JSONObject

object ShareCodec {
    private const val TYPE = "t"
    private const val TYPE_HOUSE = "house"
    private const val TYPE_ROOM = "room"
    private const val ID = "id"
    private const val NAME = "n"
    private const val HOUSE_ID = "hid"
    private const val HOUSE_NAME = "hn"

    fun encodeHouse(invite: HouseInvite): String {
        return JSONObject()
            .put(TYPE, TYPE_HOUSE)
            .put(ID, invite.uuid)
            .put(NAME, invite.name)
            .toString()
    }

    fun encodeRoom(invite: RoomInvite): String {
        return JSONObject()
            .put(TYPE, TYPE_ROOM)
            .put(ID, invite.uuid)
            .put(NAME, invite.name)
            .put(HOUSE_ID, invite.houseUuid)
            .put(HOUSE_NAME, invite.houseName)
            .toString()
    }

    fun decode(raw: String): Any {
        val json = JSONObject(raw.trim())
        return when (json.getString(TYPE)) {
            TYPE_HOUSE -> HouseInvite(
                uuid = json.getString(ID),
                name = json.getString(NAME),
            )
            TYPE_ROOM -> RoomInvite(
                uuid = json.getString(ID),
                name = json.getString(NAME),
                houseUuid = json.getString(HOUSE_ID),
                houseName = json.getString(HOUSE_NAME),
            )
            else -> error("Unknown invite type")
        }
    }
}
