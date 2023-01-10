package com.example.citylife

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.citylife.model.report.ClientReportDB
import com.example.citylife.model.report.ReportType
import com.example.citylife.utils.UserSerialization
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.*
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportsListUI(serializedUser: String) {
    var user = UserSerialization().deserialize(serializedUser)
    var notificationList: MutableList<ClientReportDB> by remember { mutableStateOf(user.notificationList) }

    LazyColumn(
        modifier = Modifier
            .background(color = Color.hsl(0f, 0f, 0.95f))
            .padding(top = 5.dp, bottom = 5.dp)

    ) {
        runBlocking {
            notificationList = user.receiveReport()
            items(items = notificationList) { notification ->

                val reportIcon = when(notification.type) {
                    ReportType.SHOP_DAMAGE.toString() -> painterResource(id = R.drawable.damaged_infrastructure)
                    ReportType.CROWD.toString() -> painterResource(id = R.drawable.crowd)
                    ReportType.ANIMALS.toString() -> painterResource(id = R.drawable.animals)
                    ReportType.ROBBERY.toString() -> painterResource(id = R.drawable.robbery)
                    ReportType.ENVIRONMENT.toString() -> painterResource(id = R.drawable.city)
                    ReportType.INCIDENT.toString() -> painterResource(id = R.drawable.car_accident)
                    ReportType.IMPASSABLE_STREET.toString() -> painterResource(id = R.drawable.impassable_street)
                    ReportType.UNSAFE_AREA.toString() -> painterResource(id = R.drawable.unsafe_area)
                    else -> painterResource(id = R.drawable.infrastructure)
                }

                ReportItem(notification.type, notification.text, notification.location, reportIcon, notification.localDateTime)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportItem(reportTitle: String, reportText: String, reportLocation: String, reportPicturePainter: Painter = painterResource(
    id = R.drawable.smart_city_logo), reportLocalDateTime: String) {

    val timeStamp:String = ISO_INSTANT.format(Instant.now()).dropLast(1)

    val dateTime = LocalDateTime.parse(reportLocalDateTime)
    val dateTimeNow = LocalDateTime.parse(timeStamp)

    val reportPostingTime = when (dateTime.until(dateTimeNow, ChronoUnit.HOURS).toInt()) {
        0 -> "${dateTime.until(dateTimeNow, ChronoUnit.MINUTES)}min"
        in 1..23 -> "${dateTime.until(dateTimeNow, ChronoUnit.HOURS)}h"
        else -> "${dateTime.until(dateTimeNow, ChronoUnit.DAYS)}d"
    }

    val rightArrowPainter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24)

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row() {
                TextButton(
                    onClick = { /*TODO*/ },
                ) {
                    Image(
                        painter = reportPicturePainter,
                        contentDescription = "report image",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(60.dp)
                            .weight(0.2f)
                    )
                    Column(
                        modifier = Modifier
                            .weight(0.8f)
                            .height(80.dp)
                            .padding(start = 12.dp, end = 12.dp)
                    ) {

                        Row() {
                            Text(
                                text = reportTitle,
                                color = Color.DarkGray,
                                fontSize = 16.sp,
                            )
                            Text(
                                text = "• $reportPostingTime",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                            )
                        }
                        Text(
                            text = reportText,
                            fontSize = 11.sp,
                            color = Color.Gray,
                        )
                    }
                    Icon(
                        painter = rightArrowPainter,
                        contentDescription = "right arrow to map activity",
                    )
                }
            }
        }
    }
}