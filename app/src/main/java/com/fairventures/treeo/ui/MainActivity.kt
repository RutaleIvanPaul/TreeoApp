package com.fairventures.treeo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.fairventures.treeo.R
import com.fairventures.treeo.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val getFarmersViewModel: GetFarmersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFarmersViewModel.getFarmers().observe(this, Observer {
            textView1.setText(it.size.toString())
        })
    }

}