package app.takahashi.yonpachi.androiddiarytest01

import android.content.Context
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Chat(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val date: Date = Date(System.currentTimeMillis())
)
