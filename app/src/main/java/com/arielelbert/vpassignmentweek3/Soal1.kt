package com.arielelbert.vpassignmentweek3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class GameState{
    START, WAITING, CLICK_NOW, TRIAL_RESULT, FAIL, FINAL_RESULT
}

enum class ResultCategory(val message: String, val color: Color, val imageRes: Int) {
    FAST("DANG YOU ARE SO FAST BRO!", Color(0xFF4CAF50), R.drawable.hormat),
    GOOD("YOUR REFLEX IS GOOD", Color(0xFF2196F3), R.drawable.mantap),
    STANDARD("MEH LIKE OTHER PERSON", Color(0xFFFF9800), R.drawable.notbad),
    SNAIL("YOU LIKE A SNAIL BRO", Color(0xFFFF5722), R.drawable.cupu)
    }


@Composable
fun App() {
    var gameState by remember { mutableStateOf(GameState.START) }
    var trialResults by remember { mutableStateOf(listOf<Long>()) }
    var startTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
    var waitingJob by remember { mutableStateOf<Job?>(null) }

    fun getCategory(average: Long): ResultCategory {
        return when {
            average < 180 -> ResultCategory.FAST
            average < 280 -> ResultCategory.GOOD
            average < 450 -> ResultCategory.STANDARD
            else -> ResultCategory.SNAIL
        }
    }

    val backgroundColor = when (gameState) {
        GameState.START, GameState.TRIAL_RESULT -> Color(0xFF64C8D0)
        GameState.WAITING -> Color(0xFF9E9E9E)
        GameState.CLICK_NOW -> Color(0xFF4CAF50)
        GameState.FAIL -> Color(0xFFE53935)
        GameState.FINAL_RESULT -> {
            val avg = if (trialResults.isNotEmpty()) trialResults.average().toLong() else 0L
            getCategory(avg).color
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                when (gameState) {
                    GameState.START, GameState.TRIAL_RESULT -> {
                        gameState = GameState.WAITING
                        waitingJob = coroutineScope.launch {
                            val randomDelay = Random.nextLong(500, 4501)
                            delay(randomDelay)
                            gameState = GameState.CLICK_NOW
                            startTime = System.currentTimeMillis()
                        }
                    }

                    GameState.WAITING -> {
                        waitingJob?.cancel()
                        gameState = GameState.FAIL
                    }

                    GameState.CLICK_NOW -> {
                        val reactionTime = System.currentTimeMillis() - startTime
                        val newResults = trialResults + reactionTime
                        trialResults = newResults

                        gameState = if (newResults.size >= 3) {
                            GameState.FINAL_RESULT
                        } else {
                            GameState.TRIAL_RESULT
                        }
                    }

                    GameState.FAIL -> {
                        gameState = GameState.START
                    }

                    GameState.FINAL_RESULT -> {
                        trialResults = emptyList()
                        gameState = GameState.START
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (gameState) {
            GameState.START -> StartPage(trialResults = trialResults)
            GameState.WAITING -> WaitingPage()
            GameState.CLICK_NOW -> ClickNowPage()
            GameState.TRIAL_RESULT -> TrialResultPage(trialResults = trialResults)
            GameState.FAIL -> FailPage(trialResults = trialResults)
            GameState.FINAL_RESULT -> {
                val averageScore =
                    if (trialResults.isNotEmpty()) trialResults.average().toLong() else 0L
                FinalResultPage(
                    trialResults = trialResults,
                    average = averageScore,
                    category = getCategory(averageScore)
                )
            }
        }
    }
}

    @Composable
    fun StartPage(trialResults: List<Long>){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ){
            Text("Reaction", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            Icon(imageVector = Icons.Filled.ElectricBolt, contentDescription = "Start", tint = Color.White, modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Text("Test", color = Color.White, fontSize = 24.sp, modifier = Modifier.padding(bottom = 32.dp))
            Text("Click to Start", color = Color.White)

            if (trialResults.isNotEmpty()){
                Spacer(modifier = Modifier.height(32.dp))
                TrialResultsCard(results = trialResults)
        }
    }
}

@Composable
fun WaitingPage(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ){
        Text("Get Ready", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Icon(Icons.Filled.Warning, contentDescription = "Wait", tint = Color.White, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text("Wait for green light...", color = Color.White)
    }
}

@Composable
fun ClickNowPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text("GO!", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Icon(imageVector = Icons.Filled.DirectionsRun, contentDescription = "G0", tint = Color.White, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text("CLICK NOW!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("TAP AS FAST AS YOU CAN", color = Color.White)
    }
}

@Composable
fun TrialResultsCard(results: List<Long>, average: Long? = null) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Trial Results",
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 1..3) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(i.toString(), fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text(
                            text = if (i <= results.size) "${results[i - 1]}ms" else "-",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            if (average != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Average Score",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${average}ms",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun TrialResultPage(trialResults: List<Long>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text("Trial ${trialResults.size} Complete!", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Time: ${trialResults.lastOrNull() ?: 0}ms", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Continue to Trial ${trialResults.size + 1}", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        TrialResultsCard(results = trialResults)
    }
}

@Composable
fun FailPage(trialResults: List<Long>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text("FAIL!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Icon(Icons.Filled.ThumbDown, contentDescription = "Fail", tint = Color.White, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text("You clicked too early, TRY TO", color = Color.White)
        Text("READ THE RULE BRO", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text("TRY AGAIN", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TrialResultsCard(results = trialResults)
    }
}

@Composable
fun FinalResultPage(trialResults: List<Long>, average: Long, category: ResultCategory) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text(category.message, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Image(painter = painterResource(R.drawable.hormat), contentDescription = "Respect", modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Average: ${average}ms", color = Color.White, fontSize = 20.sp)
        Text("Click to Start New Test", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        TrialResultsCard(results = trialResults, average = average)
    }
}

@Composable
fun FinalResultContent(
    trialResults: List<Long>,
    average: Long,
    category: ResultCategory,
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(category.color)
            .clickable { onRestart() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = category.message,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = "Category Image",
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Average: ${average}ms",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Click to Start New Test",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            TrialResultsCard(results = trialResults, average = average)
        }
    }
}

@Preview(showBackground = true, name = "1. Start Screen")
@Composable
fun PreviewStartScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF64C8D0)), contentAlignment = Alignment.Center) {
        StartPage(trialResults = emptyList())
    }
}

@Preview(showBackground = true, name = "2. Waiting Screen")
@Composable
fun PreviewWaitingScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF9E9E9E)), contentAlignment = Alignment.Center) {
        WaitingPage()
    }
}

@Preview(showBackground = true, name = "3. Click Now Screen")
@Composable
fun PreviewClickNowScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF4CAF50)), contentAlignment = Alignment.Center) {
        ClickNowPage()
    }
}

