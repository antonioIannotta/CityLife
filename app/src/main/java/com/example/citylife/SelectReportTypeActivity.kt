package com.example.citylife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                    ReportTypeListSelector()
                }
            }
        }
    }
}

@Composable
fun ReportTypeListSelector() {

    var checkPainter = painterResource(id = R.drawable.ic_baseline_check_24)

    var items by remember {
        mutableStateOf(
            ReportType.values().map {
                ListItem(
                    title = it,
                    isSelected = false
                )
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items.size) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                         items = items.mapIndexed { j, item ->
                             if (index == j) {
                                 item.copy(isSelected = !item.isSelected)
                             } else {
                                 item
                             }
                         } as MutableList<ListItem>
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = items[index].title.toString())
                if (items[index].isSelected) {
                    Icon(
                        checkPainter,
                        contentDescription = "Selected",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Divider(startIndent = 16.dp, modifier = Modifier.padding(end = 16.dp), thickness = 1f.dp, color = Color.DarkGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview6() {
    CityLifeTheme {
        ReportTypeListSelector()
    }
}