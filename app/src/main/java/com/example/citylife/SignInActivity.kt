package com.example.citylife

import android.content.Context
import android.content.Intent
import android.location.Location
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
import androidx.core.content.ContextCompat
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import com.example.citylife.signIn.SignIn
import com.example.citylife.ui.theme.CityLifeTheme
import com.example.citylife.utils.UserSerialization
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

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

var signedInUser: User =  User("", 0.0f, Location(""), emptyList<ReportType>().toMutableList())

@Composable
fun SignInUI(context: Context) {

    val emailIconPainter = Icons.Default.Email
    val passwordIconPainter = Icons.Default.Info
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val workerPool: ExecutorService = Executors.newSingleThreadExecutor()

    fun signInButtonClick(email: String, password: String): Future<User> {
        return workerPool.submit(Callable {
            SignIn(email, password).signIn()
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
                signedInUser = signInButtonClick(email, password).get()
                if ( signedInUser.username != "" ) {
                    Toast.makeText(context, "Username ---> " + signedInUser.username, Toast.LENGTH_LONG).show()

                    val reportsListActivity = Intent(context, ReportsListActivity::class.java)
                    reportsListActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    reportsListActivity.putExtra("user", UserSerialization().serialize(signedInUser)) // Valutare passaggio oggetto intero serializzato o formato GSON
                    ContextCompat.startActivity(context, reportsListActivity, null)
                } else {
                    Toast.makeText(context, "AUTENTICAZIONE FALLITA", Toast.LENGTH_LONG).show()
                }
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