package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import org.hamcrest.MatcherAssert.assertThat
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var remindersDatabase: RemindersDatabase
    @Before
    fun initDatabase() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase() = remindersDatabase.close()

    @Test
    fun insertReminder() = runBlockingTest {
        //Given - Insert a reminder
        val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)
        remindersDatabase.reminderDao().saveReminder(reminder)

        //When - Get the reminder by id from the database
        val reminderByID = remindersDatabase.reminderDao().getReminderById(reminder.id)
        //Then - The reminder contains notNullValue (Successfully saved)
        assertThat(reminderByID,`is`(notNullValue()))

    }
    @Test
    fun retrieveReminderById() = runBlockingTest {
        //Given - Insert a reminder
        val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)

        remindersDatabase.reminderDao().saveReminder(reminder)

        //When - Get the reminder by id from the database
        val reminderByID = remindersDatabase.reminderDao().getReminderById(reminder.id)

        //Then - The loaded reminder contains the expected values
        assertThat<ReminderDTO>(reminderByID as ReminderDTO, `is`(reminder))
        assertThat(reminderByID.id,`is`(reminder.id))
        assertThat(reminderByID.title,`is`(reminder.title))
        assertThat(reminderByID.description,`is`(reminder.description))
        assertThat(reminderByID.location,`is`(reminder.location))
        assertThat(reminderByID.latitude,`is`(reminder.latitude))
        assertThat(reminderByID.longitude,`is`(reminder.longitude))
    }


    @Test
    fun deleteForCheckReminderError() = runBlockingTest {
        val reminder = ReminderDTO("myTitle","myDescription","myLocation",30.68956,31.31027)
        remindersDatabase.reminderDao().saveReminder(reminder)
        remindersDatabase.reminderDao().deleteAllReminders()

        val getReminderByID = remindersDatabase.reminderDao().getReminderById(reminder.id)
        assertThat(getReminderByID,`is`(nullValue()))

    }


}