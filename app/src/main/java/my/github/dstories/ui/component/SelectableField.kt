package my.github.dstories.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectableField(
    modifier: Modifier = Modifier,
    labelText: String,
    selectedValue: String,
    dropDownContent: @Composable (dismiss: () -> Unit) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val (focusRequester) = FocusRequester.createRefs()

    Box(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            value = selectedValue,
            onValueChange = { },
            label = { Text(labelText) },
            readOnly = true,
            singleLine = true,
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable(
                    onClick = {
                        expanded.value = !expanded.value
                        focusRequester.requestFocus()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
                focusManager.clearFocus()
            }
        ) {
            dropDownContent {
                expanded.value = false
                focusManager.clearFocus()
            }
        }
    }


}
