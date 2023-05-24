package com.sikic.movemedical.ui.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
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
class AppointmentAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentAddBinding
    private lateinit var viewModel: AppointmentAddViewModel
    private var appointment: Appointment = Appointment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        viewModel = getInstance()
    }

    private fun initUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityAppointmentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.add_new_appointment)

        val arrayAdapter = ArrayAdapter(
            this, R.layout.dropdown_location_item,
            resources.getStringArray(R.array.locations)
        )
        binding.layoutFooter.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.apply {
            fab.setOnClickListener {

                appointment.apply {
                    appointmentName = binding.layoutFooter.txtAppointmentName.text.toString()
                    appointmentLocation = binding.layoutFooter.autoCompleteTextView.text.toString()
                    appointmentDescription = binding.layoutFooter.txtAppointmentDescription.text.toString()

                }

                with(appointment) {
                    val fields = listOf(
                        with(binding.layoutFooter) {
                            txtAppointmentName.text.toString()
                            autoCompleteTextView.text.toString()
                            txtAppointmentDescription.text.toString()
                        },
                        appointmentDate,
                        appointmentTime
                    )

                    if (fields.all { it.isNotEmpty() }) {
                        viewModel.insert(this)
                        finish()
                    } else {
                        Snackbar.make(
                            binding.root, getString(R.string.please_fill_all_the_data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            setupTimePicker()
            setupDatePicker()
        }
    }

    private fun setupTimePicker() {
        binding.layoutFooter.btnTime.setOnClickListener {
            MaterialTimePicker
                .Builder()
                .setTitleText(getString(R.string.select_a_time))
                .build().apply {
                    show(supportFragmentManager, "TIME_PICKER")
                    addOnPositiveButtonClickListener {
                        binding.layoutFooter.btnTime.text = prepareTimeInReadableFormat()
                        appointment.appointmentTime = prepareTimeInReadableFormat()
                    }
                }
        }
    }

    private fun setupDatePicker() {
        binding.layoutFooter.btnDate.setOnClickListener {
            val today: Calendar = Calendar.getInstance().apply {
                clear(Calendar.HOUR_OF_DAY)
                clear(Calendar.MINUTE)
                clear(Calendar.SECOND)
                clear(Calendar.MILLISECOND)
            }
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointForward.from(today.timeInMillis))

            MaterialDatePicker
                .Builder
                .datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText(getString(R.string.select_a_date))
                .build().apply {
                    show(supportFragmentManager, "DATE_PICKER")
                    addOnPositiveButtonClickListener {
                        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        val date = sdf.format(it)
                        binding.layoutFooter.btnDate.text = date
                        appointment.appointmentDate = date
                    }
                }
        }
    }

    private fun getInstance(): AppointmentAddViewModel {
        viewModel = if (this::viewModel.isInitialized) {
            viewModel
        } else {
            ViewModelProvider(this)[AppointmentAddViewModel::class.java]
        }
        return viewModel
    }

    companion object {
        fun startActivity(sourceActivity: Activity) =
            sourceActivity.startActivity(Intent(sourceActivity, AppointmentAddActivity::class.java))
    }
}