package com.example.citylife

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.citylife.model.user.User
import com.example.citylife.utils.UserSerialization
import kotlinx.coroutines.runBlocking

@Composable
fun UserSettingsUI(context: Context, serializedUser: String) {

    var userIcon = painterResource(id = R.drawable.ic_round_supervised_user_circle_24)
    val rightArrowPainter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24)

    var user: User = UserSerialization().deserialize(serializedUser)
    var reportRange by remember { mutableStateOf(1f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 20.dp)
            ) {
                Icon(
                    painter = userIcon,
                    contentDescription = "user icon",
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = user.username,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(

        ) {

            Row(
                modifier = Modifier
                    .padding(8.dp, 8.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Increase distance by ",
                    modifier = Modifier.weight(8f),
                    color = Color.Gray
                )
                Text(
                    text = reportRange.toString() + "Km",
                    modifier = Modifier.weight(2f),
                    color = Color.Gray
                )
            }
            Slider(value = reportRange, onValueChange = {
                    reportRange = it
                    runBlocking {
                        user.changeDistance(it)
                    }
                }
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        val selectReportTypeActivity = Intent(context, SelectReportTypeActivity::class.java)
                        selectReportTypeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        selectReportTypeActivity.putExtra("user", UserSerialization().serialize(user))
                        ContextCompat.startActivity(context, selectReportTypeActivity, null)
                    },
                ) {
                    Text(
                        text="Tipologia notifiche di interesse",
                        color = Color.Gray,
                        modifier = Modifier.weight(9f),
                    )
                    Icon(
                        painter = rightArrowPainter,
                        contentDescription = "right arrow to notification choice",
                        tint = Color.Gray,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}