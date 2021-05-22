package me.kaungmyatmin.studentreg

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Student::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getStudentDao(): StudentDao

    companion object {
        private var instant: AppDatabase? = null
        fun getDatabase(applicationContext: Context): AppDatabase {
            if (instant == null) {
                instant = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instant!!
        }
    }
}