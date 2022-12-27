package com.example.citylife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.citylife.bottomnavigation.BottomBarScreen.Home.title
import com.example.citylife.model.report.ReportType
import com.example.citylife.ui.theme.CityLifeTheme
import com.example.citylife.ui.theme.Teal200

class SelectReportTypeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ReporTypeListSelector("selector", emptyList<ReportType>().toMutableList())
                }
            }
        }
    }
}

@Composable
fun ReporTypeListSelector(name: String, reportTypeList: MutableList<ReportType>) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(reportTypeList.size) { reportType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        /* TODO */
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = reportType.toString())
                //if (reportTypeList[reportType.toString()].isSelected)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview6() {
    CityLifeTheme {
        ReporTypeListSelector("selector", emptyList<ReportType>().toMutableList())
    }
}