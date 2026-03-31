package com.example.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab7.ui.theme.Emerald
import com.example.lab7.ui.theme.Ink
import com.example.lab7.ui.theme.Lab7Theme
import com.example.lab7.ui.theme.Mint
import com.example.lab7.ui.theme.Slate

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CourseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab7Theme {
                CourseCrudApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CourseCrudApp(viewModel: CourseViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    CourseCrudScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNameChange = viewModel::updateName,
        onDurationChange = viewModel::updateDuration,
        onDescriptionChange = viewModel::updateDescription,
        onAddCourse = viewModel::addCourse,
        onDeleteCourse = viewModel::deleteCourse,
        onShowEdit = viewModel::showEditDialog,
        onDismissEdit = viewModel::hideEditDialog,
        onEditNameChange = viewModel::updateEditName,
        onEditDurationChange = viewModel::updateEditDuration,
        onEditDescriptionChange = viewModel::updateEditDescription,
        onUpdateCourse = viewModel::updateCourse
    )
}

@Composable
private fun CourseCrudScreen(
    uiState: CourseUiState,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddCourse: () -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onShowEdit: (Course) -> Unit,
    onDismissEdit: () -> Unit,
    onEditNameChange: (String) -> Unit,
    onEditDurationChange: (String) -> Unit,
    onEditDescriptionChange: (String) -> Unit,
    onUpdateCourse: () -> Unit,
) {
    var showForm by rememberSaveable { mutableStateOf(true) }
    val fabScale by animateFloatAsState(
        targetValue = if (showForm) 1f else 0.92f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "fabScale"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showForm = !showForm },
                modifier = Modifier.scale(fabScale),
                containerColor = Emerald,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (showForm) Icons.AutoMirrored.Outlined.MenuBook else Icons.Outlined.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (showForm) "An form" else "Mo form")
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showForm) {
                Surface(
                    color = Color.White.copy(alpha = 0.96f),
                    shadowElevation = 18.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Button(
                            onClick = onAddCourse,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Crossfade(targetState = uiState.isSaving, label = "bottomSaveText") { saving ->
                                Text(text = if (saving) "Dang luu..." else "Them vao Firestore")
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF7FFF9), Color(0xFFE9F5FF), Color.White)
                    )
                )
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .imePadding(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { HeaderSection(totalCourses = uiState.courses.size) }

                item {
                    AnimatedVisibility(
                        visible = showForm,
                        enter = fadeIn(tween(400)) + scaleIn(initialScale = 0.95f),
                        exit = fadeOut(tween(220)) + scaleOut(targetScale = 0.98f)
                    ) {
                        InputSection(
                            uiState = uiState,
                            onNameChange = onNameChange,
                            onDurationChange = onDurationChange,
                            onDescriptionChange = onDescriptionChange
                        )
                    }
                }

                item {
                    SectionTitle(
                        title = "Danh sach khoa hoc",
                        subtitle = "Du lieu dong bo realtime voi Cloud Firestore"
                    )
                }

                if (uiState.isLoading && uiState.courses.isEmpty()) {
                    item { LoadingState() }
                } else if (uiState.courses.isEmpty()) {
                    item { EmptyState() }
                } else {
                    items(uiState.courses, key = { it.id }) { course ->
                        CourseCard(
                            course = course,
                            deletingId = uiState.pendingDeleteId,
                            onEdit = { onShowEdit(course) },
                            onDelete = { onDeleteCourse(course) }
                        )
                    }
                }
            }

            if (uiState.isSaving) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }

    if (uiState.editingCourseId != null) {
        EditCourseDialog(
            uiState = uiState,
            onDismiss = onDismissEdit,
            onNameChange = onEditNameChange,
            onDurationChange = onEditDurationChange,
            onDescriptionChange = onEditDescriptionChange,
            onUpdateCourse = onUpdateCourse
        )
    }
}

