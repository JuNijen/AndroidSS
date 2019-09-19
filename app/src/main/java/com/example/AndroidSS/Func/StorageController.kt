package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import android.os.Environment
import android.util.Log
import java.io.File

import com.example.AndroidSS.R


//기본 폴더 위치 ::
//storage/emulated/0/Android/data/com.example.AndroidSS
class StorageController
{
    // public fun ----------------------------------------------------------------------------------

    fun callGetAppFileDirectory(app_activity: AppCompatActivity) : String
    {
        return getAppFileDirectory(app_activity)
    }

    fun callGetAppFileDirectoryName(app_activity: AppCompatActivity) : String
    {
        return getAppFileDirectoryName(app_activity)
    }

    fun callCreateFileDirectory(str_dir : String, str_dir_name : String) : Boolean
    {
        return createFileDirectory(str_dir, str_dir_name)
    }


    // private fun ---------------------------------------------------------------------------------

    //storage/emulated/0/Android/data/
    private fun getAppFileDirectory(app_activity: AppCompatActivity) : String
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + app_activity.getString(
            R.string.APP_DATA_DIRECTORY)
    }

    //com.example.AndroidSS
    private fun getAppFileDirectoryName(app_activity: AppCompatActivity) : String
    {
        return app_activity.getString(R.string.APP_DATA_DIRECTORY_NAME)
    }

    //createFileDirectory 제작에 참고한 자료 ::
    //https://itstudentstudy.tistory.com/48
    private fun createFileDirectory(str_dir : String, str_dir_name : String) : Boolean
    {
        var bReady = false

        //storage/emulated/0/Android/data/com.example.AndroidSS
        val dir = File(str_dir, str_dir_name)
        // 폴더가 제대로 만들어졌는지 체크

        if (!dir.mkdirs())
        {
            Log.e("@@StorageController", str_dir_name +"Directory not created")
        }
        else
        {
            Log.e("@@StorageController", str_dir_name + "Directory created")
            bReady = true
        }
        return bReady
    }
}