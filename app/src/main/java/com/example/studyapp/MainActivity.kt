package com.example.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.model.Subject
import com.example.studyapp.model.Task
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

@Preview
@Composable
fun StudyAppPreview() {
    StudyAppTheme {
        StudyApp(Modifier.fillMaxSize())
    }
}