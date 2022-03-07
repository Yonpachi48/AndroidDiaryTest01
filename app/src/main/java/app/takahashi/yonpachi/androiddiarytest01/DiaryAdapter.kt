package app.takahashi.yonpachi.androiddiarytest01

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.takahashi.yonpachi.androiddiarytest01.databinding.DiaryListItemBinding
import coil.api.load
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DiaryAdapter : ListAdapter<Chat, DiaryViewHolder>(diffUtilCallback){

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        setOnClickListener(listener)
        val view = DiaryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onClick(it, getItem(position))
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, chat: Chat)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class DiaryViewHolder (
    private val binding: DiaryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(chat: Chat) {
        val db = Firebase.firestore
        val docUser = db.collection("users").document(chat.userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.listUserImageView.load(document.data?.get("photoId").toString())
                    binding.listUserNameTextView.text = document.data?.get("name").toString()
                }
                else Log.d("tag", "🥑")
            }
            .addOnFailureListener { e ->
                Log.d("tag", "🥶", e)
            }

        binding.listDateTextView.text = SimpleDateFormat("MM月dd日(E)曜日", Locale.JAPANESE).format(chat.date)
        binding.listDiaryTextView.text = chat.content
    }
}

private val diffUtilCallback = object : DiffUtil.ItemCallback<Chat>() {
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id == newItem.id
    }
}