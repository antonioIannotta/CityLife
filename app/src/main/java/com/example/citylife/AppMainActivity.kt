package com.example.citylife

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

class AppMainActivity : ComponentActivity() {
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
                    var user = UserSerialization().deserialize(serializedUser!!)

                    /* TODO questa è l'activity dove l'utente starà per più tempo subito dopo il
                        login per cui questo sia il posto giusto in cui valutare i cambiamenti
                         della location. Credo che sia più sensato ottenere tutto qui nel "main"
                         dell'activity piuttosto che dentro un metodo composable */

                    BottomNav(applicationContext, serializedUser!!)
                }
            }
        }
    }
}

@Composable
fun BottomNav(context: Context, serializedUser: String) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        Modifier.padding(it)
        BottomNavGraph(
            navController,
            serializedUser,
            context
        )
    }
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    serializedUser: String,
    context: Context
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route =  BottomBarScreen.Home.route) {
            ReportsListUI(serializedUser = serializedUser)
        }
        composable(route =  BottomBarScreen.Report.route) {
            /*UserSettingsUI(
                serializedUser = serializedUser,
                context = context
            )*/
        }
        composable(route =  BottomBarScreen.UserSettings.route) {
            UserSettingsUI(
                serializedUser = serializedUser,
                context = context
            )
        }
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Report,
        BottomBarScreen.UserSettings
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .background(Color.White)
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

    val contentColor =
        if (selected) Color.Red else Color.Black

    Box(
        modifier = Modifier
            .height(65.dp)
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
                modifier = Modifier.height(40.dp).width(40.dp),
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
fun AppMainPreview() {
    CityLifeTheme {
        var user = User("username", 0.0f, Location(""), emptyList<ReportType>().toMutableList())
        BottomNav(LocalContext.current, UserSerialization().serialize(user))
    }
}