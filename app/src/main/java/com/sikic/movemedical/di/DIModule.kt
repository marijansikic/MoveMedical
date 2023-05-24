package com.sikic.movemedical.di

import android.content.Context
import androidx.room.Room
import com.sikic.movemedical.db.AppointmentDatabase
import com.sikic.movemedical.db.dao.AppointmentDao
import com.sikic.movemedical.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppointmentDatabase {
        return Room.databaseBuilder(
            appContext,
            AppointmentDatabase::class.java,
            "appointment_database"
        ).build()
    }

    @Provides
    fun provideAppointmentDao(database: AppointmentDatabase): AppointmentDao {
        return database.getAppointmentDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(appointmentDao: AppointmentDao): DatabaseRepository {
        return DatabaseRepository(appointmentDao)
    }
}