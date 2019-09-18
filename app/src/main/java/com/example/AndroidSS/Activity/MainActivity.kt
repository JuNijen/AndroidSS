package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import com.example.AndroidSS.R


//20190916 제작

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app_login)
        SetMain()
    }

    private fun SetMain()
    {
        //버튼과 에딧 텍스트를 찾아 연결해준다.
        var sendButton = findViewById<Button>(R.id.button)
        var editText = findViewById<EditText>(R.id.nameEdit)

        //에딧 텍스트의 힌트를 설정.
        editText.setHint(R.string.TEXT_WRITE_YOUR_NAME)

        //버튼이 눌렸을 경우 동작.
        sendButton.setOnClickListener {
            sendButtonOnClick(editText)
        }
    }

    private fun sendButtonOnClick(arg1: EditText)
    {
        if (arg1.text.isEmpty())
        {
            //값이 제대로 입력되지 않았을 경우.
            Toast.makeText(this, R.string.ERR_EDITTEXT_NOVALUE, Toast.LENGTH_SHORT).show()
        }
        else
        {
            //입력값을 토스트로 뛰운다.
            Toast.makeText(this, "${arg1.text}님, 반갑습니다.", Toast.LENGTH_LONG).show()

            //에디터 텍스트값을 비운다.
            arg1.setText("")

            // test_app_buttons으로 화면 이동
            val intent = Intent(this, ButtonsActivity::class.java)
            //intent.putExtra("인텐트 키값","전달할 값")
            startActivity(intent)

            finish()
        }
    }
}