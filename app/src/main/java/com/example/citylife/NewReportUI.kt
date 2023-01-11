package com.example.citylife

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import java.lang.reflect.Array.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun NewReportUI() {

    var reportType by remember { mutableStateOf("Report type") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ItemSelector()

        OutlinedTextField(
            value = "email",
            onValueChange = { },
            label = { Text("email") },
            leadingIcon = { },// Icon(emailIconPainter, contentDescription = "email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
        )

        OutlinedTextField(
            value = "password",
            onValueChange = { },
            label = { Text("password") },
            leadingIcon = { },//Icon(passwordIconPainter, contentDescription = "password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
        )

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        ) {
            Text(
                text = "Send Report",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ItemSelector () {

    var expanded by remember { mutableStateOf(false) }
    val list = listOf("kotlin", "java", "dart", "swift")
    var selectedItem by remember { mutableStateOf("Report type") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if(expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned() { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
        ) {
            Text(text = selectedItem)
            Icon(icon, "arrow icon")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {

            list.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedItem = label
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}