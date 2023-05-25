package com.sikic.movemedical

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.repository.DatabaseRepository
import com.sikic.movemedical.ui.add.AppointmentAddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AddViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var databaseRepository: DatabaseRepository

    private lateinit var addViewModel: AppointmentAddViewModel

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        databaseRepository = mock(DatabaseRepository::class.java)
        addViewModel = AppointmentAddViewModel(databaseRepository)
    }

    @Test
    fun testUpdateAppointment() = testDispatcher.runBlockingTest {

        val originalDummyAppointment =
            Appointment(1, "Appointment 1", Location.MEMPHIS.locationName, "6/26/2023", "12AM", "some description")

        val newDummyAppointment = originalDummyAppointment
        newDummyAppointment.appointmentName = "Changed appointment name"
        newDummyAppointment.appointmentLocation = Location.PARK_CITY.locationName
        newDummyAppointment.appointmentDate = "7/26/2023"
        newDummyAppointment.appointmentTime = "1PM"
        newDummyAppointment.appointmentDescription = "new description"

        addViewModel.insert(newDummyAppointment)

        advanceUntilIdle()
    }
}