package com.sikic.movemedical.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {
    val appointments: LiveData<List<Appointment>> = databaseRepository.getAllAppointments()

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.delete(appointment)
        }
    }
}