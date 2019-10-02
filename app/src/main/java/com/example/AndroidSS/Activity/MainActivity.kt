package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.EditText
import android.widget.Button
import android.widget.Toast

import android.content.Intent
import com.example.AndroidSS.R
import com.example.AndroidSS.Func.MY_PERMISSION
import com.example.AndroidSS.Func.PermissionFunc


//20190916 제작

class MainActivity : AppCompatActivity()
{
    private lateinit var editText : EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app_login)
        initActivity()
    }

    private fun initActivity()
    {
        //에딧 텍스트를 찾아 연결해준다.
        editText = findViewById(R.id.nameEdit)

        //버튼이 눌렸을 경우 동작.
        findViewById<Button>(R.id.button).setOnClickListener {
            sendButtonOnClick()
        }
    }

    private fun sendButtonOnClick()
    {
        if (editText.text.isEmpty())
        {
            //값이 제대로 입력되지 않았을 경우. 오류 메세지를 출력.
            Toast.makeText(this, R.string.ERR_EDITTEXT_NOVALUE, Toast.LENGTH_SHORT).show()
        }
        else
        {
            //입력값을 토스트로 띄워주고
            Toast.makeText(this, "${editText.text}님, 반갑습니다.", Toast.LENGTH_SHORT).show()

            //에디터 텍스트값을 비운다.
            editText.setText("")

            //ButtonsActivity 로 화면 이동
            val intent = Intent(this, ButtonsActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}