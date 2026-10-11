package com.arielelbert.vpassignmentweek3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class RpsState { INITIAL, PICK, REVEAL, FINISHED }
enum class RpsChoice(val label: String, val emoji: String) {
    ROCK("Rock", "✊"),
    PAPER("Paper", "🖐️"),
    SCISSORS("Scissors", "✌️")
}

@Composable
fun RockPaperScissorsApp() {
    var gameState by remember { mutableStateOf(RpsState.INITIAL) }
    var playerScore by remember { mutableIntStateOf(0) }
    var cpuScore by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }
    val maxPoints = 3
    var playerChoice by remember { mutableStateOf<RpsChoice?>(null) }
    var cpuChoice by remember { mutableStateOf<RpsChoice?>(null) }
    var roundResult by remember { mutableStateOf("") }
    var randomizedButtons by remember { mutableStateOf(RpsChoice.entries.shuffled()) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (gameState) {
            RpsState.INITIAL -> StartRpsScreen(
                onStartClick = {
                    playerScore = 0
                    cpuScore = 0
                    randomizedButtons = RpsChoice.entries.shuffled()
                    gameState = RpsState.PICK
                }
            )
            RpsState.PICK, RpsState.REVEAL -> RunningRpsScreen(
                gameState = gameState,
                playerScore = playerScore,
                cpuScore = cpuScore,
                playerChoice = playerChoice,
                cpuChoice = cpuChoice,
                roundResult = roundResult,
                buttons = randomizedButtons,
                onChoiceSelected = { choice ->
                    playerChoice = choice
                    cpuChoice = RpsChoice.entries.random()
                    roundResult = when {
                        playerChoice == cpuChoice -> "Draw"
                        (playerChoice == RpsChoice.ROCK && cpuChoice == RpsChoice.SCISSORS) ||
                                (playerChoice == RpsChoice.PAPER && cpuChoice == RpsChoice.ROCK) ||
                                (playerChoice == RpsChoice.SCISSORS && cpuChoice == RpsChoice.PAPER) -> {
                            playerScore++
                            "You Win!"
                        }
                        else -> {
                            cpuScore++
                            "You Lose"
                        }
                    }

                    gameState = RpsState.REVEAL
                    coroutineScope.launch {
                        delay(700)
                        if (playerScore >= maxPoints || cpuScore >= maxPoints) {
                            val diff = playerScore - cpuScore
                            if (diff > bestScore) bestScore = diff
                            gameState = RpsState.FINISHED
                        } else {
                            randomizedButtons = RpsChoice.entries.shuffled()
                            gameState = RpsState.PICK
                        }
                    }
                }
            )
            RpsState.FINISHED -> GameOverRpsScreen(
                playerScore = playerScore,
                cpuScore = cpuScore,
                bestScore = bestScore,
                onRestart = {
                    playerScore = 0
                    cpuScore = 0
                    randomizedButtons = RpsChoice.entries.shuffled()
                    gameState = RpsState.PICK
                },
                onExit = {
                    gameState = RpsState.INITIAL
                }
            )
        }
    }
}

@Composable
fun StartRpsScreen(onStartClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🧑 0 - 0 🤖", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Best of 5", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(48.dp))

        Text("Rock • Paper • Scissors", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(160.dp)
        ) {
            Text("Start", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RunningRpsScreen(
    gameState: RpsState,
    playerScore: Int,
    cpuScore: Int,
    playerChoice: RpsChoice?,
    cpuChoice: RpsChoice?,
    roundResult: String,
    buttons: List<RpsChoice>,
    onChoiceSelected: (RpsChoice) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🧑 $playerScore - $cpuScore 🤖", fontWeight = FontWeight.Bold)
            Text("Best of 5", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(80.dp))

        if (gameState == RpsState.PICK) {
            Text("Pick your move!", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("? ", fontSize = 42.sp, color = Color.Gray)
                Text("VS", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                Text(" ?", fontSize = 42.sp, color = Color.Gray)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(playerChoice?.emoji ?: "", fontSize = 48.sp)
                Text(" VS ", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                Text(cpuChoice?.emoji ?: "", fontSize = 48.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(roundResult, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.weight(1f))

        if (gameState == RpsState.PICK) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                buttons.forEach { choice ->
                    Button(
                        onClick = { onChoiceSelected(choice) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("${choice.emoji} ${choice.label}", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun GameOverRpsScreen(
    playerScore: Int,
    cpuScore: Int,
    bestScore: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    val isWinMatch = playerScore > cpuScore

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🧑 $playerScore - $cpuScore 🤖", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (isWinMatch) "You Win the Match!" else "You Lose the Match",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Best Score: $bestScore", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRestart,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Restart", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onExit,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Exit", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, name = "1. RPS - Start Screen")
@Composable
fun PreviewRpsStart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        StartRpsScreen(onStartClick = {})
    }
}

@Preview(showBackground = true, name = "2. RPS - Pick Screen")
@Composable
fun PreviewRpsPick() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        RunningRpsScreen(
            gameState = RpsState.PICK,
            playerScore = 1,
            cpuScore = 0,
            playerChoice = null,
            cpuChoice = null,
            roundResult = "",
            buttons = RpsChoice.entries,
            onChoiceSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "3. RPS - Reveal Screen")
@Composable
fun PreviewRpsReveal() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        RunningRpsScreen(
            gameState = RpsState.REVEAL,
            playerScore = 1,
            cpuScore = 0,
            playerChoice = RpsChoice.PAPER,
            cpuChoice = RpsChoice.ROCK,
            roundResult = "You Win!",
            buttons = RpsChoice.entries,
            onChoiceSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "4. RPS - Game Over Screen")
@Composable
fun PreviewRpsGameOver() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        GameOverRpsScreen(
            playerScore = 3,
            cpuScore = 1,
            bestScore = 3,
            onRestart = {},
            onExit = {}
        )
    }
}