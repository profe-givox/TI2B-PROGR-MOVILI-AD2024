package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel : ViewModel()    {
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())

}