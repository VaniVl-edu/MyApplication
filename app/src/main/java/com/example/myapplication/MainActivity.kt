// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = employee.name)
                Text(text = "Возраст: ${employee.age}")
                Text(text = "Пол: ${employee.gender}")
                Text(text = employee.description)
            }
            Image(
                painter = painterResource(id = employee.photoId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
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
        EmployDetails(4, "Сотрудник 4", 28, "Женский", "Описание сотрудника 4", R.drawable.rubi)
    )
}

@Composable
fun EmployeeList(employees: List<EmployDetails>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp)
    ) {
        items(employees) { employee ->
            EmployeeCard(employee = employee)
        }
    }
}