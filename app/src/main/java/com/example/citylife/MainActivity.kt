package com.example.citylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.example.citylife.ui.theme.CityLifeTheme


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
    val intent = Intent(context, SignInActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Column() {
        Button(onClick = { startActivity(context, intent, null) }) {

        }
        Button(onClick = { startActivity(context, intent, null) }) {

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