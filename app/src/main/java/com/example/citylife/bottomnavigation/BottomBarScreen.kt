package com.example.citylife.bottomnavigation

import com.example.citylife.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    // for home
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_baseline_home_24,
        icon_focused = R.drawable.ic_baseline_home_24
    )

    // for map
    object Map: BottomBarScreen(
        route = "map",
        title = "Map",
        icon = R.drawable.ic_baseline_location_on_24,
        icon_focused = R.drawable.ic_baseline_location_on_24
    )

    // for settings
    object UserSettings: BottomBarScreen(
        route = "usersettings",
        title = "UserSettings",
        icon = R.drawable.ic_baseline_report_24,
        icon_focused = R.drawable.ic_baseline_report_24
    )

    // for report
    object Report: BottomBarScreen(
        route = "report",
        title = "Report",
        icon = R.drawable.ic_baseline_settings_24,
        icon_focused = R.drawable.ic_baseline_settings_24
    )
}