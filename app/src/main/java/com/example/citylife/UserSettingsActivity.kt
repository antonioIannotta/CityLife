package com.example.citylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.citylife.model.user.User
import com.example.citylife.ui.theme.CityLifeTheme
import com.example.citylife.utils.UserSerialization

class UserSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var user: User = User("gilberto")
                    UserSettingsUI(user.username, applicationContext)
                }
            }
        }
    }
}

@Composable
fun UserSettingsUI(username: String, context: Context) {

    var userIcon = painterResource(id = R.drawable.ic_round_supervised_user_circle_24)
    val rightArrowPainter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24)

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
                    text = username,
                    fontSize = 15.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Giga Chad",
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
            Slider(value = reportRange, onValueChange = { reportRange = it })
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        val selectReportTypeIntent = Intent(context, SelectReportTypeActivity::class.java)
                        selectReportTypeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        selectReportTypeIntent.putExtra("user", UserSerialization().serialize(signedInUser))
                        ContextCompat.startActivity(context, selectReportTypeIntent, null)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview5() {
    CityLifeTheme {
        var user: User = User("gilberto")
        UserSettingsUI(user.username, LocalContext.current)
    }
    //TODO: COMMENTARE IL CODICE!!
}