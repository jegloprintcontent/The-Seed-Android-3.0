package org.ileewe.theseedjcli.application

import android.app.Application
import org.ileewe.theseedjcli.db.SeedDatabase
import org.ileewe.theseedjcli.executor.AppExecutors
import org.ileewe.theseedjcli.repository.HomeRepository
import org.ileewe.theseedjcli.repository.MinistriesRepository
import org.ileewe.theseedjcli.repository.SeedRepository
import org.ileewe.theseedjcli.repository.SermonsRepository

class SeedApplication: Application() {

    private val database by lazy { SeedDatabase.getDB(this@SeedApplication) }
    val appExecutors by lazy { AppExecutors.getAInstance() }
    val repository by lazy { HomeRepository(database.postDao(), appExecutors) }
    val seedRepository by lazy { SeedRepository(database.postDao(), appExecutors) }
    val sermonRepository by lazy { SermonsRepository(database.postDao(), appExecutors)}
    val ministriesRepository by lazy { MinistriesRepository(database.postDao(), appExecutors) }
}