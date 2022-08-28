package my.github.dstories.features.monster.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.github.dstories.features.monster.model.Monster
import my.github.dstories.features.monsters.model.ChallengeRating
import my.github.dstories.features.monsters.model.MonsterType
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.framework.AsyncRes
import my.github.dstories.model.AbilityScore
import my.github.dstories.model.AbilityScoresValues
import my.github.dstories.model.ImagePath
import my.github.dstories.ui.component.HorizontalSpacer
import my.github.dstories.ui.component.VerticalSpacer
import my.github.dstories.ui.component.forEachWithSpacers
import my.github.dstories.ui.component.skeleton
import my.github.dstories.ui.theme.PreviewTheme

@Composable
fun MainMonsterInfoCard(
    modifier: Modifier = Modifier,
    monsterPreview: ShortMonster,
    monsterInfo: AsyncRes<Monster>
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (monsterPreview.portrait != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        model = monsterPreview.portrait.value,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }

                VerticalSpacer(8.dp)

                Text(text = monsterPreview.name, style = MaterialTheme.typography.titleMedium)
            }

            VerticalSpacer(16.dp)

            MonsterProperty(
                propertyName = "\uD83D\uDD25 Challenge Rating",
                propertyValue = monsterPreview.challengeRating.toString()
            )

            VerticalSpacer(4.dp)

            MonsterProperty(
                propertyName = "❤️ Hit Points",
                propertyValue = monsterPreview.hitPoints.toString()
            )

            VerticalSpacer(4.dp)

            MonsterProperty(
                propertyName = "\uD83D\uDEE1️ Armor Class",
                propertyValue = monsterPreview.armorClass.toString()
            )

            if (monsterInfo !is AsyncRes.Error) {
                VerticalSpacer(4.dp)

                MonsterProperty(
                    modifier = Modifier.skeleton(!monsterInfo.isReady),
                    propertyName = "⚔️ Hit Dice",
                    propertyValue = monsterInfo.res?.hitDie ?: "x".repeat(10)
                )

                VerticalSpacer(4.dp)

                MonsterProperty(
                    modifier = Modifier.skeleton(!monsterInfo.isReady),
                    propertyName = "\uD83D\uDCA8 Speed",
                    propertyValue = monsterInfo.res?.speed ?: "x".repeat(10)
                )
            }

        }
    }

}

@Composable
fun MonsterProperty(
    modifier: Modifier = Modifier,
    propertyName: String,
    propertyValue: String
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$propertyName:")
            }
            append(" $propertyValue")
        },
        style = MaterialTheme.typography.bodyMedium
    )

}

@Composable
fun AbilityScoresUi(
    modifier: Modifier = Modifier,
    abilityScoresValues: AbilityScoresValues?
) {
    Column(modifier = modifier) {
        AbilityScoresSpacer()
        VerticalSpacer(height = 16.dp)
        AbilityScoresRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            abilityScores = listOf(AbilityScore.Str, AbilityScore.Dex, AbilityScore.Con),
            abilityScoresValues = abilityScoresValues
        )
        VerticalSpacer(height = 16.dp)
        AbilityScoresRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            abilityScores = listOf(AbilityScore.Int, AbilityScore.Wis, AbilityScore.Cha),
            abilityScoresValues = abilityScoresValues
        )
        VerticalSpacer(height = 16.dp)
        AbilityScoresSpacer()
    }
}

@Composable
fun AbilityScoresRow(
    modifier: Modifier = Modifier,
    abilityScores: List<AbilityScore>,
    abilityScoresValues: AbilityScoresValues?
) {
    Row(modifier = modifier) {
        abilityScores.forEachWithSpacers(
            onSpacer = { _, _ -> HorizontalSpacer(width = 54.dp) },
            onItem = { abilityScore ->
                AbilityScoreUi(
                    name = abilityScore.name.uppercase(),
                    value = abilityScoresValues?.get(abilityScore)?.total ?: 100,
                    loading = abilityScoresValues == null
                )
            }
        )
    }
}

@Composable
fun AbilityScoresSpacer() {
    Spacer(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSurface)
            .height(1.dp)
            .fillMaxWidth()
    )
}

@Composable
fun AbilityScoreUi(name: String, value: Int, loading: Boolean) {
    Column(modifier = Modifier.skeleton(loading)) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


@Preview
@Composable
private fun MainMonsterInfoCardPreview() {
    PreviewTheme {
        MainMonsterInfoCard(
            modifier = Modifier.fillMaxWidth(),
            monsterPreview = ShortMonster(
                index = "adult_brass_dragon",
                name = "Adult Brass Dragon",
                type = MonsterType.Dragon,
                portrait = ImagePath("https://static.wikia.nocookie.net/forgottenrealms/images/2/2b/Monster_Manual_5e_-_Dragon%2C_Brass_-_p104.jpg"),
                hitPoints = 50,
                armorClass = 14,
                challengeRating = ChallengeRating(13.0)
            ),
            monsterInfo = AsyncRes.Ok(
                Monster(
                    index = "adult_brass_dragon",
                    name = "Adult Brass Dragon",
                    type = MonsterType.Dragon,
                    portrait = ImagePath("https://static.wikia.nocookie.net/forgottenrealms/images/2/2b/Monster_Manual_5e_-_Dragon%2C_Brass_-_p104.jpg"),
                    hitPoints = 50,
                    armorClass = 14,
                    challengeRating = ChallengeRating(13.0),
                    hitDie = "17d2",
                    speed = "Walk 40 ft, Fly 40 ft, Swim 40 ft",
                    abilityScores = AbilityScoresValues.Default
                )
            )
        )
    }
}