@Preview(showBackground = true, name = "4. Trial Result Screen")
@Composable
fun PreviewTrialResultScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF64C8D0)), contentAlignment = Alignment.Center) {
        TrialResultPage(trialResults = listOf(436L))
    }
}

@Preview(showBackground = true, name = "5. Fail Screen")
@Composable
fun PreviewFailScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE53935)), contentAlignment = Alignment.Center) {
        FailPage(trialResults = listOf(436L))
    }
}

@Preview(showBackground = true, name = "Fast")
@Composable
fun PreviewFast() {
    FinalResultContent(
        trialResults = listOf(92L, 4L, 247L),
        average = 114L,
        category = ResultCategory.FAST,
        onRestart = {}
    )
}

@Preview(showBackground = true, name = "Good")
@Composable
fun PreviewGood() {
    FinalResultContent(
        trialResults = listOf(254L, 294L, 261L),
        average = 269L,
        category = ResultCategory.GOOD,
        onRestart = {}
    )
}

@Preview(showBackground = true, name = "Meh")
@Composable
fun PreviewMeh() {
    FinalResultContent(
        trialResults = listOf(272L, 240L, 388L),
        average = 303L,
        category = ResultCategory.STANDARD,
        onRestart = {}
    )
}

@Preview(showBackground = true, name = "Snail")
@Composable
fun PreviewSnail() {
    FinalResultContent(
        trialResults = listOf(1579L, 557L, 460L),
        average = 865L,
        category = ResultCategory.SNAIL,
        onRestart = {}
    )
}
