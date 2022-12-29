package com.example.citylife

import com.example.citylife.model.report.ReportType

data class ListItem(
    var title: ReportType,
    var isSelected: Boolean
)