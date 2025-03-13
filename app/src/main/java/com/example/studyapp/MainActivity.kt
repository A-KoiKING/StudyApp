package com.example.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studyapp.model.Subject
import com.example.studyapp.model.Task
import com.example.studyapp.ui.theme.StudyAppTheme


sealed class StudyAppScreen(val route: String) {
    data object Home : StudyAppScreen("home")
    data object Edit : StudyAppScreen("edit")
    data object Share : StudyAppScreen("share")
}

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

@Composable
fun StudyApp(modifier: Modifier) {
    val navController = rememberNavController()

    val subjects = rememberSaveable {
        mutableListOf(
            Subject("国語", mutableListOf(Task("古典ワーク 第一章"), Task("漢字プリント 1~4"))),
            Subject("数学", mutableListOf(Task("教科書 問1"), Task("教科書 問2"))),
            Subject("社会", mutableListOf(Task("地理ワーク P13"))),
            Subject("理科", mutableListOf(Task("レポート 2"))),
            Subject("英語", mutableListOf(Task("英単語 1~200")))
        )
    }

    var target by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("25位以内になる!") }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                target = target,
                text = text,
                onTextChange = { text = it },
                onTargetChange = { target = !target }
            )
        },
        bottomBar = {
            AppBottomBar(navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = StudyAppScreen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(StudyAppScreen.Home.route) { Greetings(subjects) }
            composable(StudyAppScreen.Edit.route) { Greetings(subjects) }
            composable(StudyAppScreen.Share.route) { TaskCompletionGraph(subjects) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(target: Boolean, text: String, onTextChange: (String) -> Unit, onTargetChange: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (target && text.isNotEmpty()) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp
                )
            } else {
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = { Text("目標を入力") },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        },
        actions = {
            IconButton(
                onClick = onTargetChange
            ) {
                Icon(
                    imageVector = if (target && text.isNotEmpty()) Icons.Outlined.Edit else Filled.DoneOutline,
                    contentDescription = if (target) {
                        stringResource(R.string.title_edit)
                    } else {
                        stringResource(R.string.title_check)
                    }
                )
            }
        }
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem("ホーム", StudyAppScreen.Home.route, Filled.Home),
        BottomNavItem("編集", StudyAppScreen.Edit.route, Filled.Edit),
        BottomNavItem("共有", StudyAppScreen.Share.route, Filled.Share)
    )

    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(40.dp)
                    )
                },
                label = {
                    Text(
                        item.label,
                        fontSize = 20.sp
                    )
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)


@Preview
@Composable
fun StudyAppPreview() {
    StudyAppTheme {
        StudyApp(Modifier.fillMaxSize())
    }
}