package com.example.studyapp

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studyapp.model.Subject
import com.example.studyapp.model.Task

@Composable
fun Greetings(subjects: List<Subject>, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(subjects) { subject ->
                Greeting(subject = subject)
            }
        }
    }
}


@Composable
private fun Greeting(subject: Subject, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        TabContent(subject = subject)
    }
}

@Composable
private fun TabContent(subject: Subject) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var addTask by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = subject.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Column(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()) {
                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp
                    )
                    subject.tasks.forEach { task ->
                        val taskCompleted = rememberSaveable(task.text)  { mutableStateOf(task.isCompleted) }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = taskCompleted.value,
                                onCheckedChange = {
                                    taskCompleted.value = it
                                    task.isCompleted = it
                                },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color.White
                                ),
                            )
                            Text(
                                text = task.text,
                                Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                    when {
                        addTask -> {
                            InputScreen(onInputButton = {
                                addTask = !addTask
                            }, subject = subject)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(90.dp)
                            ),
                    ) {
                        IconButton(
                            onClick = { addTask = !addTask },
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .size(width = 20.dp, height = 20.dp)
                        ) {
                            Icon(
                                imageVector = if ( addTask ) Filled.Close else Filled.Add,
                                contentDescription = stringResource(R.string.task_add),
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(90.dp)
                    )
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun InputScreen(onInputButton: () -> Unit, subject: Subject) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Blue,
                ),
            )
            Button(onClick = {
                onInputButton()
                subject.tasks.add(Task(text))
            }) {
                Modifier.fillMaxSize()
                Text(text = "OK")
            }
        }
    }
}
