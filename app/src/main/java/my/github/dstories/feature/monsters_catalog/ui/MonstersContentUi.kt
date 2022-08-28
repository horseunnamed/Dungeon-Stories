package my.github.dstories.feature.monsters_catalog.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.github.dstories.R
import my.github.dstories.core.model.Monster
import my.github.dstories.core.ui.component.forEachWithSpacers

@Composable
fun MonstersContent(
    monsters: List<Monster.Preview>,
    onMonsterClick: (Monster.Preview) -> Unit
) {
    if (monsters.isEmpty()) {
        MonstersEmptyContent()
    } else {
        LazyColumn {
            item("top_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
            monsters.forEachWithSpacers(
                onSpacer = { prevIndex, _ ->
                    item("${prevIndex}_spacer") {
                        Spacer(Modifier.height(8.dp))
                    }
                },
                onItem = { monster ->
                    item(monster.index) {
                        MonsterCard(monster = monster, onClick = { onMonsterClick(monster) })
                    }
                }
            )
            item("bottom_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MonstersEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.puffing_dragon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No monsters found :(",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Try to change search",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonsterCard(monster: Monster.Preview, onClick: () -> Unit) {
    val monsterDescription = with(monster) {
        "${type.name} • ${monster.challengeRating} Challenge\n" +
                "$hitPoints HP • $armorClass AC"
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = { onClick() }
    ) {
        Row(Modifier.wrapContentHeight()) {
            Column(
                Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
                    .weight(1f)
            ) {
                Text(monster.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(monsterDescription, style = MaterialTheme.typography.bodyMedium)
            }
            if (monster.portrait != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outline),
                    model = monster.portrait.value,
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
        }
    }
}
