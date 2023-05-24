package com.sikic.movemedical.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentAddViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    fun insert(appointment: Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insert(appointment)
        }
    }
}