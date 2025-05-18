package com.example.bit_chronicles_1

sealed interface UiState {

    object Initial : UiState

    object Loading : UiState

    data class Success(val messages: List<Message>) : UiState

    data class Error(val errorMessage: String, val messages: List<Message> = emptyList()) : UiState
}

data class Message(
    val text: String,
    val isFromUser: Boolean
)
