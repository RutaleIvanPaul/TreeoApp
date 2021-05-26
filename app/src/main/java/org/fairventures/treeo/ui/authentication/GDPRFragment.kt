package org.fairventures.treeo.ui.authentication


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_gdpr.*
import org.fairventures.treeo.R

class GDPRFragment : DialogFragment() {
    private val TAG = "GDPRFragment"
    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gdpr, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gdpr_conditions_button.setOnClickListener {
            dismiss()
        }

        try {
            val webView = view.findViewById<WebView>(R.id.webview)
            webView.loadUrl("https://fairventures.org/ueber-uns-2/datenschutz/")
        } catch (e: Exception) {
            Log.e("WebView Error", e.stackTrace.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    companion object {
        @JvmStatic
        fun display(fragmentManager: FragmentManager?): GDPRFragment? {
            val gdprFragment = GDPRFragment()
            val TAG = "GRPR_dialog"
            if (fragmentManager != null) {
                gdprFragment.show(fragmentManager, TAG)
            }
            return gdprFragment
        }
    }

}