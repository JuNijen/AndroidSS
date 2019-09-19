package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import android.media.MediaRecorder
import android.media.MediaPlayer
import android.os.Environment
import android.util.Log

import java.text.SimpleDateFormat
import java.io.IOException
import java.util.*

import com.example.AndroidSS.R
import java.io.File


//20190918 제작
//제작에 참고한 자료 ::
//https://developer.android.com/guide/topics/media/mediarecorder


class AudioRecordFunc
{
    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mFileName = ""


    // public fun ----------------------------------------------------------------------------------

    fun callSetFileName(app_activity: AppCompatActivity)
    {
        setFileName(app_activity)
    }

    fun CallStartBtnOnClick(app_activity: AppCompatActivity)
    {
        StartBtnOnClick(app_activity)
    }

    fun CalStopBtnOnClick()
    {
        StopBtnOnClick()
    }

    fun CallPlayBtnOnClick()
    {
        PlayBtnOnClick()
    }

    fun CallStopPlayBtnOnClick()
    {
        StopPlayBtnOnClick()
    }


    // private fun ---------------------------------------------------------------------------------

    //setFileDirectory 제작에 참고한 자료 ::
    //https://itstudentstudy.tistory.com/48
    private fun setFileDirectory(app_activity: AppCompatActivity)
    {
        var strAppDir = StorageController().callGetAppFileDirectory(app_activity)
        var strAppDirName = StorageController().callGetAppFileDirectoryName(app_activity)

        //storage/emulated/0/Android/data/com.example.AndroidSS 를 생성하거나 이미 존재하면
        StorageController().callCreateFileDirectory(strAppDir, strAppDirName)

        //recorded_files 폴더를 만들어준다.
        //storage/emulated/0/Android/data/com.example.AndroidSS/recorded_files
        var strRecordDir  = StorageController().callGetAppFileDirectory(app_activity) + strAppDirName
        var strRecordDirName  = app_activity.getString(R.string.APP_DATA_DIRECTORY_NAME_VOICE)

        StorageController().callCreateFileDirectory(strRecordDir, strRecordDirName)
    }

    private fun setFileName(app_activity: AppCompatActivity)
    {
        mFileName = ""

        setFileDirectory(app_activity)

        var mAppDirectory = StorageController().callGetAppFileDirectory(app_activity) + StorageController().callGetAppFileDirectoryName(app_activity)
        var mRecordFileDirectory = mAppDirectory + app_activity.getString(R.string.APP_DATA_DIRECTORY_NAME_VOICE_DIR)
        var mCurrentTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        var mCurrentDateTime =
            app_activity.getString(R.string.TEXT_AUDIO_FILE_NAME_FORMAT, mCurrentTime)

        mFileName = mRecordFileDirectory + mCurrentDateTime
    }

    private fun StartBtnOnClick(app_activity: AppCompatActivity)
    {
        var isRecordAudioEnabled =
            PermissionFunc().CallCheckPermission(app_activity, MY_PERMISSION.E_RECORD_AUDIO)
        var isWriteExternalStroageEnabled = PermissionFunc().CallCheckPermission(
            app_activity,
            MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE
        )

        //두 권한 모두 가지고 있을 경우
        if (isRecordAudioEnabled && isWriteExternalStroageEnabled)
        {
            mRecorder = MediaRecorder()
            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            callSetFileName(app_activity)
            mRecorder?.setOutputFile(mFileName)

            try
            {
                mRecorder?.prepare()
            }
            catch (e: IOException)
            {
                Log.e(
                    app_activity.getString(R.string.TEXT_AUDIO_RECORD_IS_RUNNING),
                    app_activity.getString(R.string.ERR_FROM_CODE)
                )
            }
            mRecorder?.start()
        }
    }

    private fun StopBtnOnClick()
    {
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
    }

    private fun PlayBtnOnClick()
    {
        mPlayer = MediaPlayer()

        try
        {
            mPlayer?.setDataSource(mFileName)
            mPlayer?.prepare()
            mPlayer?.start()
        }
        catch (e: IOException)
        {
            //Log.e("@@@PlayBtn 몬가 문제가 생김 ",mFileName)
        }
    }

    private fun StopPlayBtnOnClick()
    {
        mPlayer?.release()
        mPlayer = null
    }
}