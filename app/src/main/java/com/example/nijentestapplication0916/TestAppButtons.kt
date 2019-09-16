package com.example.nijentestapplication0916

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

//인텐트를 위하여 추가.
import android.content.Intent


class TestAppButtons : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_app_buttons)
    }
/*
    public void Func()
    {
        //Find the EditText
        var editText = findViewById<EditText>(R.id.editText);
        editText.setHint("이름을 입력 해 주십시오.");

        //Find the Button
        var sendButton = findViewById<Button>(R.id.button);

        //Set on click
        sendButton.setOnClickListener {

            //입력값을 토스트로 뛰운다.
            Toast.makeText(this, "${editText.text}님, 반갑습니다.", Toast.LENGTH_LONG).show();

            //에디터 텍스트값을 비운다.
            editText.setText("");

            // 메인으로 화면 이동
            //val intent = Intent(this,MainPage::class.java)
            //intent.putExtra("인텐트 키값","전달할 값")
            //startActivity(intent)

            // 메인으로 화면 이동
            val intent = Intent(this, TestAppButtons::class.java)
            startActivity(intent)
        }
    }
 */
}
