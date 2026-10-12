package com.example.localai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

private data class ChatMessage(val role: String, val text: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LocalAiChat()
                }
            }
        }
    }
}

@Composable
private fun LocalAiChat() {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "LocalAI",
                "Xin chào! Đây là giao diện LocalAI. Runtime mô hình AI offline chưa được tích hợp, nên hiện tại ứng dụng chưa thể tạo câu trả lời bằng model."
            )
        )
    }
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("LocalAI", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { messages.clear() }) { Text("Xóa chat") }
        }
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(message.role, style = MaterialTheme.typography.labelLarge)
                    Text(message.text, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập tin nhắn…") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (input.isNotBlank()) {
                        messages.add(ChatMessage("Bạn", input.trim()))
                        messages.add(
                            ChatMessage(
                                "LocalAI",
                                "Tin nhắn đã được thêm vào giao diện. Hãy tích hợp runtime GGUF để AI thực sự trả lời offline."
                            )
                        )
                        input = ""
                    }
                })
            )
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        messages.add(ChatMessage("Bạn", input.trim()))
                        messages.add(
                            ChatMessage(
                                "LocalAI",
                                "Tin nhắn đã được thêm vào giao diện. Runtime AI offline chưa được cài đặt."
                            )
                        )
                        input = ""
                    }
                },
                enabled = input.isNotBlank()
            ) {
                Text("Gửi")
            }
        }
    }
}
