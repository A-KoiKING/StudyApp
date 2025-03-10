package com.example.studyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.model.Subject

@Composable
fun TaskCompletionGraph(subjects: List<Subject>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "教科ごとの進捗",
            style = MaterialTheme.typography.headlineMedium,
        )
        LazyColumn {
            items(subjects) { subject ->
                val completionPercentage = completionPercentage(subject).toFloat()

                Box(modifier = Modifier.padding(vertical = 10.dp)) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(
                                width = 8.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(84.dp)
                            .fillMaxWidth(completionPercentage.div(100f))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = subject.name,
                        Modifier.align(Alignment.Center),
                        fontSize = 36.sp
                    )
                    //                Text(
                    //                    text = "$completionPercentage%",
                    //                    style = MaterialTheme.typography.bodyMedium,
                    //                    textAlign = TextAlign.End,
                    //                )
                }
            }
        }
    }
}

@Composable
fun completionPercentage(subject: Subject): Int {
    if (subject.tasks.isEmpty()) return 0
    val completedTasks = subject.tasks.count { it.isCompleted }
    return (completedTasks * 100) / subject.tasks.size
}