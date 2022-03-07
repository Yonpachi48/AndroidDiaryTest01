package app.takahashi.yonpachi.androiddiarytest01

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String = "",
    val uid: String = "",
    val name: String = "",
    val photoId: String = "",
    val groupId: String?
)
