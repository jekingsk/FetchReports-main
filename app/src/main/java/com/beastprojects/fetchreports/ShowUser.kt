package com.beastprojects.fetchreports

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beastprojects.fetchreports.databinding.ActivityShowUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowUser : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        userRef = database.reference.child("Users")

        // Get the user ID from intent
        val userId = intent.getStringExtra("Uid")

        // Fetch user details from Realtime Database
        if (userId != null) {
            userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(UserFetch::class.java)
                        if (user != null) {
                            // Update UI with user details
                            updateUserUI(user)
                        }
                    } else {
                        // Handle case where user ID does not exist in the database
                        // Display an error message or take appropriate action
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Display an error message or take appropriate action
                }
            })
        }
    }

    private fun updateUserUI(user: UserFetch) {
        // Update UI elements with user details
        // Assuming you have TextViews in your layout to display user details
        val usernameTextView = findViewById<TextView>(R.id.ShowUser)
        val phoneNumberTextView = findViewById<TextView>(R.id.ShowPhoneNumber)
        val alternativePhnoTextView = findViewById<TextView>(R.id.ShowAlternativePhono)
        val emailTextView = findViewById<TextView>(R.id.ShowEmail)

        usernameTextView.text = user.username
        phoneNumberTextView.text = user.phoneNum.toString()
        alternativePhnoTextView.text = user.altphnum.toString()
        emailTextView.text = user.email
    }
}
data class UserFetch (
        val email : String = "",
        val username: String = "",
        val phoneNum: Long = 0,
        val altphnum: Long = 0
        )