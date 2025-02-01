package com.example.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.ui.theme.StudyAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyAppTheme {
                StudyApp(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

data class Task(
    val text: String,
    var isCompleted: Boolean = false
)

data class Subject(
    var name: String,
    var tasks: MutableList<Task> = mutableListOf()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyApp(modifier: Modifier) {
    val subjects: MutableList<Subject> = rememberSaveable {
        mutableListOf(
            Subject("国語", mutableListOf(Task("古典ワーク 第一章"), Task("漢字プリント 1~4"))),
            Subject("数学", mutableListOf(Task("教科書 問1"), Task("教科書 問2"), Task("教科書 問3"), Task("教科書 問4"))),
            Subject("社会", mutableListOf(Task("地理ワーク P13"))),
            Subject("理科", mutableListOf(Task("レポート 2"))),
            Subject("英語", mutableListOf(Task("英単語 1~200")))
        )
    }
    var menuType by rememberSaveable { mutableStateOf(true) }
    var target by rememberSaveable { mutableStateOf(false) }
    var text by remember { mutableStateOf("25位以内になる!") }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (target && text.isNotEmpty()) {
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    } else {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text("目標を入力") },
                            maxLines = 1,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            target = !target
                        },
                    ) {
                        Icon(
                            imageVector = if (target && text != "") Icons.Outlined.Edit else Filled.DoneOutline,
                            contentDescription = if (target) {
                                stringResource(R.string.title_edit)
                            } else {
                                stringResource(R.string.title_check)
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row {
                    Button(
                        onClick = { menuType = false },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .weight(0.5f)
                    ) {
                        Text(
                            "スタート",
                            fontSize = 30.sp
                        )
                    }
                    Button(
                        onClick = { menuType = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .weight(0.5f)
                    ) {
                        Text(
                            "共有",
                            fontSize = 30.sp)
                    }
                }
            }
        }
        ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (menuType) {
                TaskCompletionGraph(subjects)
            } else {
                Greetings(subjects)
            }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {

            }
        }
    }
}

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
                            .height(84.dp)
                            .fillMaxWidth(completionPercentage.div(100f))
                            .padding(horizontal = 8.dp)
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

@Composable
private fun Greetings(subjects: List<Subject>, modifier: Modifier = Modifier) {

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
        CardContent(subject = subject)
    }
}

@Composable
private fun CardContent(subject: Subject) {
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


//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    uiMode = UI_MODE_NIGHT_YES,
//    name = "GreetingPreviewDark",
//
//)
//@Preview(showBackground = true, widthDp = 320)
//@Composable
//fun GreetingPreview() {
//    StudyAppTheme {
//        Greetings()
//    }
//}
//
//@Preview(showBackground = true, widthDp = 320, heightDp = 320)
//@Composable
//fun OnboardingPreview() {
//    StudyAppTheme {
//        OnboardingScreen(onContinueClicked = {})
//    }
//}
//
@Preview
@Composable
fun StudyAppPreview() {
    StudyAppTheme {
        StudyApp(Modifier.fillMaxSize())
    }
}