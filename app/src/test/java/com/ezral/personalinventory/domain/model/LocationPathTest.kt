package com.ezral.personalinventory.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationPathTest {

    @Test
    fun format_includesAllSegments() {
        val path = LocationPath(
            houseName = "Home",
            roomName = "Kitchen",
            containerName = "Pantry",
        )
        assertEquals("Home › Kitchen › Pantry", path.format())
    }

    @Test
    fun format_omitsContainerWhenNull() {
        val path = LocationPath(houseName = "Home", roomName = "Garage")
        assertEquals("Home › Garage", path.format())
    }
}
