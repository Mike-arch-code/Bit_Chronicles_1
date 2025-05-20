package com.example.bit_chronicles_1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.android.gms.tasks.OnCompleteListener // ¡Importación necesaria para el listener de Firebase!
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BakingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val messages = mutableListOf<Message>()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    // Instancia de tu manager de Firebase
    private val firebaseDBManager = FirebaseRealtimeDBManager()

    // Define una ruta para tu chat en Firebase. Puedes hacerla dinámica
    // (ej. basada en ID de usuario o sesión) si tienes múltiples chats.
    private val CHAT_PATH = "chats/my_first_chat_session/messages"

    fun sendPrompt(prompt: String) {
        val userMessage = Message(prompt, isFromUser = true)
        messages.add(userMessage)
        _uiState.value = UiState.Loading

        // Subir mensaje del usuario a Firebase
        firebaseDBManager.pushData(
            CHAT_PATH,
            userMessage,
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Mensaje del usuario guardado en Firebase.")
                } else {
                    println("Error al guardar mensaje del usuario: ${task.exception?.message}")
                }
            }
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fullPrompt = buildString {
                    messages.forEach { message ->
                        if (message.isFromUser) {
                            append("User: ${message.text}\n")
                        } else {
                            append("Bot: ${message.text}\n")
                        }
                    }
                    append("Bot:")
                }

                val response = generativeModel.generateContent(content { text(fullPrompt) })
                val botReplyText = response.text?.trim() ?: "No response"
                val botMessage = Message(botReplyText, isFromUser = false)
                messages.add(botMessage)

                // Subir mensaje del bot a Firebase
                firebaseDBManager.pushData(
                    CHAT_PATH,
                    botMessage,
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Mensaje del bot guardado en Firebase.")
                        } else {
                            println("Error al guardar mensaje del bot: ${task.exception?.message}")
                        }
                    }
                )

                _uiState.value = UiState.Success(messages.toList())
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    errorMessage = e.localizedMessage ?: "Unknown error",
                    messages = messages.toList()
                )
            }
        }
    }
}
