package com.sikic.movemedical.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.sikic.movemedical.R
import com.sikic.movemedical.databinding.ActivityMainBinding
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.ui.add.AppointmentAddActivity
import com.sikic.movemedical.ui.details.AppointmentDetailsActivity
import com.sikic.movemedical.ui.edit.AppointmentEditActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppointmentAdapterListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareViewModel()
        prepareUI()
    }

    private fun prepareViewModel() {
        mainViewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
        mainViewModel.appointments.observe(this@MainActivity) {
            appointmentAdapter.addItems(it)
        }
    }

    private fun prepareUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appointmentAdapter = AppointmentAdapter(this, this)
        binding.apply {
            recyclerAppointments.adapter = appointmentAdapter
            recyclerAppointments.addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            appointmentAdapter.enableSwipeActions(recyclerAppointments)

            btnAddAppointment.setOnClickListener {
                AppointmentAddActivity.startActivity(this@MainActivity)
            }
        }
    }

    override fun onAppointmentClicked(appointment: Appointment, sourceActivity: MainActivity) {
        AppointmentDetailsActivity.startActivity(
            this,
            appointment
        )
    }

    override fun onAppointmentDeleted(appointment: Appointment) {
        mainViewModel.deleteAppointment(appointment)
        Snackbar.make(
            binding.root, getString(R.string.appointment_deleted, appointment.appointmentName), Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onAppointmentUpdated(appointment: Appointment) {
        AppointmentEditActivity.startActivity(
            this,
            Appointment(
                appointment.id,
                appointment.appointmentName,
                appointment.appointmentLocation,
                appointment.appointmentDate,
                appointment.appointmentTime,
                appointment.appointmentDescription
            )
        )
    }
}

interface AppointmentAdapterListener {
    fun onAppointmentClicked(appointment: Appointment, sourceActivity: MainActivity)
    fun onAppointmentDeleted(appointment: Appointment)
    fun onAppointmentUpdated(appointment: Appointment)
}