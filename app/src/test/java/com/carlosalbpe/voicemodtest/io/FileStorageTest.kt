package com.carlosalbpe.voicemodtest.io

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.test.core.app.ApplicationProvider
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import com.carlosalbpe.voicemodtest.framework.io.InternalFileStorage
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.testutils.observeOnce
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.testutils.getOrAwaitValue
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FileStorageTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var fileStorage : FileStorage
    var returnedFile : File? = null

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        fileStorage = InternalFileStorage(context, Dispatchers.Main)
    }

    @Test
    fun fileCreationTestSuccess(){
        fileStorage.createFile().observeOnce {result ->
            assertEquals(result.status, Status.SUCCESS)
            assertNotNull(result.data)
            returnedFile = result.data
        }
    }

    @Test
    fun fileCreationTestError(){
        fileStorage = mock(InternalFileStorage::class.java)
        `when`(fileStorage.createFile()).thenReturn(liveData { emit(Result(status=Status.ERROR, message="error"))  })

        fileStorage.createFile().observeOnce {result ->
            assertEquals(result.status, Status.ERROR)
            assertNull(result.data)
        }
    }

    @Test
    fun fileDeletionTest(){
        //This test case is not working because delete() always returns false, it looks like a permissions problem.
        var liveData = fileStorage.createFile()
        var result = liveData.getOrAwaitValue()

        fileStorage.deleteFile(result.data!!.absolutePath).observeOnce {
            assertTrue(it.status == Status.SUCCESS)
            assertTrue(it.data!!)
        }
    }

    @Test
    fun fileDeletionTestError(){
        fileStorage.deleteFile("notExists").observeOnce {
            assert(it.status == Status.ERROR)
        }
    }

    @After
    fun cleanUp(){
        returnedFile?.delete()
    }

}
