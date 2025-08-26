package com.trustledger.adminaitrust.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trustledger.adminaitrust.databinding.ItemAnnouncementBinding
import com.trustledger.adminaitrust.databinding.ItemNotificationBinding
import com.trustledger.adminaitrust.models.Announcement
import com.trustledger.adminaitrust.models.NotificationItem

class NotificationAdapter(
    private var notifications: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        return NotificationViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        val notification = notifications[position]
        holder.binding.notificationTitle.text = notification.title
        holder.binding.notificationMessage.text = notification.body

        val fullDate = notification.timestamp.toDate().toString()
        val trimmedDate = fullDate.substringBefore("GMT").trim()
        holder.binding.tvAnnouncementTime.text = trimmedDate
    }


    override fun getItemCount(): Int {
        return notifications.size
    }

    fun updateData(newAnnouncements: List<NotificationItem>) {
        notifications = newAnnouncements
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}