package com.example.showpriceandpicapp

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.showpriceandpicapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mstore: StorageReference
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val thingnametxtview = findViewById<TextView>(R.id.textView)
        val thingpricetxtview = findViewById<TextView>(R.id.textView3)
        val findbtn = findViewById<Button>(R.id.button)
        val textboxinput = findViewById<EditText>(R.id.editTextText)
        val imageview = findViewById<ImageView>(R.id.imageView)


        findbtn.setOnClickListener {

            //here we sat up our progress dialog and text
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Fetching")
            progressDialog.setCancelable(false)
            progressDialog.show()

            //now we need to fetch the text from the textbox
            val itemname: String = textboxinput.text.toString()
            mstore = FirebaseStorage.getInstance().getReference("images/" + itemname + ".jpg")
            //image display in try catch
            try {
                val localfile: File = File.createTempFile("tempfile", "jpg")
                mstore.getFile(localfile)
                    .addOnSuccessListener {
                        if (progressDialog.isShowing){
                            progressDialog.dismiss()
                        }
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        imageview.setImageBitmap(bitmap)
                        Toast.makeText(this,"gud",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener{
                        if (progressDialog.isShowing){
                            progressDialog.dismiss()
                        }

                        Toast.makeText(this,"no gud",Toast.LENGTH_SHORT).show()

                    }
            }
            catch (IOException:Exception) {
                // handler

            }
            if (itemname.isNotEmpty())
            {
                database = FirebaseDatabase.getInstance().getReference("items")
                database.child(itemname).get().addOnCompleteListener{
                        task ->
                    if (task.getResult().exists()){
                        Toast.makeText(this,"here's the details",Toast.LENGTH_SHORT).show()
                        val datasnapshot: DataSnapshot = task.getResult()
                        var thingname:String = datasnapshot.child("name").getValue().toString()
                        var thingprice:String = datasnapshot.child("price").getValue().toString()
                        binding.textView.setText(thingname)
                        binding.textView3.setText(thingprice)


                    }
                    else{
                        Toast.makeText(this,"here's the error",Toast.LENGTH_SHORT).show()

                    }


                }
            }


            else {
                Toast.makeText(this,"something's wrong with displaying info",Toast.LENGTH_SHORT).show()

            }


        }

    }}

