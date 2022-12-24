package com.example.citylife

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.citylife.bottomnavigation.BottomBarScreen
import com.example.citylife.model.report.ReportType
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
                    BottomNav()
                }
            }
        }
    }
}

@Composable
fun ReportsListUI(serializedUser: String, names: List<String> = List(50){"$it"}) {

    var user = UserSerialization().deserialize(serializedUser)

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


@Composable
fun BottomNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route =  BottomBarScreen.Home.route) {
            ReportsListActivity()
        }
        composable(route =  BottomBarScreen.Report.route) {
            UserSettingsUI()
        }
        composable(route =  BottomBarScreen.Profile.route) {
            UserSettingsUI()
        }
    }
}

@Composable
fun BottomNav() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        Modifier.padding(it)
        BottomNavGraph(
            navController = navController
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Report,
        BottomBarScreen.Profile
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    /*val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent*/

    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = if (selected) screen.icon_focused else screen.icon),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview6() {
    CityLifeTheme {
        var user = User("username", 0.0f, Location(""), emptyList<ReportType>().toMutableList())
        ReportsListUI(UserSerialization().serialize(user))
        BottomNav()
    }
}