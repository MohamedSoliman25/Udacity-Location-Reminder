package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource (var reminders:MutableList<ReminderDTO>? = mutableListOf()): ReminderDataSource {

    private var isReturnError = false
    fun setReturnsError(value: Boolean) {
        isReturnError = value
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
            if (isReturnError) {
                return Result.Error("Error occurred")
            }
        else {
                reminders?.let {
                    return Result.Success(ArrayList(it))
                }
                return Result.Error("Reminders not found")
            }
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (isReturnError){
            return Result.Error("Error occurred")
        }else{

            reminders?.let { reminderList->
                for (i in reminderList){
                    if(i.id ==id)
                        return Result.Success(i)
                }
            }
            return Result.Error("Reminder not found")
        }
    }





}