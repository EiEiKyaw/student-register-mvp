package me.kaungmyatmin.studentreg

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "father_name")
    val fatherName: String,

    @ColumnInfo(name = "age")
    val age: Int,

    @ColumnInfo(name = "phone")
    val phone: String
):Parcelable