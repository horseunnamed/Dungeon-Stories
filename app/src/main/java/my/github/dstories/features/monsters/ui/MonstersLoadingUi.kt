package my.github.dstories.features.monsters.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.github.dstories.ui.component.Skeleton

@Composable
fun MonstersLoadingColumn() {
    Column(Modifier.padding(16.dp)) {
        MonsterPlaceholderCard()
        Spacer(Modifier.height(8.dp))
        MonsterPlaceholderCard()
        Spacer(Modifier.height(8.dp))
        MonsterPlaceholderCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonsterPlaceholderCard() {
    OutlinedCard(
        Modifier.fillMaxWidth()
    ) {
        Row(Modifier.wrapContentHeight()) {
            Column(
                Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
                    .weight(1f)
            ) {
                Skeleton(
                    Modifier
                        .height(24.dp)
                        .width(57.dp)
                )
                Spacer(Modifier.height(4.dp))
                Skeleton(
                    Modifier
                        .height(40.dp)
                        .width(164.dp)
                )
            }
            Skeleton(
                modifier = Modifier.size(100.dp),
                cornerRadius = 0.dp
            )
        }
    }
}
