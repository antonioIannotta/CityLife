package com.example.citylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.citylife.ui.theme.CityLifeTheme
import androidx.compose.foundation.Image as Image


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpSignIn(applicationContext)
                }
            }
        }
    }
}

@Composable
fun SignUpSignIn(context: Context) {

    val logoImagePainter = painterResource(id = R.drawable.smart_city_logo)
    val signInActivityIntent = Intent(context, SignInActivity::class.java)
    val signUpActivityIntent = Intent(context, SignUpActivity::class.java)
    signInActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    signUpActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column {
            Image(
                logoImagePainter,
                contentDescription = "app logo",
                modifier = Modifier.fillMaxWidth())
            Button (
                onClick = { startActivity(context, signInActivityIntent, null) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
            ) {
                Text(text = "Sign in",color = Color.White)
            }
            Button(
                onClick = { startActivity(context, signUpActivityIntent, null) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
            ) {
                Text(text = "Sign up",color = Color.White)
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    CityLifeTheme {
        SignUpSignIn(LocalContext.current)
    }
}