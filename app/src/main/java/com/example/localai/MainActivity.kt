package com.example.localai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    var selectedModel by remember { mutableStateOf<Uri?>(null) }
    var modelLabel by remember { mutableStateOf("Chưa chọn model") }
    val modelPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Some document providers do not grant persistent access.
            }
            selectedModel = uri
            modelLabel = uri.lastPathSegment?.substringAfterLast('/') ?: "Đã chọn tệp model"
        }
    }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "LocalAI",
                "Xin chào! Bạn có thể chọn tệp model GGUF để chuẩn bị cho bước tích hợp runtime. Lưu ý: engine suy luận AI chưa được tích hợp trong bản này."
            )
        )
    }
    var input by remember { mutableStateOf("") }

    fun sendMessage() {
        val prompt = input.trim()
        if (prompt.isNotEmpty()) {
            messages.add(ChatMessage("Bạn", prompt))
            messages.add(
                ChatMessage(
                    "LocalAI",
                    if (selectedModel == null) {
                        "Bạn chưa chọn tệp model. Hãy chọn model GGUF trước; runtime AI vẫn cần được tích hợp để tạo câu trả lời thật."
                    } else {
                        "Đã chọn tệp model, nhưng engine suy luận chưa được kết nối nên mình chưa thể chạy model hoặc trả lời bằng AI offline."
                    }
                )
            )
            input = ""
        }
    }

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
        Text("Model: $modelLabel", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = { modelPicker.launch(arrayOf("*/*")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Chọn tệp model GGUF")
        }
        Text(
            "Trạng thái: giao diện hoạt động; runtime AI offline chưa tích hợp.",
            style = MaterialTheme.typography.bodySmall
        )
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
                keyboardActions = KeyboardActions(onSend = { sendMessage() })
            )
            Button(onClick = { sendMessage() }, enabled = input.isNotBlank()) {
                Text("Gửi")
            }
        }
    }
}
