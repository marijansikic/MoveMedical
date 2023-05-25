package com.sikic.movemedical

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.repository.DatabaseRepository
import com.sikic.movemedical.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var databaseRepository: DatabaseRepository

    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        databaseRepository = mock(DatabaseRepository::class.java)
        mainViewModel = MainViewModel(databaseRepository)
    }

    @Test
    fun testGetAllAppointments() = testDispatcher.runBlockingTest {
        val dummyAppointments = listOf(
            Appointment(1, "Appointment 1", Location.MEMPHIS.locationName, "6/26/2023", "12AM", "some description"),
            Appointment(2, "Appointment 2", Location.SAN_DIEGO.locationName, "6/26/2023", "12PM", "some description 2")
        )
        val liveData = MutableLiveData<List<Appointment>>()
        liveData.value = dummyAppointments

        `when`(databaseRepository.getAllAppointments()).thenReturn(liveData)

        mainViewModel.appointments

        advanceUntilIdle()

        val actualAppointments = mainViewModel.appointments.value
        Assert.assertEquals(dummyAppointments, actualAppointments)
    }

    @Test
    fun testDeleteAppointment() = testDispatcher.runBlockingTest {
        val dummyAppointments = listOf(
            Appointment(1, "Appointment 1", Location.MEMPHIS.locationName, "6/26/2023", "12AM", "some description"),
            Appointment(2, "Appointment 2", Location.SAN_DIEGO.locationName, "6/26/2023", "12PM", "some description 2")
        )
        val liveData = MutableLiveData<List<Appointment>>()
        liveData.value = dummyAppointments

        mainViewModel.deleteAppointment(dummyAppointments[1])

        advanceUntilIdle()

        val actualAppointmentsSize = mainViewModel.appointments.value?.size
        Assert.assertEquals(dummyAppointments.size, actualAppointmentsSize)
    }
}