package com.unsarten.app.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users_data")
data class UserData(
    @PrimaryKey var userId: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "phone_number") var phoneNumber: String,
): Serializable