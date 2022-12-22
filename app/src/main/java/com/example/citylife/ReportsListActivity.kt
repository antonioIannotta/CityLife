package com.example.citylife

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.citylife.model.user.User
import com.example.citylife.ui.theme.CityLifeTheme
import com.example.citylife.utils.UserSerialization


class ReportsListActivity : ComponentActivity() {
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
                    ReportsListUI(serializedUser!!)
                }
            }
        }
    }
}

/*fun deserializeUser(serializedUser : String): User {


    return user
}*/

@Composable
fun ReportsListUI(serializedUser: String, names: List<String> = List(50){"$it"}) {

    //UserSerialization().deserialize(serializedUser)

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) {
            name -> ReportItem("Title", "Description text", "5th avenue", painterResource(
            id = R.drawable.smart_city_logo), "15.56")
        }
    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview6() {
    CityLifeTheme {
        ReportsListUI("")
    }
}