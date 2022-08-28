package my.github.dstories.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownChip(
    modifier: Modifier = Modifier,
    labelText: String,
    selected: Boolean,
    dropdownContent: @Composable (dismiss: () -> Unit) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box(modifier) {
        FilterChip(
            selected = selected,
            onClick = { expanded.value = !expanded.value },
            label = { Text(labelText) },
            selectedIcon = { ChipIcon(Icons.Default.Check) },
            trailingIcon = { ChipIcon(Icons.Default.ArrowDropDown) },
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            dropdownContent {
                expanded.value = false
            }
        }
    }
}