package com.sikic.movemedical.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sikic.movemedical.db.dao.AppointmentDao
import com.sikic.movemedical.db.entity.Appointment

@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppointmentDatabase : RoomDatabase() {
    abstract fun getAppointmentDao(): AppointmentDao
}