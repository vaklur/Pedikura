package com.example.pedikura.logs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.R


class LogsAdapter(
    private var logs: List<Logs>,
) : RecyclerView.Adapter<LogsAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logsTV: TextView = view.findViewById(R.id.item_log)
        val logsLL: LinearLayout = view.findViewById(R.id.item_log_LL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = logs[position]
        val logText = item.time+"   "+item.logText
        holder.logsTV.text = logText
        when(item.severity){
            "green" -> holder.logsLL.setBackgroundColor(Color.GREEN)
            "yellow" -> holder.logsLL.setBackgroundColor(Color.YELLOW)
            "red" -> holder.logsLL.setBackgroundColor(Color.RED)
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = logs.size
}