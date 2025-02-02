package org.ileewe.theseedjcli.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.ileewe.theseedjcli.converter.CategoryConverter
import org.ileewe.theseedjcli.converter.ContentConverter
import org.ileewe.theseedjcli.converter.GuidConverter
import org.ileewe.theseedjcli.converter.TitleConverter
import org.ileewe.theseedjcli.dao.PostDao
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post

@Database(entities = [Post::class, Category::class], version = 2, exportSchema = true)
@TypeConverters(TitleConverter::class, ContentConverter::class, CategoryConverter::class, GuidConverter::class)
abstract class SeedDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: SeedDatabase? = null

        // Defining Migration from version 1 to version 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic: Adding a new column (new_column) to the 'post' table
                // Adjusting this based on your schema changes.
                database.execSQL("ALTER TABLE post ADD COLUMN new_column INTEGER DEFAULT 0")
            }
        }

        // Singleton pattern for getting the SeedDatabase instance
        fun getDB(context: Context): SeedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SeedDatabase::class.java,
                    "the_seed"
                )
                    .addMigrations(MIGRATION_1_2) // Add migration
                    .fallbackToDestructiveMigration() // Add this line to clear and recreate the database if necessary
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
