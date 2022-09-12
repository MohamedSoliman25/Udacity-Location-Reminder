package com.udacity.project4.locationreminders.data.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

@get:Rule
var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var remindersLocalRepository: RemindersLocalRepository
    @Before
    fun initRepository() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        remindersLocalRepository = RemindersLocalRepository(remindersDatabase.reminderDao(),Dispatchers.Main)
    }

    @After
    fun closeDatabase() = remindersDatabase.close()

@Test
fun checkReminderInserted() = runBlocking {
    val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)

    remindersLocalRepository.saveReminder(reminder)
    val getReminderById = remindersLocalRepository.getReminder(reminder.id)

    assertThat((getReminderById as Result.Success).data, `is`(notNullValue()))
}

    @Test
    fun checkReminderRetrievedByID() = runBlocking {
        val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)

        remindersLocalRepository.saveReminder(reminder)
        val getReminderById = remindersLocalRepository.getReminder(reminder.id)

        assertThat((getReminderById as Result.Success).data, `is`(reminder))
    }
    @Test
    fun deleteForCheckReminderError() = runBlocking {
        val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)
        remindersLocalRepository.saveReminder(reminder)
        remindersLocalRepository.deleteAllReminders()

        val getMessage = remindersLocalRepository.getReminder(reminder.id) as Result.Error

        assertThat(getMessage.message, `is`("Reminder not found!"))
    }





}