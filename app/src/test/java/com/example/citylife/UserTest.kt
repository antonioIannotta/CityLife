package com.example.citylife

import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import org.junit.Test
import org.junit.Assert.*

class UserTest {

    @Test
    fun getUsername() {
        val user = User("Antonio")
        assertEquals(user.username, "Antonio")
    }

    @Test
    suspend fun distanceTest() {
        val user = User("Antonio")
        user.changeDistance(5.0f)
        assertEquals(user.getDistance(), 5.0f)
    }

    @Test
    fun reportListTest() {
        var reportList = mutableListOf<ReportType>()

        reportList.add(ReportType.INCIDENT)
        assertEquals(reportList[0], ReportType.INCIDENT)
    }
}