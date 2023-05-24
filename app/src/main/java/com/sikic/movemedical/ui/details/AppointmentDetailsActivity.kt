package com.sikic.movemedical.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.sikic.movemedical.databinding.ActivityAppointmentDetailsBinding
import com.sikic.movemedical.db.entity.Appointment

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
            txtLocation.text = appointment?.appointmentLocation
            txtDate.text = appointment?.appointmentDate
            txtTime.text = appointment?.appointmentTime
            txtAppointmentDescription.text = appointment?.appointmentDescription
        }
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