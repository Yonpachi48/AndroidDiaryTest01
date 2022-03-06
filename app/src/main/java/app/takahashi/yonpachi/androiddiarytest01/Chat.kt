package app.takahashi.yonpachi.androiddiarytest01

import android.content.Context
import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val content: String = ""
)
