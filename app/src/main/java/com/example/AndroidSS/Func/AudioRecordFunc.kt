package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import android.media.MediaRecorder
import android.media.MediaPlayer
import android.widget.Toast
import android.util.Log

import java.text.SimpleDateFormat
import java.io.IOException
import java.util.*

import com.example.AndroidSS.R


//20190918 제작
//제작에 참고한 자료 ::
//https://developer.android.com/guide/topics/media/mediarecorder


class AudioRecordFunc
{
    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mFileName = ""


    // public fun ----------------------------------------------------------------------------------

    fun callSetFileName(appCompactActivity: AppCompatActivity)
    {
        setFileName(appCompactActivity)
    }

    fun callStartBtnOnClick(appCompactActivity: AppCompatActivity)
    {
        startBtnOnClick(appCompactActivity)
    }

    fun callStopBtnOnClick()
    {
        stopBtnOnClick()
    }

    fun callPlayBtnOnClick()
    {
        playBtnOnClick()
    }

    fun callStopPlayBtnOnClick()
    {
        stopPlayBtnOnClick()
    }


    // private fun ---------------------------------------------------------------------------------

    private fun setFileDirectory(appCompactActivity: AppCompatActivity)
    {
        var strAppDir = StorageController().callGetAppFileDirectory(appCompactActivity)
        var strAppDirName = StorageController().callGetAppFileDirectoryName(appCompactActivity)

        //storage/emulated/0/Android/data/com.example.AndroidSS 를 생성하거나 이미 존재하면
        StorageController().callCreateFileDirectory(strAppDir, strAppDirName)

        //recorded_files 폴더를 만들어준다.
        //storage/emulated/0/Android/data/com.example.AndroidSS/recorded_files
        var strRecordDir  = StorageController().callGetAppFileDirectory(appCompactActivity) + strAppDirName
        var strRecordDirName  = appCompactActivity.getString(R.string.APP_DATA_DIRECTORY_NAME_VOICE)

        StorageController().callCreateFileDirectory(strRecordDir, strRecordDirName)
    }

    private fun setFileName(appCompactActivity: AppCompatActivity)
    {
        mFileName = ""

        setFileDirectory(appCompactActivity)

        var mAppDirectory = StorageController().callGetAppFileDirectory(appCompactActivity) + StorageController().callGetAppFileDirectoryName(appCompactActivity)
        var mRecordFileDirectory = mAppDirectory + appCompactActivity.getString(R.string.APP_DATA_DIRECTORY_NAME_VOICE_DIR)
        var mCurrentTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        var mCurrentDateTime =
            appCompactActivity.getString(R.string.TEXT_AUDIO_FILE_NAME_FORMAT, mCurrentTime)

        mFileName = mRecordFileDirectory + mCurrentDateTime
    }

    private fun startBtnOnClick(appCompactActivity: AppCompatActivity)
    {
        var isRecordAudioEnabled =
            PermissionFunc().callCheckPermission(appCompactActivity, MY_PERMISSION.E_RECORD_AUDIO)
        var isWriteExternalStroageEnabled = PermissionFunc().callCheckPermission(
            appCompactActivity,
            MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE
        )

        //두 권한 모두 가지고 있을 경우
        if (isRecordAudioEnabled && isWriteExternalStroageEnabled)
        {
            mRecorder = MediaRecorder()
            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            callSetFileName(appCompactActivity)
            mRecorder?.setOutputFile(mFileName)

            try
            {
                mRecorder?.prepare()
            }
            catch (e: IOException)
            {
                Log.e(
                    appCompactActivity.getString(R.string.TEXT_AUDIO_RECORD_IS_RUNNING),
                    appCompactActivity.getString(R.string.ERR_FROM_CODE)
                )
            }
            mRecorder?.start()
            Toast.makeText(appCompactActivity, R.string.TEXT_AUDIO_RECORD_STARTED, Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopBtnOnClick()
    {
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
    }

    private fun playBtnOnClick()
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

    private fun stopPlayBtnOnClick()
    {
        mPlayer?.release()
        mPlayer = null
    }
}