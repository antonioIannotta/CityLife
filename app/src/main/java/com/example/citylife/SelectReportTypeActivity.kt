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
import com.example.citylife.model.user.User
import com.example.citylife.ui.theme.CityLifeTheme
import com.example.citylife.ui.theme.Teal200
import com.example.citylife.utils.UserSerialization
import kotlinx.coroutines.runBlocking

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
                    var serializedUser = intent.getStringExtra("user")
                    var user: User = UserSerialization().deserialize(serializedUser!!)
                    println(user.reportPreferences)
                    ReportTypeListSelector(user)
                }
            }
        }
    }
}

@Composable
fun ReportTypeListSelector(user: User) {

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
                                if (!item.isSelected) {
                                    runBlocking {
                                        user.addReportToPreferences(
                                            ReportType.valueOf(
                                                items[index].title.toString()
                                            )
                                        )
                                    }
                                } else {
                                    runBlocking {
                                        user.removeReportTypeFromPreferences(
                                            ReportType.valueOf(
                                                items[index].title.toString()
                                            )
                                        )
                                    }
                                }
                                item.copy(isSelected = !item.isSelected)
                            } else {
                                item
                            }
                        }
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = items[index].title.toString())
                if (user.reportPreferences.contains(items[index].title)) {
                    items[index].isSelected = true
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
        //ReportTypeListSelector(user = user)
    }
}