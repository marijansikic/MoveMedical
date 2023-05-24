package com.sikic.movemedical.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.sikic.movemedical.R
import com.sikic.movemedical.databinding.ActivityAppointmentAddBinding
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.prepareTimeInReadableFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AppointmentEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentAddBinding
    private lateinit var viewModel: AppointmentEditViewModel
    private lateinit var appointment: Appointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        viewModel = getInstance()
    }

    private fun initUI() {
        appointment = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_APPOINTMENT_UPDATE, Appointment::class.java) ?: Appointment()
        } else {
            intent.getParcelableExtra(ARG_APPOINTMENT_UPDATE) ?: Appointment()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityAppointmentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appointment.apply {
            title = getString(R.string.update_appointment, appointment.appointmentName)
            with(binding.layoutFooter) {
                txtAppointmentName.setText(appointment.appointmentName)
                autoCompleteTextView.setText(appointment.appointmentLocation)
                btnTime.text = appointment.appointmentTime
                btnDate.text = appointment.appointmentDate
                txtAppointmentDescription.setText(appointment.appointmentDescription)
            }
        }

        val locations = resources.getStringArray(R.array.locations)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_location_item, locations)

        binding.apply {
            layoutFooter.autoCompleteTextView.setAdapter(arrayAdapter)
            fab.setOnClickListener {
                appointment.apply {
                    appointmentName = binding.layoutFooter.txtAppointmentName.text.toString()
                    appointmentLocation = binding.layoutFooter.autoCompleteTextView.text.toString()
                    appointmentDescription = binding.layoutFooter.txtAppointmentDescription.text.toString()
                    viewModel.update(this)
                    finish()
                }
            }

            prepareTimePicker()
            prepareDatePicker()
        }
    }

    private fun prepareDatePicker() {
        binding.layoutFooter.btnDate.setOnClickListener {
            val today: Calendar = Calendar.getInstance().apply {
                clear(Calendar.HOUR_OF_DAY)
                clear(Calendar.MINUTE)
                clear(Calendar.SECOND)
                clear(Calendar.MILLISECOND)
            }
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointForward.from(today.timeInMillis))

            val datePicker: MaterialDatePicker<Long> = MaterialDatePicker
                .Builder
                .datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText(getString(R.string.select_a_date))
                .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")
            datePicker.apply {
                addOnPositiveButtonClickListener {
                    val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    val date = sdf.format(it)
                    binding.layoutFooter.btnDate.text = date
                    appointment.appointmentDate = date
                }
            }
        }
    }

    private fun prepareTimePicker() {
        binding.layoutFooter.btnTime.setOnClickListener {
            val timePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTitleText(getString(R.string.select_a_time))
                .build()
            timePicker.show(supportFragmentManager, "TIME_PICKER")
            timePicker.apply {
                addOnPositiveButtonClickListener {
                    binding.layoutFooter.btnTime.text = prepareTimeInReadableFormat()
                    appointment.appointmentTime = prepareTimeInReadableFormat()
                }
            }
        }
    }

    private fun getInstance(): AppointmentEditViewModel {
        viewModel = if (this::viewModel.isInitialized) {
            viewModel
        } else {
            ViewModelProvider(this)[AppointmentEditViewModel::class.java]
        }
        return viewModel
    }

    companion object {
        private const val ARG_APPOINTMENT_UPDATE = "appointment_update"

        fun startActivity(sourceActivity: Activity, appointment: Appointment) {
            val intent = Intent(sourceActivity, AppointmentEditActivity::class.java)
            intent.putExtra(ARG_APPOINTMENT_UPDATE, appointment)
            sourceActivity.startActivity(intent)
        }
    }
}