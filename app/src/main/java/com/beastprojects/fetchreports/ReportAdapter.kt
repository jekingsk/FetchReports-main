package com.beastprojects.fetchreports

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportAdapter(private val reportList: List<Report>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.Username)
        val timeTextView: TextView = itemView.findViewById(R.id.Time)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.PhoneNumber)
        val alternativeTextView: TextView = itemView.findViewById(R.id.Alternative)
        val langTextView: TextView = itemView.findViewById(R.id.Lang)
        val longTextView: TextView = itemView.findViewById(R.id.Long)
        val imgloc:ImageView=itemView.findViewById(R.id.ImgLoc)
        val ButtonShowUser : Button = itemView.findViewById(R.id.ButtonShowUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.report_list, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentItem = reportList[position]

        holder.usernameTextView.text = currentItem.name ?: "N/A"
        holder.timeTextView.text = currentItem.time ?: "N/A"
        holder.phoneNumberTextView.text = currentItem.uid ?: "N/A"
        holder.alternativeTextView.text = if (currentItem.isBlog) "Blog" else if (currentItem.isPost) "Post" else "N/A"
        holder.langTextView.text = currentItem.type
        holder.longTextView.text = "Lat: ${currentItem.latitude}, Long: ${currentItem.longitude}"


        holder.ButtonShowUser.setOnClickListener {

            val context = holder.itemView.context
            val intent = Intent(context, ShowUser::class.java)
            intent.putExtra("Uid", currentItem.uid)

            context.startActivity(intent)

        }



        holder.imgloc.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UserLocAndDistance::class.java)
            // Add latitude and longitude data as extras
            intent.putExtra("latitude", currentItem.latitude)
            intent.putExtra("longitude", currentItem.longitude)
            intent.putExtra("Type",currentItem.type)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = reportList.size
}