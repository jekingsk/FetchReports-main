package com.beastprojects.fetchreports

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.beastprojects.fetchreports.databinding.ActivityMainBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.RemoteMessage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var reportList: MutableList<Report>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and adapter
        reportList = mutableListOf()
        reportAdapter = ReportAdapter(reportList)
        binding.RecyclerReports.layoutManager = LinearLayoutManager(this)
        binding.RecyclerReports.adapter = reportAdapter

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("ReportsNew")

        // ChildEventListener to listen for new report additions
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val report = snapshot.getValue(Report::class.java)
                report?.let {
                    reportList.add(it) // Add new report to the list
                    reportAdapter.notifyDataSetChanged() // Notify adapter of data change

                    // Send notification using FirebaseMessagingService
                    sendNotification(it)
                }
            }

            private fun sendNotification(report: Report) {
                val notificationTitle = "New Report: ${report.type}"
                val notificationMessage = "Report by ${report.name} (UID: ${report.uid})"

                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val pendingIntent = PendingIntent.getActivity(
                    this@MainActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

                // Prepare the data to send to FirebaseMessagingService
                val data = hashMapOf(
                    "title" to notificationTitle,
                    "message" to notificationMessage
                )

                // Create a new instance of RemoteMessage and pass the data
                val remoteMessage = RemoteMessage.Builder("1")
                    .setData(data)
                    .build()

                // Call the onMessageReceived method of MyFirebaseMessagingService to handle the notification
                MyFirebaseMessagingService().onMessageReceived(remoteMessage)
            }



            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle changes to existing child nodes (if needed)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle removal of child nodes (if needed)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle movement/reordering of child nodes (if needed)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}

// Report data model
data class Report(
    val name: String? = null,
    val uid: String? = null,
    val time: String? = null,
    val type: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isPost: Boolean = false,
    val isBlog: Boolean = false
)