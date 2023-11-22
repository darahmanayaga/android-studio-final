package com.example.final_exam

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddItem : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var editPrice: EditText
    private lateinit var imageUpload: ImageButton
    private lateinit var detailsUpload: Button

    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Handle the selected image URI
            selectedImageUri = uri
            // Display the image or perform any other actions
            // (e.g., setImageURI(uri) for an ImageView)

            // Change the color of the ImageButton when an image is chosen
            changeImageButtonColor()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        editName = findViewById(R.id.editName)
        editDescription = findViewById(R.id.editDescription)
        editPrice = findViewById(R.id.editPrice)
        imageUpload = findViewById(R.id.imageUpload)
        detailsUpload = findViewById(R.id.detailsUpload)

        // Save the original color of the ImageButton
        originalButtonColor = imageUpload.imageTintList?.defaultColor ?: Color.TRANSPARENT

        imageUpload.setOnClickListener {
            // Open the image picker when the image upload button is clicked
            getContent.launch("image/*")
        }

        detailsUpload.setOnClickListener {
            // Call the function to upload text data to Firestore
            selectedImageUri?.let { imageUri ->
                uploadTextData(imageUri)
            }
        }
    }

    private var originalButtonColor: Int = Color.TRANSPARENT

    private fun uploadTextData(imageUri: Uri) {
        // Retrieve text data from the UI
        val name = editName.text.toString()
        val description = editDescription.text.toString()
        val price = editPrice.text.toString().toDoubleOrNull() ?: 0.0

        // Generate a unique name for the image
        val imageName = "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"

        // Create a reference to the image in Firebase Storage
        val imageRef = storageRef.child("images/$imageName")

        // Upload the image to Firebase Storage
        val uploadTask: UploadTask = imageRef.putFile(imageUri)

        // Listen for the upload success or failure events
        uploadTask.addOnSuccessListener {
            // Image uploaded successfully, get download URL
            imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                // Save text data along with image URL to Firestore
                val item = hashMapOf(
                    "name" to name,
                    "description" to description,
                    "price" to price,
                    "imageUrl" to imageUrl.toString()
                )

                // Save text data to Firestore
                db.collection("items")
                    .add(item)
                    .addOnSuccessListener {
                        // Data uploaded successfully
                        // You can add further actions here, such as navigating to another activity
                        finish()

                        // Change the appearance of the ImageButton upon successful upload
                        changeImageButtonAppearance(true)

                        // Display a toast message
                        makeText(this@AddItem, "Item added successfully", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener { e ->
            // Handle failures
            // You might want to display an error message to the user

            // Change the appearance of the ImageButton upon failure
            changeImageButtonAppearance(false)
        }
    }

    // Function to change the appearance of the ImageButton
    private fun changeImageButtonAppearance(successfulUpload: Boolean) {
        if (successfulUpload) {
            imageUpload.setImageResource(R.drawable.success_color)
        } else {
            imageUpload.setImageResource(R.drawable.fail_color)
        }
    }

    // Function to change the color of the ImageButton
    private fun changeImageButtonColor() {
        // Change the color of the ImageButton when an image is chosen
        imageUpload.setImageResource(R.drawable.success_color)
    }
}
