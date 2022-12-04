package com.codelabs.basicstatecodelab

import androidx.lifecycle.viewmodel.compose.viewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier = modifier) {
        StatefulCounter()

        WellnessTasksList(
            list = wellnessViewModel.tasks,
            onCloseTask = {
                    task -> wellnessViewModel.remove(task)
            },
            onCheckedTask = {
                task, checked -> wellnessViewModel.changeTaskChecked(task, checked)
            }

        )
    }
}

/**
 * Composition
 * : 컴포저블 함수들을 실행할 때 Compose가 만드는 ui의 description
 *
 * Initial composition
 * : 컴포저블 함수들을 처음으로 돌려서 Composition을 만드는 것
 *
 * Recomposition
 * : 데이터가 바뀌었을 때 컴포저블 함수들을 다시 돌려서 Composition을 업데이트하는 것
 */
@Composable
fun WaterCounter(modifier: Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        /**
         * remember
         * : Composition에 object를 save하고,
         *   remember가 호출되는 코드가 recomposition되는 동안 invoke되지 않으면 forget함
         */
        var count by rememberSaveable{ mutableStateOf(0) }

        if (count > 0) {
            Text("You've had $count glasses.")
        }

        Button(
            onClick = { count++ },
            Modifier.padding(top = 8.dp),
            enabled = count < 10
        ) {
            Text("Add one")
        }
    }
}


/**
 * Stateless Composable
 * : state를 들고 있지 않는 컴포저블 함수
 * : state hoisting을 이용하면 stateless한 컴포저블을 만들 수 있음
 *
 * State hoisting
 * : 컴포저블을 stateless하게 만들기 위해서 state를 composable 함수를 호출하는 애쪽으로 옮기는 패턴
 *
 * 컴포즈에서 state hoisting하는 방법: State 변수를 아래 두 파라미터로 바꾼다.
 * (1) value: T : 보여주고 있는 현재 값
 * (2) onValueCahnge: (T) -> Unit : value가 새로운 값으로 변하게되는 이벤트
 *
 * 위처럼 hoiste는 state의 특징
 * (1) Single source of truth : state를 '복붙'하는게 아니라 caller쪽으로 '이동'시킴으로써, ssot가 보장됨
 * (2) Shareable: hoist된 state는 여러 컴포저블 함수들이 '공유'할 수 있음
 * (3) Interceptable: stateless한 컴포저블 함수의 state를 바꿀지말지 caller쪽에서 결정할 수 있음
 * (4) Decoupled: stateless한 컴포저블 함수는 뷰모델에 저장될 수 있음
 *
 * Stateless vs Stateful
 * Stateless composable
 * : state를 정의하거나 수정하지 않는, 어떤 state도 들고 있지 않은 컴포저블 함수
 *
 * Stateful composable
 * : 시간이 지나면서 변하는 state를 들고 있는 컴포저블 함수
 */
@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text("You've had $count glasses")
        }
        Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
            Text("Add one")
        }
    }
}

@Composable
fun StatefulCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count = count, onIncrement = { count++ }, modifier)
}

