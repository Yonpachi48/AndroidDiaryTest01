package app.takahashi.yonpachi.androiddiarytest01

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.QuerySnapshot

data class Group(
    @DocumentId
    val id: String = "",
    val groupName: String = "",
    val groupId: String = "",
    val chats: Chat?
)
