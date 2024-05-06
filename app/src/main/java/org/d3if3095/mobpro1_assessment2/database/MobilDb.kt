package org.d3if3095.mobpro1_assessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3095.mobpro1_assessment2.model.Mobil

@Database(entities = [Mobil::class], version = 1, exportSchema = false)
abstract class MobilDb : RoomDatabase() {

    abstract val dao: MobilDao

    companion object {

        @Volatile
        private var INSTANCE: MobilDb? = null

        fun getInstance(context: Context): MobilDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MobilDb::class.java,
                        "mahasiswa.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}