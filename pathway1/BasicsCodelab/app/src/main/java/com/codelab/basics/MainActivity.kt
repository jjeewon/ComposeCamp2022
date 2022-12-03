package com.codelab.basics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme
import com.codelab.basics.ui.theme.Blue
import com.codelab.basics.ui.theme.Navy
import com.codelab.basics.ui.theme.Shapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /**
             * BasicsCodelabTheme : Composable 함수를 스타일링ㅎ
             */
            BasicsCodelabTheme {
                // A surface container using the 'background' color from the theme
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboadingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}


/**
 * modifier : UI element가 부모 레이아웃 안에서 어떻게 layout, display, behave해야 하는지 설정해줌
 * weight modifier : weight가 없는 다른 element들 밀어내고 가능한 공간에 채워지도록 해줌
 *
 * recomposition
 * : data가 변경되었을 때, Compose가 새로운 데이터로 함수들을 재실행해서 ui를 업데이트하는 것.
 * : composable 각각에 어떤 데이터 필요한지 봐서 데이터 변한 컴포넌트들만 recompose하게 하고 데이터 안변한 애들은 스킵함
 *
 * Composable function execution order
 * : 순서 없이 실행됨 -> 코드 실행되는 순서에 의존하면 안됨
 * : 자주 실행됨 -> 함수가 recompose되는 횟수에 의존하면 안됨
 *
 * remember
 * : recomposition 하는 와중에 state를 보존하는 방법
 * : mutable state를 'remeber'하게 해줌
 * : state가 reset되지 않게 해줌
 * : composable 함수는 자동으로 state를 구독하기 때문에 state가 바뀌면 바뀐 state와 관련된 composable이
 *   알아서 recompose되어서 update된 ui를 보여줌
 *
 *   remember vs rememberSaveable
 *   rememberSaveable
 *   : configuration change & 프로세스가 죽을 때 state 날라감
 *
 *   rememberSaveable
 *    configuration change & 프로세스가 죽을 때 state 저장해줌
 */
@Composable
fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name, style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4)
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(id = R.string.show_less)
                } else {
                    stringResource(id = R.string.show_more)
                }
            )
        }
    }
}

    /**
     * State hoisting
     * hoist : 변수를 scope 내 상위로 끌어올려 선언을 먼저 하는 것
     * state hoisting
     * : state가 복제되는걸 막아줌
     * : 버그 생기는거 줄여줌
     * : composable 함수 재사용하는거 도와줌
     * : composable 함수를 테스트하기 쉽게 해줌
     *
     * compose에서는 parent에서 state 값을 공유하는게 아니라 state를 hoist 함
     * => 그 값에 접근해야하는 공통 조상(Onboarding->MyApp)으로 옮김
     */
    @Composable
    private fun MyApp(
        modifier: Modifier = Modifier,
    ) {
        var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

        Surface(modifier) {
            if (shouldShowOnboarding) {
                OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
            } else {
                Greetings()
            }
        }
    }

    @Preview
    @Composable
    private fun MyAppPreview() {
        BasicsCodelabTheme {
            MyApp(Modifier.fillMaxSize())
        }
    }


    /**
     * 그냥 Column : 스크롤할 수 없는 column
     * : 스크롤 안됨
     *
     * LazyColumn : 스크롤할 수 있는 column
     * : 스크롤 됨.
     * : 화면에 보이는 아이템들만 렌더링함
     * : 리사이클러뷰처럼 자식들을 재활용하지 않음. 사용자가 스크롤할 때마다 새로운 컴포저블을 emit함
     * : 재활용하지 않고 새로 emit하더라도 안드로이드 View보단 비용이 덜든다함..?
     */
    @Composable
    private fun Greetings(
        modifier: Modifier = Modifier,
        names: List<String> = List(1000) { "$it" }
    ) {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name = name)
            }
        }
    }

@Composable
private fun Greeting(name: String) {
    Card(
        contentColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}


    @Preview(showBackground = true, widthDp = 320)
    @Composable
    private fun GreetingPreview() {
        BasicsCodelabTheme {
            Greetings()
        }
    }

    @Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = UI_MODE_NIGHT_YES,
        name = "DARK"
    )
    @Composable
    fun DefaultPreview() {
        BasicsCodelabTheme {
            MyApp()
        }
    }
