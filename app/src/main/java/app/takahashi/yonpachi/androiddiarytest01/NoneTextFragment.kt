package app.takahashi.yonpachi.androiddiarytest01

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class NoneTextFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("ERROR")
            .setMessage("テキストを入力してください")
            .setPositiveButton("OK") { dialog, which -> }
            .show()

        return builder.create()
    }
}