package com.example.kotlinflow.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kotlinflow.R
import com.example.kotlinflow.ViewModel.CommentViewModel
import com.example.kotlinflow.network.Status
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var commentIdTextview: TextView
    private lateinit var nameTextview: TextView
    private lateinit var emailTextview: TextView
    private lateinit var commentTextview: TextView

    private var viewModel: CommentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)


        searchButton = findViewById(R.id.button)
        searchEditText = findViewById(R.id.search_edit_text)
        progressBar = findViewById(R.id.progress_bar)
        commentIdTextview = findViewById(R.id.comment_id_textview)
        nameTextview = findViewById(R.id.name_textview)
        emailTextview = findViewById(R.id.email_textview)
        commentTextview = findViewById(R.id.comment_textview)


        searchButton.setOnClickListener {
            // check to prevent api call with no parameters
            if (searchEditText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Query Can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                // if Query isn't empty, make the api call
                viewModel!!.getNewComment(searchEditText.text.toString().toInt())
            }
        }


        lifecycleScope.launch {
            viewModel!!.commentState.collect {
                when (it.status) {


                    Status.LOADING -> {
                        progressBar.isVisible = true
                    }


                    Status.SUCCESS -> {
                        progressBar.isVisible = false

                        it.data?.let { comment ->
                            commentIdTextview.text = comment.id.toString()
                            nameTextview.text = comment.name
                            emailTextview.text = comment.email
                            commentTextview.text = comment.comment
                        }
                    }


                    else -> {
                        progressBar.isVisible = false
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}