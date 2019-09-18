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


private const val LOG_TAG = "AudioRecordTest"

//20190918 제작
//제작에 참고한 자료 ::
//https://developer.android.com/guide/topics/media/mediarecorder


class AudioRecordFunc : AppCompatActivity()
{
    var LOG_TAG = "AudioRecording"
    var mFileName = ""

    var mRecorder: MediaRecorder? = null
    var mPlayer: MediaPlayer? = null

    private lateinit var startbtn: Button
    private lateinit var stopbtn: Button
    private lateinit var playbtn: Button
    private lateinit var stopplay: Button


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_voice_record)

        SetButtons()

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
        mFileName += "/AudioRecording.3gp"
    }

    private fun SetButtons()
    {
        startbtn = findViewById(R.id.btnRecord)
        stopbtn = findViewById(R.id.btnStop)
        playbtn = findViewById(R.id.btnPlay)
        stopplay = findViewById(R.id.btnStopPlay)

        startbtn.isEnabled = true
        stopbtn.isEnabled = false
        playbtn.isEnabled = false
        stopplay.isEnabled = false

        startbtn.setOnClickListener{
            StartBtnOnClick()
        }

        stopbtn.setOnClickListener{
            StopBtnOnClick()
        }

        playbtn.setOnClickListener{
            PlayBtnOnClick()
        }
        stopplay.setOnClickListener{
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
            startbtn.isEnabled = false
            stopbtn.isEnabled = true
            playbtn.isEnabled = false
            stopplay.isEnabled = false

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
                Log.e(LOG_TAG, "prepare() failed")
            }
            mRecorder?.start()
            Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG)
                .show()
        }
    }


    private fun StopBtnOnClick()
    {
        startbtn.isEnabled = true
        stopbtn.isEnabled = false
        playbtn.isEnabled = true
        stopplay.isEnabled = true

        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
        Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG)
            .show()
    }

    private fun PlayBtnOnClick()
    {
        startbtn.isEnabled = true
        stopbtn.isEnabled = false
        playbtn.isEnabled = false
        stopplay.isEnabled = true

        mPlayer = MediaPlayer()
        try
        {
            mPlayer?.setDataSource(mFileName)
            mPlayer?.prepare()
            mPlayer?.start()
            Toast.makeText(
                getApplicationContext(),
                "Recording Started Playing",
                Toast.LENGTH_LONG
            ).show()
        }
        catch (e: IOException)
        {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private fun StopPlayBtnOnClick()
    {
        mPlayer?.release()
        mPlayer = null

        startbtn.isEnabled = true
        stopbtn.isEnabled = false
        playbtn.isEnabled = true
        stopplay.isEnabled = false

        Toast.makeText(
            getApplicationContext(),
            "Playing Audio Stopped",
            Toast.LENGTH_SHORT
        ).show()
    }
}

//    override fun onRequestPermissionsResult(requestCode : Int,
//                                            permissions : String, int[] grantResults)
//    {
//        switch(requestCode)
//        {
//            case REQUEST_AUDIO_PERMISSION_CODE :
//            if (grantResults.length > 0)
//            {
//                boolean permissionToRecord = grantResults [0] == PackageManager.PERMISSION_GRANTED;
//                boolean permissionToStore = grantResults [1] == PackageManager.PERMISSION_GRANTED;
//                if (permissionToRecord && permissionToStore)
//                {
//                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG)
//                        .show()
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
//            break
//        }
//    }
//    CheckPermissions() : Boolean
//    {
//        int result = ContextCompat . checkSelfPermission (getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat . checkSelfPermission (getApplicationContext(), RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//    }
//    private void RequestPermissions()
//    {
//        ActivityCompat.requestPermissions(
//            MainActivity.this,
//            new String []{ RECORD_AUDIO, WRITE_EXTERNAL_STORAGE },
//            REQUEST_AUDIO_PERMISSION_CODE
//        )
//    }
//}

/*
//여기에 들어오기도 전부터 터짐.
override fun onCreate(icicle: Bundle?)
{
    super.onCreate(icicle)

    // Record to the external cache directory for visibility
    fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

    ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

    recordButton = RecordButton(this)
    playButton = PlayButton(this)
    val ll = LinearLayout(this).apply {
        addView(
            recordButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0f
            )
        )
        addView(
            playButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0f
            )
        )
    }
    setContentView(ll)
}

private var fileName: String = ""

private var recordButton: RecordButton? = null
private var recorder: MediaRecorder? = null

private var playButton: PlayButton? = null
private var player: MediaPlayer? = null

// Requesting permission to RECORD_AUDIO
private var permissionToRecordAccepted = false
private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
)
{
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION)
    {
        grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
    else
    {
        false
    }
    if (!permissionToRecordAccepted) finish()
}

private fun onRecord(start: Boolean) = if (start)
{
    startRecording()
}
else
{
    stopRecording()
}

private fun onPlay(start: Boolean) = if (start)
{
    startPlaying()
}
else
{
    stopPlaying()
}

private fun startPlaying()
{
    player = MediaPlayer().apply {
        try
        {
            setDataSource(fileName)
            prepare()
            start()
        }
        catch (e: IOException)
        {
            Log.e(LOG_TAG, "prepare() failed")
        }
    }
}

private fun stopPlaying()
{
    player?.release()
    player = null
}

private fun startRecording()
{
    recorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setOutputFile(fileName)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try
        {
            prepare()
        }
        catch (e: IOException)
        {
            Log.e(LOG_TAG, "prepare() failed")
        }

        start()
    }
}

private fun stopRecording()
{
    recorder?.apply {
        stop()
        release()
    }
    recorder = null
}

internal inner class RecordButton(ctx: Context) : Button(ctx)
{

    var mStartRecording = true

    var clicker: OnClickListener = OnClickListener {
        onRecord(mStartRecording)
        text = when (mStartRecording)
        {
            true -> "Stop recording"
            false -> "Start recording"
        }
        mStartRecording = !mStartRecording
    }

    init
    {
        text = "Start recording"
        setOnClickListener(clicker)
    }
}

internal inner class PlayButton(ctx: Context) : Button(ctx)
{
    var mStartPlaying = true
    var clicker: OnClickListener = OnClickListener {
        onPlay(mStartPlaying)
        text = when (mStartPlaying)
        {
            true -> "Stop playing"
            false -> "Start playing"
        }
        mStartPlaying = !mStartPlaying
    }

    init
    {
        text = "Start playing"
        setOnClickListener(clicker)
    }
}

override fun onStop()
{
    super.onStop()
    recorder?.release()
    recorder = null
    player?.release()
    player = null
}
*/