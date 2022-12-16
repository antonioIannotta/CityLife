package com.example.citylife

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import com.example.citylife.signUp.SignUp
import com.example.citylife.ui.theme.CityLifeTheme
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpUI(applicationContext)
                }
            }
        }
    }
}

lateinit var signedUpUser: User

@Composable
fun SignUpUI(context: Context) {

    val emailIconPainter = Icons.Default.Email
    val passwordIconPainter = Icons.Default.Info
    var surname by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val workerPool: ExecutorService = Executors.newSingleThreadExecutor()

    fun signUpButtonClick(name: String, surname: String, email: String, password: String): Future<User> {
        return workerPool.submit(Callable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SignUp(name, surname, email, password).signUp()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sign up",
            textAlign = TextAlign.Left,
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)

        )
        Row() {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("name") },
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 10.dp, end = 5.dp, top = 10.dp),
            )

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("surname") },
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 5.dp, bottom = 10.dp, top = 10.dp),
            )
        }

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
                signedUpUser = signUpButtonClick(name, surname, email, password).get()
                Toast.makeText(context, "Username ---> " + signedUpUser.username, Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        ) {
            Text(
                text = "Sign up",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview (
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview3() {
    CityLifeTheme {
        SignUpUI(LocalContext.current)
    }
}