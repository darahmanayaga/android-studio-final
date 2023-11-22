package com.example.final_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.EventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<Item>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var user: FirebaseUser

    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.userDetails)
        user = auth.currentUser!!

        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            textView.text = user.email
        }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager (this)
        recyclerView.setHasFixedSize(true)

        itemArrayList = arrayListOf()

        myAdapter = MyAdapter(itemArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        addButton = findViewById(R.id.addNewItem)

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddItem::class.java)
            startActivity(intent)
        }

    }
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("items")
            .addSnapshotListener(object : com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val item = dc.document.toObject(Item::class.java)

                            // Get the first photo URL if the 'photoUrls' field is an array or nested collection
                            val photoUrls = dc.document.get("photoUrl") as? List<*>
                            if (!photoUrls.isNullOrEmpty() && photoUrls[0] is String) {
                                item.imageUrl = photoUrls[0] as String
                            }

                            itemArrayList.add(item)
                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}
