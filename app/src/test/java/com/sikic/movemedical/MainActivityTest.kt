package com.sikic.movemedical

import com.sikic.movemedical.db.entity.Appointment
import com.sikic.movemedical.ui.main.MainActivity
import com.sikic.movemedical.ui.main.MainViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainActivityTest {

    @Mock
    private lateinit var mockViewModel: MainViewModel

    private lateinit var activity: MainActivity

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        activity = MainActivity()
        activity.mainViewModel = mockViewModel
    }

    @Test
    fun testOnAppointmentClicked() {
        // Create a mock appointment and source activity
        val appointment = mock(Appointment::class.java)
        val sourceActivity = mock(MainActivity::class.java)

        // Call the method under test
        activity.onAppointmentClicked(appointment, sourceActivity)

        // Verify the behavior
        verify(mockViewModel).deleteAppointment(appointment)
        // ... perform additional verifications as needed
    }

    @Test
    fun testOnAppointmentDeleted() {
        // Create a mock appointment
        val appointment = mock(Appointment::class.java)

        // Call the method under test
        activity.onAppointmentDeleted(appointment)

        // Verify the behavior
        verify(mockViewModel).deleteAppointment(appointment)
        // ... perform additional verifications as needed
    }

    @Test
    fun testOnAppointmentUpdated() {
        // Create a mock appointment
        val appointment = mock(Appointment::class.java)

        // Call the method under test
        activity.onAppointmentUpdated(appointment)

        // Verify the behavior
      //  verify(mockViewModel).updateAppointment(appointment)
        // ... perform additional verifications as needed
    }

    // Other tests

}