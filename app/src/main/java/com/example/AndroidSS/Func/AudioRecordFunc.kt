package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import android.media.MediaRecorder
import android.widget.Button
import android.os.Bundle
import android.util.Log


import android.os.Environment
import android.widget.Toast
import com.example.AndroidSS.R

import java.io.IOException

import android.media.MediaPlayer


//20190918 제작
//제작에 참고한 자료 ::
//https://developer.android.com/guide/topics/media/mediarecorder


class AudioRecordFunc : AppCompatActivity()
{
    private var mFileName = ""

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null

    private lateinit var startRecordBtn: Button
    private lateinit var stopRecordBtn: Button
    private lateinit var startplayBtn: Button
    private lateinit var stopPlayBtn: Button


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_voice_record)

        SetButtons()

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
        mFileName += getString(R.string.TEXT_AUDIO_FILE_NAME)
    }

    private fun SetButtons()
    {
        startRecordBtn = findViewById(R.id.btnRecord)
        stopRecordBtn = findViewById(R.id.btnStop)
        startplayBtn = findViewById(R.id.btnPlay)
        stopPlayBtn = findViewById(R.id.btnStopPlay)

        startRecordBtn.isEnabled = true
        stopRecordBtn.isEnabled = false
        startplayBtn.isEnabled = false
        stopPlayBtn.isEnabled = false

        startRecordBtn.setOnClickListener{
            StartBtnOnClick()
        }

        stopRecordBtn.setOnClickListener{
            StopBtnOnClick()
        }

        startplayBtn.setOnClickListener{
            PlayBtnOnClick()
        }
        stopPlayBtn.setOnClickListener{
            StopPlayBtnOnClick()
        }
    }

    private fun StartBtnOnClick()
    {
        var isRecordAudioEnabled = PermissionFunc().CallCheckPermission(this, MY_PERMISSION.E_RECORD_AUDIO)
        var isWriteExternalStroageEnabled = PermissionFunc().CallCheckPermission(this, MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE)

        //두 권한 모두 가지고 있을 경우
        if (isRecordAudioEnabled && isWriteExternalStroageEnabled)
        {
            startRecordBtn.isEnabled = false
            stopRecordBtn.isEnabled = true
            startplayBtn.isEnabled = false
            stopPlayBtn.isEnabled = false

            mRecorder = MediaRecorder()
            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder?.setOutputFile(mFileName)
            try
            {
                mRecorder?.prepare()
            }
            catch (e: IOException)
            {
                Log.e(getString(R.string.TEXT_AUDIO_RECORD_IS_RUNNING), getString(R.string.ERR_FROM_CODE))
            }
            mRecorder?.start()
            Toast.makeText(this, R.string.TEXT_AUDIO_RECORD_STARTED, Toast.LENGTH_LONG).show()
        }
    }


    private fun StopBtnOnClick()
    {
        startRecordBtn.isEnabled = true
        stopRecordBtn.isEnabled = false
        startplayBtn.isEnabled = true
        stopPlayBtn.isEnabled = true

        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
        Toast.makeText(this, R.string.TEXT_AUDIO_RECORD_STOPPED, Toast.LENGTH_LONG).show()
    }

    private fun PlayBtnOnClick()
    {
        startRecordBtn.isEnabled = true
        stopRecordBtn.isEnabled = false
        startplayBtn.isEnabled = false
        stopPlayBtn.isEnabled = true

        mPlayer = MediaPlayer()
        try
        {
            mPlayer?.setDataSource(mFileName)
            mPlayer?.prepare()
            mPlayer?.start()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STARTED,Toast.LENGTH_LONG).show()
        }
        catch (e: IOException)
        {
            Log.e(getString(R.string.TEXT_AUDIO_RECORD_IS_RUNNING), getString(R.string.ERR_FROM_CODE))
        }
    }

    private fun StopPlayBtnOnClick()
    {
        mPlayer?.release()
        mPlayer = null

        startRecordBtn.isEnabled = true
        stopRecordBtn.isEnabled = false
        startplayBtn.isEnabled = true
        stopPlayBtn.isEnabled = false

        Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STOPPED,Toast.LENGTH_SHORT).show()
    }
}