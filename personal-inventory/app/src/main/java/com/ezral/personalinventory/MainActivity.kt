package com.ezral.personalinventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezral.personalinventory.ui.PersonalInventoryAppRoot
import com.ezral.personalinventory.ui.theme.PersonalInventoryTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezral.personalinventory.data.repository.HouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
) : ViewModel() {
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    private val _showOnboarding = MutableStateFlow(true)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    init {
        viewModelScope.launch {
            _showOnboarding.value = !houseRepository.hasAnyHouse()
            _ready.value = true
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val ready by viewModel.ready.collectAsStateWithLifecycle()
            val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()

            PersonalInventoryTheme {
                if (ready) {
                    PersonalInventoryAppRoot(showOnboarding = showOnboarding)
                }
            }
        }
    }
}
