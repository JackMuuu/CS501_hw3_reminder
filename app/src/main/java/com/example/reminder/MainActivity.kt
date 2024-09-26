package com.example.reminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.reminder.ui.theme.ReminderTheme
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReminderApp()
                }
            }
        }
    }
}

@Composable
fun ReminderApp() {

    var reminderMessage by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var isReminderSet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Reminder Message Input
                OutlinedTextField(
                    value = reminderMessage,
                    onValueChange = { reminderMessage = it },
                    label = { Text("Reminder Message") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date Picker Button
                Button(onClick = {
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = "${dayOfMonth}/${month + 1}/${year}"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 20.dp )
                ) {
                    Text(text = if (selectedDate.isNotEmpty()) "Date: $selectedDate" else "Select Date", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Time Picker Button
                Button(onClick = {
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePickerDialog.show()
                },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 20.dp )
                ) {
                    Text(text = if (selectedTime.isNotEmpty()) "Time: $selectedTime" else "Select Time", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Set Reminder Button
                Button(
                    onClick = {
                        if (reminderMessage.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                            isReminderSet = true
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Reminder Set")
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter all details")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Reminder", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Clear Reminder Button
                Button(
                    onClick = {
                        reminderMessage = ""
                        selectedDate = ""
                        selectedTime = ""
                        isReminderSet = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Reminder Cleared")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Reminder", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Reminder Details
                if (isReminderSet) {
                    Text("Reminder Set:")
                    Text("Message: $reminderMessage")
                    Text("Date: $selectedDate")
                    Text("Time: $selectedTime")
                } else {
                    Text("")
                }
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
