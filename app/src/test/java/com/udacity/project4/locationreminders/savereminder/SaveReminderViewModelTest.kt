package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SaveReminderViewModelTest {

    //Testing the SaveReminderView and its live data objects
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @Before
    fun setUpViewModel(){
        stopKoin()
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
        fakeDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)
    }


    @Test
    fun showLoading_withReminderData()= runBlockingTest {

        val reminderDataItem = ReminderDataItem("myTitle","myDescription","myLocation",30.68956,31.31027)
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.validateAndSaveReminder(reminderDataItem)
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), Matchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), Matchers.`is`(false))
    }

    @Test
    fun validateAndSaveReminder_withValidReminderItem_showToast() = runBlockingTest {
        val reminderDataItem = ReminderDataItem("myTitle","myDescription","myLocation",30.68956,31.31027)
        saveReminderViewModel.validateAndSaveReminder(reminderDataItem)
       assertThat(saveReminderViewModel.showToast.getOrAwaitValue()).isEqualTo("Reminder Saved !")
    }
    @Test
    fun validateAndSaveReminder_withValidReminderItem_navigationCommand() {
        val reminderDataItem = ReminderDataItem("myTitle","myDescription","myLocation",30.68956,31.31027)
        saveReminderViewModel.validateAndSaveReminder(reminderDataItem)
        assertThat(saveReminderViewModel.navigationCommand.getOrAwaitValue()).isEqualTo(NavigationCommand.Back)
    }

    @Test
    fun validateAndSaveReminder_withoutCoordinates_returnSnackBarIntError() {
        val reminderDataItemWithoutCoordinates = ReminderDataItem("myTitle","myDescription",null,null,null)
        saveReminderViewModel.validateAndSaveReminder(reminderDataItemWithoutCoordinates)
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_select_location)
    }

    @Test
    fun validateAndSaveReminder_withNullReminderDataItems_returnError() {
        val reminderDataItemNullable = ReminderDataItem(null,null,null,null,null)
        saveReminderViewModel.validateAndSaveReminder(reminderDataItemNullable)
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_enter_title)
    }




}