package app.takahashi.yonpachi.androiddiarytest01

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.QuerySnapshot

data class Room(
    @DocumentId
    val id: String = "",
    val roomName: String = "",
    val roomId: String = "",
    val chats: Chat?
)
