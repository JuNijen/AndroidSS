package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.content.Intent
import android.view.View
import android.widget.*
import android.net.Uri
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.example.AndroidSS.Func.*
import com.example.AndroidSS.Service.NoticePowerOffBtn
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import com.example.AndroidSS.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


//20190916 제작
//20190917 수정 (CALL, TTS)
//20190919 수정 (GPS, RECORD)

class ButtonsActivity : AppCompatActivity()
{
    private lateinit var ttsFunc: TTSFunc
    private lateinit var audioRecordFunc : AudioRecordFunc

    private lateinit var mStartRecordBtn: Button
    private lateinit var mStopRecordBtn: Button
    private lateinit var mStartPlayBtn: Button
    private lateinit var mStopPlayBtn: Button
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var mVisualizer: BarVisualizer? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        things()
        initActivity()
        setVisualizer()
        setButtons()
        readLogFile()
    }

    override fun onPause()
    {
        ttsFunc.callStopTTS()
        super.onPause()
    }


    // private fun ---------------------------------------------------------------------------------

    private fun initActivity()
    {
        ttsFunc = TTSFunc()
        ttsFunc.callInitFunc(this)

        audioRecordFunc = AudioRecordFunc()
    }

    private fun setButtons()
    {
        findViewById<ImageButton>(R.id.callBtn).setOnClickListener {
            callBtnOnClick()
        }

        findViewById<ImageButton>(R.id.gpsBtn).setOnClickListener {
            gpsBtnBtnOnClick()
        }

        findViewById<ImageButton>(R.id.voiceBtn).setOnClickListener {
            voiceBtnBtnOnClick()
        }

        findViewById<ImageButton>(R.id.speakerBtn).setOnClickListener {
            speakerBtnOnClick()
        }

        findViewById<Button>(R.id.returnHomeBtn).setOnClickListener {
            returnHomeBtnOnClick()
        }

        setRecordButtons()
    }

    private fun setRecordButtons()
    {
        mStartRecordBtn = findViewById(R.id.btnRecord)
        mStopRecordBtn = findViewById(R.id.btnStop)
        mStartPlayBtn = findViewById(R.id.btnPlay)
        mStopPlayBtn = findViewById(R.id.btnStopPlay)

        //초기 버튼 설정
        mStartRecordBtn.isEnabled = true
        mStopRecordBtn.isEnabled = false
        mStartPlayBtn.isEnabled = false
        mStopPlayBtn.isEnabled = false

        //시작 버튼
        mStartRecordBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = false
            mStopRecordBtn.isEnabled = true
            mStartPlayBtn.isEnabled = false
            mStopPlayBtn.isEnabled = false

            audioRecordFunc.callStartBtnOnClick(this)
        }

        //정지 버튼
        mStopRecordBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = true
            mStopPlayBtn.isEnabled = true

            audioRecordFunc.callStopBtnOnClick()
            Toast.makeText(this, R.string.TEXT_AUDIO_RECORD_STOPPED, Toast.LENGTH_SHORT).show()
        }

        //재생 시작 버튼
        mStartPlayBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = false
            mStopPlayBtn.isEnabled = true

            audioRecordFunc.callPlayBtnOnClick()
            startVisualizer()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STARTED,Toast.LENGTH_SHORT).show()
        }

        //재생 정지 버튼
        mStopPlayBtn.setOnClickListener{

            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = true
            mStopPlayBtn.isEnabled = false

            audioRecordFunc.callStopPlayBtnOnClick()
            stopVisualizer()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STOPPED, Toast.LENGTH_SHORT).show()
        }
    }

    //20190923 추가 - audio wave visualizer 추가를 위함.
    //참고자료 ::
    //https://androidexample365.com/a-light-weight-and-easy-to-use-audio-visualizer-for-android/
    //20190923 수정 - null check, add release
    //https://github.com/gauravk95/audio-visualizer-android/blob/c0c0bb29cc12c4b09eb573b12c517256b629e1b7/app/src/main/java/com/gauravk/audiovisualizersample/ui/CircleLineActivity.java
    private fun setVisualizer()
    {
        mVisualizer = findViewById<View>(R.id.barVisualizer) as BarVisualizer
    }

    private fun startVisualizer()
    {
        //get the AudioSessionId from your MediaPlayer and pass it to the visualizer
        val audioSessionId = audioRecordFunc.callGetAudioSessionID()

        if (audioSessionId != -1)
        {
            if(mVisualizer != null)
            {
                mVisualizer?.show()
                mVisualizer?.setAudioSessionId(audioSessionId)
            }
        }
    }

    private fun stopVisualizer()
    {
        //TODO: check for completion of audio eg. using MediaPlayer.OnCompletionListener()
        if (mVisualizer != null)
        {
            mVisualizer?.clearAnimation()
            mVisualizer?.release()
            mVisualizer?.hide()
        }
    }

    private fun callBtnOnClick()
    {
        val intent = Intent(Intent.ACTION_CALL)

        //PermissionFunc 를 생성하고 Check 한다.
        //Check 에는 Alert > Request 기능이 포함되어있다.
        if (PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_CALL_PHONE))
        {
            //TODO::인텐트 저친구 뭐가문젠지 잘 모르겠음. 아무튼 해결해야함
            intent.data = Uri.parse("tel:${getString(R.string.TEXT_CALL_NUM)}")
            startActivity(intent)
        }
    }

    private fun gpsBtnBtnOnClick()
    {
        var strCurrentPosition = getString(R.string.TEXT_GPS_CURRENT_POSITION)
        var strAdress = GPSFunc().callGetKoreanAdress(this)

        findViewById<TextView>(R.id.textView1).text = strCurrentPosition + strAdress
    }

    private fun voiceBtnBtnOnClick()
    {
        //TODO::여기에 있어야 할까.
        var bInternet = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_INTERNET)
        var bReadSms = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_READ_SMS)
        var bReceiveSms = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_RECEIVE_SMS)
        var bReadContact = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_READ_CONTACTS)

        if(!bInternet)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_INTERNET)
        if(!bReadSms)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_READ_SMS)
        if(!bReceiveSms)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_RECEIVE_SMS)
        if(!bReadContact)
            PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_READ_CONTACTS)


        //20190924 PowerOffSeivice (가제)의 적용을 위하여 추가됨.
        //TODO::꼭 여기에 넣어줬어야했는가? 다른데 어디에 넣어야 할 지는 모르겠음.
        this.startService(Intent(this, NoticePowerOffBtn::class.java))
    }

    private fun returnHomeBtnOnClick()
    {
        // 메인으로 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun speakerBtnOnClick()
    {
        //TODO:: 이 부분은 임시입니다.
        ttsFunc.callPlayTTS(getString(R.string.TEXT_PERMISSION_NOTICE, getString(R.string.TEXT_EMPTY)))
    }

    private fun readLogFile()
    {
        var fileName = "MEMO.txt"
        var string = loadData(fileName)

        findViewById<TextView>(R.id.textView2).text = string
    }


    //20191001 제작
    //참고자료 ::
    //https://stackoverflow.com/questions/9544737/read-file-from-assets
    fun loadData(inFile: String): String
    {
        var tContents = ""

        try
        {
            val stream = assets.open(inFile)

            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        }
        catch (e: IOException)
        {
            // Handle exceptions here
        }
        return tContents
    }

    fun things()
    {
        setContentView(R.layout.app_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
            R.id.nav_tools, R.id.nav_share, R.id.nav_send), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean
    {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
