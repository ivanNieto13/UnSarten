package com.unsarten.app.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDataDao {

    @Query("SELECT * FROM users_data")
    suspend fun getUserData(): MutableList<UserData>

    @Insert
    suspend fun addUser(user: UserData)

    @Query("DELETE FROM users_data WHERE userId=:userId")
    suspend fun deleteUser(userId: String)

    @Query("DELETE FROM users_data")
    suspend fun deleteAllUsers()

}
