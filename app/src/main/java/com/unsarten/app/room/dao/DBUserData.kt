package com.unsarten.app.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserData::class],
    version = 1
)
abstract class DBUserData: RoomDatabase() {
    abstract fun daoUser(): UserDataDao
}
