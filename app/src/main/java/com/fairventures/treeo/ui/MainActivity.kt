package com.fairventures.treeo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.fairventures.treeo.R
import com.fairventures.treeo.models.RegisterUser
import com.fairventures.treeo.repository.MainRepository
import com.fairventures.treeo.ui.authentication.RegisterUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_user_button.setOnClickListener {
            registerUserViewModel.createUser(
                RegisterUser(
                    firstname.text.toString(),
                    lastname.text.toString(),
                    password.text.toString(),
                    email.text.toString(),
                    country.text.toString(),
                    register_username.text.toString(),
                    phone.text.toString()
                )
            ).observe(this, Observer {
                textView1.setText(it.toString())
            })

        }

    }

}