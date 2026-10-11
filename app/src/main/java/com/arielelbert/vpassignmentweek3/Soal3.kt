package com.arielelbert.vpassignmentweek3

import androidx.compose.foundation.background
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
import kotlin.random.Random

enum class GameState2 { START, COUNTDOWN, RUNNING, GAMEOVER }
enum class GameMode { COLOR, TEXT }

enum class ColorItem(val label: String, val color: Color) {
    RED("RED", Color.Red),
    BLUE("BLUE", Color.Blue),
    GREEN("GREEN", Color(0xFF4CAF50)),
    ORANGE("ORANGE", Color(0xFFFF9800))
}

@Composable
fun ColorWordMatchingApp() {
    var gameState by remember { mutableStateOf(GameState2.START) }
    var gameMode by remember { mutableStateOf(GameMode.COLOR) }
    var score by remember { mutableIntStateOf(0) }
    var strikes by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }
    var countdownText by remember { mutableStateOf("3") }
    var timeLeft by remember { mutableLongStateOf(5000L) }
    var wordItem by remember { mutableStateOf(ColorItem.RED) }
    var inkItem by remember { mutableStateOf(ColorItem.BLUE) }
    var buttonLeftItem by remember { mutableStateOf(ColorItem.RED) }
    var buttonRightItem by remember { mutableStateOf(ColorItem.BLUE) }
    val coroutineScope = rememberCoroutineScope()
    fun generateNewQuestion() {
        gameMode = if (Random.nextBoolean()) GameMode.COLOR else GameMode.TEXT
        wordItem = ColorItem.entries.random()
        inkItem = ColorItem.entries.random()

        val correctAnswer = if (gameMode == GameMode.COLOR) inkItem else wordItem
        val wrongAnswer = ColorItem.entries.filter { it != correctAnswer }.random()
        val isSwapped = Random.nextBoolean()
        if (isSwapped) {
            buttonLeftItem = wrongAnswer
            buttonRightItem = correctAnswer
        } else {
            buttonLeftItem = correctAnswer
            buttonRightItem = wrongAnswer
        }

        timeLeft = 5000L
    }

    LaunchedEffect(gameState, score, strikes, wordItem) {
        if (gameState == GameState2.RUNNING) {
            while (timeLeft > 0) {
                delay(100L)
                timeLeft -= 100L
            }
            strikes++
            if (strikes >= 3) {
                if (score > bestScore) bestScore = score
                gameState = GameState2.GAMEOVER
            } else {
                generateNewQuestion()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when (gameState) {
            GameState2.START -> StartScreen(
                onStartClick = {
                    score = 0
                    strikes = 0
                    coroutineScope.launch {
                        gameState = GameState2.COUNTDOWN
                        countdownText = "3"
                        delay(700)
                        countdownText = "2"
                        delay(700)
                        countdownText = "1"
                        delay(700)
                        countdownText = "Start!"
                        delay(700)
                        generateNewQuestion()
                        gameState = GameState2.RUNNING
                    }
                }
            )
            GameState2.COUNTDOWN -> CountdownScreen(text = countdownText)
            GameState2.RUNNING -> RunningScreen(
                mode = gameMode,
                score = score,
                strikes = strikes,
                timeLeftMs = timeLeft,
                wordItem = wordItem,
                inkItem = inkItem,
                buttonLeft = buttonLeftItem,
                buttonRight = buttonRightItem,
                onAnswerSelected = { selectedChoice ->
                    val correctAnswer = if (gameMode == GameMode.COLOR) inkItem else wordItem
                    if (selectedChoice == correctAnswer) {
                        score++
                    } else {
                        strikes++
                    }

                    if (strikes >= 3) {
                        if (score > bestScore) bestScore = score
                        gameState = GameState2.GAMEOVER
                    } else {
                        generateNewQuestion()
                    }
                }
            )
            GameState2.GAMEOVER -> GameOverScreen(
                score = score,
                bestScore = bestScore,
                onRestart = {
                    score = 0
                    strikes = 0
                    generateNewQuestion()
                    gameState = GameState2.RUNNING
                },
                onExit = {
                    gameState = GameState2.START
                }
            )
        }
    }
}

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Welcome", fontSize = 24.sp, color = Color.Gray)
        Text("to", fontSize = 20.sp, color = Color.Gray)
        Text("Color Word Matching", fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 32.dp))

        Button(
            onClick = onStartClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text("Start Game", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun CountdownScreen(text: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = text, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
    }
}

@Composable
fun RunningScreen(
    mode: GameMode,
    score: Int,
    strikes: Int,
    timeLeftMs: Long,
    wordItem: ColorItem,
    inkItem: ColorItem,
    buttonLeft: ColorItem,
    buttonRight: ColorItem,
    onAnswerSelected: (ColorItem) -> Unit
) {
    val secondsLeft = (timeLeftMs / 1000).coerceAtLeast(0L)
    val clueText = if (mode == GameMode.COLOR) "Pilih WARNA TINTA" else "Pilih NAMA KATA"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Mode: $mode", fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Row {
                Text(text = "✅ $score", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "❌ $strikes/3", fontWeight = FontWeight.Bold, color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = clueText, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(64.dp))

        Text(text = "$secondsLeft s", fontSize = 24.sp, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = wordItem.label,
            fontSize = 54.sp,
            fontWeight = FontWeight.Bold,
            color = inkItem.color
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onAnswerSelected(buttonLeft) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.width(140.dp).height(50.dp)
            ) {
                Text(text = buttonLeft.label, color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onAnswerSelected(buttonRight) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.width(140.dp).height(50.dp)
            ) {
                Text(text = buttonRight.label, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun GameOverScreen(score: Int, bestScore: Int, onRestart: () -> Unit, onExit: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text("Game Over!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(32.dp))
        Text("You're Score", fontSize = 16.sp, color = Color.Gray)
        Text(text = "$score", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Best Score", fontSize = 14.sp, color = Color.Gray)
        Text(text = "$bestScore", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRestart,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Restart Game", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onExit,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Exit", color = Color.Black)
        }
    }
}

@Preview(showBackground = true, name = "1. Stroop - Start Screen")
@Composable
fun PreviewStroopStart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        StartScreen(onStartClick = {})
    }
}

@Preview(showBackground = true, name = "2. Stroop - Countdown Screen")
@Composable
fun PreviewStroopCountdown() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CountdownScreen(text = "3")
    }
}

@Preview(showBackground = true, name = "3. Stroop - Running Screen (Color Mode)")
@Composable
fun PreviewStroopRunning() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        RunningScreen(
            mode = GameMode.COLOR,
            score = 5,
            strikes = 1,
            timeLeftMs = 3000L, // 3 detik tersisa
            wordItem = ColorItem.RED,
            inkItem = ColorItem.BLUE,
            buttonLeft = ColorItem.BLUE,
            buttonRight = ColorItem.RED,
            onAnswerSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "4. Stroop - Game Over Screen")
@Composable
fun PreviewStroopGameOver() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        GameOverScreen(
            score = 5,
            bestScore = 5,
            onRestart = {},
            onExit = {}
        )
    }
}