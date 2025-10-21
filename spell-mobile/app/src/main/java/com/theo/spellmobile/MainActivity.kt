package com.theo.spellmobile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theo.spellmobile.ui.theme.SpellMobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpellMobileTheme {
                mainApp()
            }
        }
    }
}

@Composable
fun mainApp() {
    Scaffold { innerPadding ->
        val pad = innerPadding
        var inputed by remember { mutableStateOf(false) }
        var userInfo by remember { mutableStateOf(arrayOf("", "")) }
        val coroutineScope = rememberCoroutineScope()

        // Added for debug
        var lastRequestStatus by remember { mutableStateOf<String?>(null) }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(color = Color.LightGray)
            )
            // Show request status if available
            lastRequestStatus?.let { status ->
                Text(
                    text = "Last request: $status",
                    color = if (status == "Success") androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
                )
            }
            
            if (!inputed) {
                Column {
                    var username by rememberSaveable() { mutableStateOf("") }
                    var url by rememberSaveable() {mutableStateOf("")}
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username")}
                    )
                    TextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text("Url")}
                    )
                    Button(
                        onClick = {
                            userInfo = returnInfo(url, username);
                            inputed = !inputed; },
                    ) { Text("Submit") }
                }
            }
            if (inputed) {
                Greeting(userInfo[0])
                Text(userInfo[1])
                SpellList(userInfo[1], userInfo[0], coroutineScope) { status ->
                    lastRequestStatus = status
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                )
                    {
                        Button(
                            onClick = { inputed = !inputed },
                        ) { Text("Reset Connection") }
                        Box(
                            modifier = Modifier.height(35.dp).padding(20.dp).fillMaxWidth()
                        )

                    }
                }
            }
        }
    }

fun returnInfo(url: String, username: String): Array<String> {
    return arrayOf(username, url)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun SpellList(url: String, username: String, coroutineScope: CoroutineScope, onRequestComplete: (String) -> Unit) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SpellButton("Fireball", {castSpell(url, username, "fireball", coroutineScope, onRequestComplete)})
        SpellButton("Magic Missile", {castSpell(url, username, "magicmissile", coroutineScope, onRequestComplete)})
        SpellButton("Blinding Light", {castSpell(url, username, "blindinglight", coroutineScope, onRequestComplete)})
    }
}


fun castSpell(
    url: String,
    username: String,
    spell: String,
    coroutineScope: CoroutineScope,
    onRequestComplete: (String) -> Unit
) {
    val fullUrl = "$url/$username/$spell"

    coroutineScope.launch {
        val success = SimpleHttp.sendRequest(fullUrl)
        onRequestComplete(if (success) "Success" else "Failed")
    }
}

@Composable
fun SpellButton(text: String, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .padding(10.dp),
        onClick = onClick
    ) { Text(text) }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpellMobileTheme {
        mainApp()
    }
}