package com.sikic.movemedical.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "appointmentTable")
class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name") var appointmentName: String = "",
    @ColumnInfo(name = "location") var appointmentLocation: String = "",
    @ColumnInfo(name = "date") var appointmentDate: String = "",
    @ColumnInfo(name = "time") var appointmentTime: String = "",
    @ColumnInfo(name = "description") var appointmentDescription: String = ""
) : Parcelable