package org.fairventures.treeo.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import org.fairventures.treeo.R
import org.fairventures.treeo.util.RESOLVE_HINT


class PhoneNumberLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_login)

        requestHint()
    }

    // Construct a request for phone numbers and show the picker
    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0
        )
    }

    // Obtain the phone number from the result
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                Log.d("PHONE NUMBER", credential?.id.toString())

                val client = SmsRetriever.getClient(this)
                val task: Task<Void> = client.startSmsRetriever()

                task.addOnSuccessListener(OnSuccessListener<Void?> {
                    // Successfully started retriever, expect broadcast intent
                    // ...
                })

                task.addOnFailureListener(OnFailureListener {
                    // Failed to start retriever, inspect Exception for more details
                    // ...
                })


            }
        }
    }
}