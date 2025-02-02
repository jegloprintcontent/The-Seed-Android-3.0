package org.ileewe.theseedjcli.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.databinding.FragmentMoreBinding
import java.lang.Exception

class MoreFragment : Fragment(), View.OnClickListener {

    companion object {
        private val TAG = "More"
    }

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!
    private var manufacturer = ""
    private var model = ""
    private var osRelease = ""
    private var versionName = ""
    private var versionCode = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        val root = binding.root

        getDeviceDetails()
        getApplicationDetails()

        return  root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactNoBtn.setOnClickListener(this)
        binding.contactEmailBtn.setOnClickListener(this)
        binding.feedbackBtn.setOnClickListener(this)
        binding.prayerRequestBtn.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        if (view != null) {

            when(view.id){

                R.id.contact_no_btn -> {

                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.apply {
                        data = Uri.parse(getString(R.string.contact_church_phone_number))
                    }
                    requireActivity().startActivity(intent)
                    return
                }

                R.id.contact_email_btn -> {
                    sendEmail("info@ileewe.org", "Contact us", null)
                    /*val email_ = "info@ileewe.org"
                    val subject_ = "Contact us"
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:")
                    //intent.type  = "text/plain"
                    intent.putExtra(EXTRA_EMAIL, arrayOf(email_))
                    intent.putExtra(EXTRA_SUBJECT, subject_)
                    intent.putExtra(EXTRA_MESSAGE, "")

                    try {
                        startActivity(intent)
                    }catch (e: Exception) {
                        Toast.makeText(requireContext(),
                            getString(R.string.no_application_found), Toast.LENGTH_LONG).show()
                    }*/

                    return

                }

                R.id.feedback_btn -> {

                    //Extra message to append to feedback
                    @Suppress("DEPRECATION")
                    val message = Html.fromHtml(
                        StringBuilder()
                            .append(
                                "<p><b>Your device and operating " +
                                        "system details here will help our developers address your feedback.</b></p>"
                            )
                            .append("<small>Device Model: $manufacturer $model</small><br>")
                            .append("<small>Android Version: $osRelease</small><br>")
                            .append("<small>App Version: $versionName/$versionCode</small><br>")
                            .toString()
                    )

                    sendEmail("jegloprintcontent@gmail.com", "Feedback", message)
                    return
                }

                R.id.prayer_request_btn -> {
                    sendEmail("guidance@ileewe.org", "Prayer Request")
                    return
                }

            }
        }
    }


    private fun sendEmail(email:String, subject: String, message: Spanned? = null) {

        val message_ = message
        val email_ = email
        val subject_ = subject
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.apply {
            setData(Uri.parse("mailto:"))
            putExtra(EXTRA_EMAIL, arrayOf(email_))
            putExtra(EXTRA_SUBJECT, subject_)
            putExtra(EXTRA_TEXT, message_)
        }

        try {
            startActivity(intent)
        }catch (e: Exception) {
            Toast.makeText(requireContext(),
                getString(R.string.no_application_found), Toast.LENGTH_LONG).show()
        }

    }

    private fun getDeviceDetails() {
        manufacturer = Build.MANUFACTURER
        model = Build.MODEL
        osRelease = Build.VERSION.RELEASE
    }

    private fun getApplicationDetails() {
        try {
            val packageInfo: PackageInfo = requireContext().packageManager.getPackageInfo(requireActivity().packageName, 0)
            versionName = packageInfo.versionName
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        //Analytics
        Analytics.recordSectionVisit(requireActivity(), TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}