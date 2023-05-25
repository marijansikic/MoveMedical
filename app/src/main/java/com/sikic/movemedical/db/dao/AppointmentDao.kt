package com.sikic.movemedical.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sikic.movemedical.db.entity.Appointment

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(appointment: Appointment)

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("Select * from appointmentTable order by id ASC")
    fun getAllAppointments(): LiveData<List<Appointment>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(appointment: Appointment) : Int
}