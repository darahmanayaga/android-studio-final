package com.example.final_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.final_exam.databinding.ActivityItemDetailsBinding

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the Item object passed from the MainActivity
        val item = intent.getSerializableExtra("item") as? Item

        // Find the back button
        val backButton = findViewById<Button>(R.id.backButton)

        // Display item details in the new activity
        binding.tvItemName.text = item?.name
        binding.tvItemDesc.text = item?.description
        binding.tvItemPrice.text = item?.price.toString()

        // Load image using Glide (assuming you have set up Glide as before)
        Glide.with(this)
            .load(item?.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.ivItemImage)

        // Set a click listener for the back button
        binding.backButton.setOnClickListener {
            // Finish the current activity and go back to the previous activity
            finish()
        }
    }

    // Override the onBackPressed method to handle the back button press
    override fun onBackPressed() {
        // Finish the current activity and go back to the previous activity
        finish()
    }
}
