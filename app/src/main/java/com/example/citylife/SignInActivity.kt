package com.example.citylife

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.citylife.model.user.User
import com.example.citylife.signIn.SignIn
import com.example.citylife.ui.theme.CityLifeTheme
import kotlinx.coroutines.launch

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignInUI(applicationContext)
                }
            }
        }
    }
}


suspend fun retrieveUser(email: String, password: String): User? {
    return SignIn(email, password).signIn()
}

@Composable
fun SignInUI(context: Context) {

    val coroutineScope = rememberCoroutineScope()
    val user = remember { mutableStateOf<User?>(null) }

    val emailIconPainter = Icons.Default.Email
    val passwordIconPainter = Icons.Default.Info
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signInButtonClick: (email: String, password: String) -> Unit = { s: String, s1: String ->
        coroutineScope.launch {
            val user = retrieveUser(email, password)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign in",
            textAlign = TextAlign.Left,
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)

        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            leadingIcon = { Icon(emailIconPainter, contentDescription = "email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            leadingIcon = { Icon(passwordIconPainter, contentDescription = "password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedButton(
            onClick = {
                signInButtonClick(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        ) {
            Text(
                text = "Sign in",
                textAlign = TextAlign.Center
            )
        }
    }
}
/*
var username = ""
lateinit var user: User

fun signInButtonClick(email: String, password: String, context: Context) {


/*
    val workerPool: ExecutorService = Executors.newSingleThreadExecutor()
    workerPool.submit {

    }*/
}*/



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview2() {
    CityLifeTheme {
        SignInUI(LocalContext.current)
    }
}