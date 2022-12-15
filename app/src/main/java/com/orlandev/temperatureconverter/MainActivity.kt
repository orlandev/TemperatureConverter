package com.orlandev.temperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.orlandev.temperatureconverter.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TemperatureApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureTextField(
    temperature: MutableState<String>, modifier: Modifier = Modifier, callback: () -> Unit
) {

    TextField(modifier = modifier,
        value = temperature.value,
        onValueChange = { temperature.value = it },
        keyboardActions = KeyboardActions(onAny = { callback() }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun TemperatureRadioButton(
    selected: Boolean, resID: Int, onClick: (Int) -> Unit, modifier: Modifier = Modifier

) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {

        RadioButton(selected = selected, onClick = { onClick(resID) })
        Text(text = stringResource(id = resID), modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun TemperatureScaleButtonGroup(
    selected: MutableState<Int>, modifier: Modifier = Modifier
) {
    val sel = selected.value
    val onClick = { resId: Int -> selected.value = resId }
    Row(modifier = modifier) {
        TemperatureRadioButton(
            selected = sel == R.string.celsius, resID = R.string.celsius, onClick = onClick
        )
        TemperatureRadioButton(
            selected = sel == R.string.fahrenheit,
            resID = R.string.fahrenheit,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TemperatureApp() {

    val temperature = remember {
        mutableStateOf("")
    }
    val scale = remember {
        mutableStateOf(R.string.celsius)
    }

    var convertedTemperature by remember {
        mutableStateOf(0f)
    }


    val calc = {
        val temp = temperature.value.toFloat()
        convertedTemperature = if (scale.value == R.string.celsius) (temp * 1.8F) + 32F
        else (temp - 32F) / 1.8F
    }

    val result = remember(convertedTemperature) {
        if (convertedTemperature.isNaN()) ""
        else "${convertedTemperature}${
            if (scale.value == R.string.celsius) "F"
            else "C"
        }"
    }

    val enabled = temperature.value.isNotBlank()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TemperatureTextField(
                temperature = temperature,
                modifier = Modifier.padding(bottom = 16.dp),
                callback = calc
            )
            TemperatureScaleButtonGroup(
                selected = scale, modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = calc, enabled = enabled
            ) {
                Text(text = stringResource(id = R.string.convert_text))
            }
            if (result.isNotEmpty()) {
                Text(
                    text = result
                )
            }
        }
    }
}











