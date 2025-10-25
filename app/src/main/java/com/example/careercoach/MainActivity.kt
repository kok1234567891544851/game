package com.example.careercoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

// Простая заготовка приложения-карьеры тренера с Compose
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    CareerApp()
                }
            }
        }
    }
}

data class Career(
    var teamName: String = "FC New Start",
    var budget: Int = 500000,
    var seasonRound: Int = 1,
    var wins: Int = 0,
    var draws: Int = 0,
    var losses: Int = 0
)

@Composable
fun CareerApp() {
    var screen by remember { mutableStateOf("home") }
    var career by remember { mutableStateOf(Career()) }
    var lastMatchText by remember { mutableStateOf("") }

    when (screen) {
        "home" -> HomeScreen(onNewCareer = { career = Career(teamName = it); lastMatchText = ""; screen = "career" }, onOpenCareer = { screen = "career" })
        "career" -> CareerScreen(career = career, lastMatchText = lastMatchText, onStartMatch = {
            val result = simulateMatch(career)
            lastMatchText = result
        }, onBack = { screen = "home" })
    }
}

@Composable
fun HomeScreen(onNewCareer: (String) -> Unit, onOpenCareer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Career Coach", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onNewCareer("FC Rising") }, modifier = Modifier.fillMaxWidth()) {
            Text("Start New Career")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onOpenCareer, modifier = Modifier.fillMaxWidth()) {
            Text("Open Career")
        }
    }
}

@Composable
fun CareerScreen(career: Career, lastMatchText: String, onStartMatch: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Team: ${career.teamName}")
            Text(text = "Budget: $${career.budget}")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Season round: ${career.seasonRound}")
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFFEFEFEF)), contentAlignment = Alignment.Center) {
            Text("Pitch / Match preview (placeholder)")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onStartMatch) { Text("Play Match") }
            Button(onClick = onBack) { Text("Back") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Last result: $lastMatchText")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Record: W:${career.wins} D:${career.draws} L:${career.losses}")
    }
}

fun simulateMatch(career: Career): String {
    // Очень простая симуляция: шанс соперника зависит от бюджета
    val teamStrength = (career.budget / 100000).coerceAtLeast(1)
    val opponentStrength = Random.nextInt(1, 10)
    val scoreTeam = Random.nextInt(0, teamStrength + 3)
    val scoreOpp = Random.nextInt(0, opponentStrength + 3)

    career.seasonRound += 1
    // Простейшая корректировка бюджета
    career.budget += (Random.nextInt(-50000, 80000))

    val resultText = when {
        scoreTeam > scoreOpp -> {
            career.wins += 1
            "Win $scoreTeam:$scoreOpp"
        }
        scoreTeam == scoreOpp -> {
            career.draws += 1
            "Draw $scoreTeam:$scoreOpp"
        }
        else -> {
            career.losses += 1
            "Loss $scoreTeam:$scoreOpp"
        }
    }
    return resultText
}