@Composable
private fun HeaderSection(totalCourses: Int) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Emerald, Color(0xFF18C777), Color(0xFF73D9FF))
                    )
                )
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text("Firebase + Compose") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White.copy(alpha = 0.16f),
                        labelColor = Color.White
                    )
                )
                Text(
                    text = "Quan ly khoa hoc hien dai",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(
                    text = "Them, sua, xoa du lieu nhanh va hien thi danh sach dep mat co hieu ung.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.92f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CounterPill(label = "Tong khoa hoc", value = totalCourses.toString())
                    CounterPill(label = "Trang thai", value = "Online")
                }
            }
        }
    }
}

@Composable
private fun CounterPill(label: String, value: String) {
    Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.14f)) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Text(
                text = "$label: $value",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun InputSection(
    uiState: CourseUiState,
    onNameChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionTitle("Them khoa hoc", "Nhap thong tin va day len Firebase Firestore")
            CourseTextField(uiState.name, onNameChange, "Ten khoa hoc", Icons.AutoMirrored.Outlined.MenuBook)
            CourseTextField(uiState.duration, onDurationChange, "Thoi gian hoc", Icons.Outlined.Schedule)
            CourseTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = "Mo ta khoa hoc",
                leadingIcon = Icons.Outlined.Search,
                minLines = 3
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, color = Ink)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Slate)
    }
}

@Composable
private fun CourseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    minLines: Int = 1,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        singleLine = minLines == 1,
        minLines = minLines,
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun LoadingState() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = Emerald)
            Column {
                Text("Dang tai danh sach", style = MaterialTheme.typography.titleMedium)
                Text("Cho ket noi Firebase tra ve du lieu", color = Slate)
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Mint)
                    .padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = null,
                    tint = Emerald
                )
            }
            Text("Chua co khoa hoc nao", style = MaterialTheme.typography.titleMedium)
            Text("Them ban ghi dau tien de kiem tra thao tac CRUD.", color = Slate)
        }
    }
}

@Composable
private fun CourseCard(
    course: Course,
    deletingId: String?,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Emerald,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = course.duration,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Sua", tint = Emerald)
                    }
                    FilledIconButton(
                        onClick = onDelete,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFFECE8),
                            contentColor = Color(0xFFC53E2E)
                        )
                    ) {
                        Crossfade(targetState = deletingId == course.id, label = "deleteIcon") { deleting ->
                            if (deleting) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Outlined.Delete, contentDescription = "Xoa")
                            }
                        }
                    }
                }
            }
            Text(text = course.description, style = MaterialTheme.typography.bodyLarge, color = Ink.copy(alpha = 0.86f))
            Text(
                text = "Ma: ${course.id.takeLast(6).uppercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate,
                modifier = Modifier.alpha(0.75f)
            )
        }
    }
}

@Composable
private fun EditCourseDialog(
    uiState: CourseUiState,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUpdateCourse: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cap nhat khoa hoc", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CourseTextField(uiState.editName, onNameChange, "Ten khoa hoc", Icons.AutoMirrored.Outlined.MenuBook)
                CourseTextField(uiState.editDuration, onDurationChange, "Thoi gian hoc", Icons.Outlined.Schedule)
                CourseTextField(
                    value = uiState.editDescription,
                    onValueChange = onDescriptionChange,
                    label = "Mo ta khoa hoc",
                    leadingIcon = Icons.Outlined.Search,
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = onUpdateCourse) {
                Text("Luu thay doi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huy")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CourseCrudPreview() {
    Lab7Theme {
        CourseCrudScreen(
            uiState = CourseUiState(
                name = "Lap trinh Android",
                duration = "8 tuan",
                description = "Khoa hoc tu co ban den nang cao",
                courses = listOf(
                    Course("1", "Firebase", "6 tuan", "Firestore, auth va cloud"),
                    Course("2", "UI Compose", "4 tuan", "Xay dung giao dien dep va animation")
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNameChange = {},
            onDurationChange = {},
            onDescriptionChange = {},
            onAddCourse = {},
            onDeleteCourse = {},
            onShowEdit = {},
            onDismissEdit = {},
            onEditNameChange = {},
            onEditDurationChange = {},
            onEditDescriptionChange = {},
            onUpdateCourse = {}
        )
    }
}
