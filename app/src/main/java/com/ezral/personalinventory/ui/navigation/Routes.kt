package com.ezral.personalinventory.ui.navigation

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOUSES = "houses"
    const val HOUSE_DETAIL = "house/{houseId}"
    const val ROOM_DETAIL = "room/{roomId}"
    const val CONTAINER_DETAIL = "container/{containerId}"
    const val ITEM_DETAIL = "item/{itemId}"
    const val ADD_ITEM = "item/add?roomId={roomId}&containerId={containerId}&itemId={itemId}"
    const val SEARCH = "search?query={query}"
    const val SCAN = "scan"
    const val LISTS = "lists"

    fun houseDetail(houseId: Long) = "house/$houseId"
    fun roomDetail(roomId: Long) = "room/$roomId"
    fun containerDetail(containerId: Long) = "container/$containerId"
    fun itemDetail(itemId: Long) = "item/$itemId"
    fun addItem(roomId: Long? = null, containerId: Long? = null, itemId: Long? = null): String {
        return "item/add?roomId=${roomId ?: -1}&containerId=${containerId ?: -1}&itemId=${itemId ?: -1}"
    }
    fun search(query: String = "") = "search?query=${query}"
}
