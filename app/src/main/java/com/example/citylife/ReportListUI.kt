package com.example.citylife

import android.app.Activity
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.citylife.model.report.ClientReportDB
import com.example.citylife.utils.UserSerialization
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Composable
fun ReportsListUI(serializedUser: String) {
    var user = UserSerialization().deserialize(serializedUser)
    var notificationList: MutableList<ClientReportDB> by remember { mutableStateOf(user.notificationList) }

    println("avvio thread")

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        Activity().runOnUiThread(Runnable {
            while(true) {
                runBlocking {
                    println("sto per eseguire la receive report ----> ")
                    notificationList = user.receiveReport()
                    items(items = notificationList) {
                            notification -> ReportItem(notification.type, notification.text, notification.location, painterResource(
                        id = R.drawable.smart_city_logo), notification.localDateTime)
                    }
                }
            }
        })
    }

    /*Executors.newSingleThreadExecutor().submit(Runnable {
        while(true) {
            runBlocking {
                println("sto per eseguire la receive report ----> ")
                notificationList = user.receiveReport()
            }
        }
    })

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = notificationList) {
                notification -> ReportItem(notification.type, notification.text, notification.location, painterResource(
            id = R.drawable.smart_city_logo), notification.localDateTime)
        }
        println("ho finito tutto")

    }*/
}

@Composable
fun ReportItem(reportTitle: String, reportText: String, reportLocation: String, reportPicturePainter: Painter = painterResource(
    id = R.drawable.smart_city_logo), reportLocalDateTime: String) {

    val rightArrowPainter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24)

    Surface(
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Row() {
                Image(
                    painter = reportPicturePainter,
                    contentDescription = "report image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .weight(0.3f)
                )
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(start = 15.dp)
                ) {

                    Row() {
                        Text(
                            text = reportTitle,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "• $reportLocalDateTime",
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start= 5.dp, top = 8.dp)
                        )
                    }
                    Text(text = reportText)

                }
                Column(
                    modifier = Modifier
                        .weight(0.2f),
                ) {
                    TextButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            painter = rightArrowPainter,
                            contentDescription = "right arrow to map activity",
                        )
                    }
                }
            }
        }
    }
}