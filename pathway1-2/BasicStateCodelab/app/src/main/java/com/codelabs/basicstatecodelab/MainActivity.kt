package com.codelabs.basicstatecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.basicstatecodelab.ui.theme.BasicStateCodelabTheme

/**
 * Event
 * : 앱 내부/외부에서 발생하는 입력
 * : + 값을 보내는 센서, 네트워크 응답 등
 *
 * State vs Event
 * state
 * : ui에 어떤 것을 보여줘야하는지에 대해 description을 제공함
 * : 'is'
 *
 * event
 * : state가 변하게 되는 메커니즘
 * : ui를 변하게 함
 * : 'happen'
 *
 *
 */
class MainActivity : ComponentActivity() {

    private val viewModel: WellnessViewModel = WellnessViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicStateCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WellnessScreen( wellnessViewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicStateCodelabTheme {
        Greeting("Android")
    }
}