package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import androidx.compose.runtime.mutableStateMapOf
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmployeeList(Details.employees)
        }
    }
}

@Composable
fun EmployeeCard(employee: EmployDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = employee.name)
                Text(text = "Возраст: ${employee.age}")
                Text(text = "Пол: ${employee.gender}")
                Text(text = employee.description)
            }
            Image(
                painter = painterResource(id = employee.photoId),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }
    }
}

data class EmployDetails(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val description: String,
    val photoId: Int
)

object Details {
    val employees = listOf(
        EmployDetails(1, "Сотрудник 1", 25, "Мужской", "Описание сотрудника 1", R.drawable.rohan),
        EmployDetails(2, "Сотрудник 2", 30, "Женский", "Описание сотрудника 2", R.drawable.kate),
        EmployDetails(3, "Сотрудник 3", 22, "Мужской", "Описание сотрудника 3", R.drawable.roy),
        EmployDetails(4, "Сотрудник 4", 28, "Женский", "Описание сотрудника 4", R.drawable.rubi),
        EmployDetails(5, "Сотрудник 5", 26, "Мужской", "Описание сотрудника 5", R.drawable.rohan),
        EmployDetails(6, "Сотрудник 6", 31, "Женский", "Описание сотрудника 6", R.drawable.kate),
        EmployDetails(7, "Сотрудник 7", 24, "Мужской", "Описание сотрудника 7", R.drawable.roy)
    )
}

@Composable
fun EmployeeList(employees: List<EmployDetails>) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf<EmployDetails?>(null) }
    val density = LocalDensity.current
    var pressOffset by remember { mutableStateOf(Offset.Zero) }
    val boxPositions = remember { mutableStateMapOf<Int, IntOffset>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp)
    ) {
        itemsIndexed(employees) { index, employee ->
            Box(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        boxPositions[employee.id] = IntOffset(
                            coordinates.positionInRoot().x.roundToInt(),
                            coordinates.positionInRoot().y.roundToInt()
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                val globalX = boxPositions[employee.id]?.x ?: 0
                                val globalY = boxPositions[employee.id]?.y ?: 0
                                pressOffset = Offset((globalX + offset.x).toFloat(), (globalY + offset.y).toFloat())
                                tryAwaitRelease()
                            },
                            onTap = {
                                Toast.makeText(context, "ID элемента: ${employee.id}", Toast.LENGTH_SHORT).show()
                            },
                            onLongPress = {
                                showMenu = true
                                selectedEmployee = employee
                            }
                        )
                    }
            ) {
                EmployeeCard(employee = employee)
            }
        }
    }

    if (showMenu && selectedEmployee != null) {
        val xOffset = with(density) { pressOffset.x.toDp() }
        val yOffset = with(density) { pressOffset.y.toDp() }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            offset = DpOffset(xOffset, yOffset)
        ) {
            DropdownMenuItem(
                text = { Text("Закрыть") },
                onClick = {
                    Toast.makeText(context, "Закрыть ${selectedEmployee?.name}", Toast.LENGTH_SHORT).show()
                    showMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Отмена") },
                onClick = {
                    Toast.makeText(context, "Отмена ${selectedEmployee?.name}", Toast.LENGTH_SHORT).show()
                    showMenu = false
                }
            )
        }
    }
}