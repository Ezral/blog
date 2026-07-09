package com.ezral.personalinventory.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezral.personalinventory.data.repository.HouseRepository
import com.ezral.personalinventory.domain.model.DefaultRoomTemplates
import com.ezral.personalinventory.ui.components.InventoryScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
) : ViewModel() {
    fun completeOnboarding(
        houseName: String,
        selectedRooms: List<String>,
        onDone: () -> Unit,
    ) {
        viewModelScope.launch {
            houseRepository.createHouseWithRooms(
                name = houseName.trim(),
                roomNames = selectedRooms.ifEmpty { listOf("Kitchen", "Bedroom") },
            )
            onDone()
        }
    }
}

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    var houseName by rememberSaveable { mutableStateOf("") }
    var selectedRooms by rememberSaveable { mutableStateOf(DefaultRoomTemplates.toSet()) }

    InventoryScaffold(title = "Welcome") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Organize your home inventory by house, room, and storage.",
                style = MaterialTheme.typography.bodyLarge,
            )
            OutlinedTextField(
                value = houseName,
                onValueChange = { houseName = it },
                label = { Text("House name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Text("Rooms to add", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DefaultRoomTemplates.forEach { room ->
                    FilterChip(
                        selected = room in selectedRooms,
                        onClick = {
                            selectedRooms = if (room in selectedRooms) {
                                selectedRooms - room
                            } else {
                                selectedRooms + room
                            }
                        },
                        label = { Text(room) },
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.completeOnboarding(houseName, selectedRooms.toList(), onFinished)
                },
                enabled = houseName.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Get started")
            }
        }
    }
}
