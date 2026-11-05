package com.socialquest.app
import com.socialquest.app.ui.components.SocialQuestSplash
import androidx.compose.runtime.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.socialquest.app.navigation.AppNav
import com.socialquest.app.ui.theme.SocialquestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SocialquestTheme {
                var showSplash by remember {
                    mutableStateOf(true)
                }

                if (showSplash) {
                    SocialQuestSplash(
                        onFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    AppNav()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SocialquestTheme {
        Greeting("Android")
    }
}
