package com.sikic.movemedical.ui.details

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.sikic.movemedical.databinding.ActivityAppointmentDetailsBinding
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.Location


class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appointment = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_APPOINTMENT_DETAILS, Appointment::class.java)
        } else {
            intent.getParcelableExtra(ARG_APPOINTMENT_DETAILS)
        }

        title = appointment?.appointmentName ?: "Appointment"

        binding = ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            txtAppointmentName.text = appointment?.appointmentName

            setupLocationText(appointment)

            txtDate.text = appointment?.appointmentDate
            txtTime.text = appointment?.appointmentTime
            txtAppointmentDescription.text = appointment?.appointmentDescription
        }
    }

    private fun setupLocationText(appointment: Appointment?) {
        val spannableString = SpannableString(appointment?.appointmentLocation)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val foundLocation = Location.values().find { it.locationName == appointment?.appointmentLocation }
                val googleMapsIntentUri =
                    Uri.parse("geo:${foundLocation?.latitude},${foundLocation?.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, googleMapsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                startActivity(mapIntent)
            }
        }

        spannableString.setSpan(
            clickableSpan, 0, appointment?.appointmentLocation?.length ?: 0,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtLocation.text = spannableString
        binding.txtLocation.movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        private const val ARG_APPOINTMENT_DETAILS = "appointment_details"

        fun startActivity(sourceActivity: Activity, appointment: Appointment) {
            val intent = Intent(sourceActivity, AppointmentDetailsActivity::class.java)
            intent.putExtra(ARG_APPOINTMENT_DETAILS, appointment)
            sourceActivity.startActivity(intent)
        }
    }
}