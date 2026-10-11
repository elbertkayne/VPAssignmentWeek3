package com.arielelbert.vpassignmentweek3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClickerGameApp() {
    var coins by remember { mutableLongStateOf(0L) }
    var tapValue by remember { mutableLongStateOf(1L) }
    var upgradeCost by remember { mutableLongStateOf(10L) }
    var nextTapValueIncrease by remember { mutableLongStateOf(2L) }
    var isCatPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            CoinHeaderSection(coins = coins, tapValue = tapValue)
            Spacer(modifier = Modifier.height(48.dp))
            CatInteractiveSection(
                isPressed = isCatPressed,
                onPressChanged = { pressed ->
                    isCatPressed = pressed
                    if (pressed) {
                        coins += tapValue
                        coroutineScope.launch {
                            delay(150)
                            isCatPressed = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            UpgradeSection(
                coins = coins,
                upgradeCost = upgradeCost,
                nextIncrease = nextTapValueIncrease,
                onUpgradeClick = {
                    if (coins >= upgradeCost) {
                        coins -= upgradeCost
                        tapValue += nextTapValueIncrease
                        upgradeCost = (upgradeCost * 2)
                        nextTapValueIncrease = (nextTapValueIncrease * 1.5).toLong()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CoinHeaderSection(coins: Long, tapValue: Long) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Coins",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Text(
                text = "$coins",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$tapValue coins per tap",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CatInteractiveSection(isPressed: Boolean, onPressChanged: (Boolean) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tap the Cat!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(220.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onPressChanged(true)
                },
            contentAlignment = Alignment.Center
        ) {
            val imageRes = if (isPressed) R.drawable.cat_open else R.drawable.cat_closed

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Cat Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isPressed) "Meow!" else "Purr~",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun UpgradeSection(
    coins: Long,
    upgradeCost: Long,
    nextIncrease: Long,
    onUpgradeClick: () -> Unit
) {
    val canAfford = coins >= upgradeCost
    val missingCoins = upgradeCost - coins

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Give Me Your Coin",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Next upgrade: +$nextIncrease coins per tap",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onUpgradeClick,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if (canAfford) "Pay for $upgradeCost coins" else "Find $missingCoins more coins",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "1. Clicker Game - Default State")
@Composable
fun PreviewClickerGameDefault() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.cat_background),
            contentDescription = "Background Cat",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoinHeaderSection(coins = 0, tapValue = 1)
            Spacer(modifier = Modifier.height(24.dp))
            CatInteractiveSection(isPressed = false, onPressChanged = {})
            Spacer(modifier = Modifier.height(24.dp))
            UpgradeSection(coins = 0, upgradeCost = 10, nextIncrease = 2, onUpgradeClick = {})
        }
    }
}

@Preview(showBackground = true, name = "2. Clicker Game - Cat Meow & Enough Coins")
@Composable
fun PreviewClickerGameActive() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.cat_background),
            contentDescription = "Background Cat",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoinHeaderSection(coins = 15, tapValue = 1)
            Spacer(modifier = Modifier.height(24.dp))
            CatInteractiveSection(isPressed = true, onPressChanged = {})
            Spacer(modifier = Modifier.height(24.dp))
            UpgradeSection(coins = 15, upgradeCost = 10, nextIncrease = 2, onUpgradeClick = {})
        }
    }
}