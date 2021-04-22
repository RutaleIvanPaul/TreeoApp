package org.fairventures.treeo.ui

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import org.fairventures.treeo.R
import org.fairventures.treeo.util.ChooseLangFragment
import org.fairventures.treeo.util.ContextUtils
import org.fairventures.treeo.util.PrefManager
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context) {
        val prefManager = PrefManager()
        val localeToSwitchTo = Locale((prefManager.getPersistStorage(newBase))!!)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(
            newBase,
            localeToSwitchTo
        )
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
