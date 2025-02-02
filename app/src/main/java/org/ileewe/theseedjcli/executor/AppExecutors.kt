package org.ileewe.theseedjcli.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    companion object {
        private var instance: AppExecutors? = null
        public fun getAInstance(): AppExecutors {
            if (instance == null) {
                instance = AppExecutors()
            }
            return instance as AppExecutors
        }
    }

    private val mDiskIO: Executor = Executors.newSingleThreadExecutor()
    private val mainThreadExecutor = MainThreadExecutor()

    //Accessing private values from outside of class
    public val diskIO: Executor = mDiskIO
    public val mainThread: Executor = mainThreadExecutor

    private class MainThreadExecutor: Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
        override fun execute(p0: Runnable?) {
            p0?.let {
                    command ->
                mainThreadHandler.post(command)
            }
        }

    }
}