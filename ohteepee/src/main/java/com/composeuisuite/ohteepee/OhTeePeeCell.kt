package com.composeuisuite.ohteepee

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.composeuisuite.ohteepee.configuration.CellConfigurations

@Composable
internal fun OhTeePeeCell(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    cellConfigurations: CellConfigurations,
    modifier: Modifier = Modifier,
    isErrorOccurred: Boolean = false,
    onFocusChanged: ((isFocused: Boolean) -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val cellConfigurationState by remember(
        key1 = value,
        key2 = isFocused,
        key3 = isErrorOccurred
    ) {
        val config = when {
            isErrorOccurred -> cellConfigurations.errorCellConfig
            isFocused -> cellConfigurations.activeCellConfig
            value.isNotEmpty() -> cellConfigurations.filledCellConfig
            else -> cellConfigurations.emptyCellConfig
        }
        mutableStateOf(config)
    }

    val textFieldValue = remember(value) {
        TextFieldValue(
            text = value,
            selection = TextRange(value.length),
        )
    }

    Surface(
        modifier = modifier
            .width(cellConfigurations.width)
            .height(cellConfigurations.height)
            .border(
                border = BorderStroke(
                    width = cellConfigurationState.borderWidth,
                    color = cellConfigurationState.borderColor
                ),
                shape = cellConfigurationState.shape
            ),
        elevation = cellConfigurations.elevation,
        shape = cellConfigurationState.shape,
    ) {
        TextField(
            modifier = Modifier
//            .absolutePadding(left = 2.dp, right = 1.dp)
                .onFocusEvent {
                    onFocusChanged?.invoke(it.isFocused)
                    isFocused = it.isFocused
                },
            singleLine = true,
            value = textFieldValue,
            onValueChange = { onValueChange(it.text) },
            keyboardActions = KeyboardActions(
                onNext = {},
                onDone = {}
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = cellConfigurationState.textStyle.color,
                backgroundColor = cellConfigurationState.backgroundColor,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent,
            ),
            textStyle = cellConfigurationState.textStyle.copy(textAlign = TextAlign.Center)
        )
    }
}