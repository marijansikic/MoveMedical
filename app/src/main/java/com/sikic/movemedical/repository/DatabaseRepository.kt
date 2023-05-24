package com.sikic.movemedical.repository

import androidx.lifecycle.LiveData
import com.sikic.movemedical.db.dao.AppointmentDao
import com.sikic.movemedical.db.entity.Appointment

class DatabaseRepository(private val appointmentDao: AppointmentDao) {

    fun getAllAppointments(): LiveData<List<Appointment>> {
        return appointmentDao.getAllAppointments()
    }

    suspend fun insert(appointment: Appointment) {
        appointmentDao.insert(appointment)
    }

    suspend fun delete(appointment: Appointment) {
        appointmentDao.delete(appointment)
    }

    suspend fun update(appointment: Appointment) {
        appointmentDao.update(appointment)
    }
}