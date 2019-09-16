package com.example.nijentestapplication0916

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app_login)
        SetMain();
    }

    fun SetMain()
    {
        //버튼과 에딧 텍스트를 찾아 연결해준다.
        var sendButton = findViewById<Button>(R.id.button);
        var editText = findViewById<EditText>(R.id.nameEdit);

        //에딧 텍스트의 힌트를 설정.
        editText.setHint(R.string.TEXT_WRITE_YOUR_NAME);

        //버튼이 눌렸을 경우 동작.
        sendButton.setOnClickListener {
            sendButtonOnClick(editText);
        }
    }

    fun sendButtonOnClick(arg1: EditText)
    {
        if(arg1.text.isEmpty())
        {
            //값이 제대로 입력되지 않았을 경우.
            Toast.makeText(this, R.string.ERR_EDITTEXT_NOVALUE, Toast.LENGTH_SHORT).show();
        }
        else
        {
            //입력값을 토스트로 뛰운다.
            Toast.makeText(this, "${arg1.text}님, 반갑습니다.", Toast.LENGTH_LONG).show();

            //에디터 텍스트값을 비운다.
            arg1.setText("")

            // test_app_buttons으로 화면 이동
            val intent = Intent(this, TestAppButtons::class.java)
            //intent.putExtra("인텐트 키값","전달할 값")
            startActivity(intent)

            finish()
        }
    }
}



/* 190916 inflearn 강의 ( https://bit.ly/2kL0hew )

class KotlinStudyMain
{
    fun main(args: Array<String>) {
        println("Hello world")
        println("빌드 단축키 : shift + F10 이었던거같은데...")
        println("빌드 단축키 : alt + shift + F10")

        printKotlin();
        valTest();
        StringInterpolationTest();
        IfTest1(1 ,20);
        IfTest2(1 ,20);
        printProduct("스트링", "스트링2");
        getStringlengthTset("스트링 길이");
        whileLoopTest();
    }

    fun printKotlin() : Unit
    {
        println("Uint 은 Java의 void 리턴 역할과 동일하다.")
    }

    fun valTest() : Int
    {
        //val : 읽기전용 변수
        //값의 할당이 1회만 가능, Java의 final과 유사한 특성을 지님.

        val a: Int = 1  // 즉시 할당
        val b = 2       //int type 추론
        //val c: Int      // 컴파일 오류, 초기화가 필요함.
        //c = 3           // 컴파일 오류, 읽기전용.

        println(a + b)

        return 0;
    }

    fun StringInterpolationTest() : Unit
    {
        //문자열 보간법
        /* 보간(Interpolation) 이란
            ①새로운 점을 만들기 위해 수많은 점들을 평균화시키는 것.
            이 방법은 샘플점들을 직선으로 연결하지 않고 곡선으로 연결함으로써
            본래 신호파형에 대한 변형을 최소화시켜 준다.

            ②영상신호의 표준방식 변환시 기존의 정보로부터
            새로운 정보를 만들어야 하는데 가령 525라인에서
            625라인을 만들 때 처리되는 방식을 말한다.

            출처: https://iskim3068.tistory.com/35 [ikfluencer]
         */

        var a = 1
        val s1 = "a is $a"

        //s1의 현재 상태를 출력.
        println(s1)

        //a의 값을 변경하고 - - - s1에서 보간한 s2의 상태를 출력.
        a = 2
        //arbitary expression in template
        val s2 = "${s1.replace("is", "was")}, but now is $a"
        println(s2)
    }

    fun IfTest1(a: Int, b: Int):Int
    {
        if (a > b)
        {
            return a
        }
        else
        {
            return b
        }
    }

    //IfTest1과 같은 내용을 하단과 같이 축약 할 수 있다
    fun IfTest2(a:Int, b:Int) = if(a > b) a else b


    fun parseIntTest(str: String): Int?
    {
        //값이 null 일 수 있는 경우 타입에 nullable 마크를 명시해야함함
        //정수가 아닌 경우 null을 리턴

        //이러면 안되는데 일단은 오류방지용.
        return null
    }


    fun printProduct(arg1: String, arg2: String)
    {
        //nullable 타입의 변수를 접근 할 때는 반드시 null 체크를 해 주어야 하고,
        //그렇지 않으면 컴파일 오류가 발생하게 된다.

        val x : Int? = parseIntTest(arg1)
        val y : Int? = parseIntTest(arg2)

        if (x != null && y != null)
        {
            println(x * y)
        }
        else
        {
            println("either '$arg1' or '$arg2' is not a number")
        }
    }

    fun getStringlengthTset(obj: Any): Int?
    {
        // 자동 타입 변환 ::
        // 타입 체크만 해도 자동으로 타입 변환이 됨.

        if(obj is String)
        {
            //'obj' 가 자동으로 String 타입으로 변환 됨
            return obj.length
        }

        return null
    }

    fun whileLoopTest()
    {
        val items = listOf("apple", "banana", "kiwi")
        var index = 0;

        while (index < items.size)
        {
            println("item at $index is ${items[index]}")
            index ++
        }
    }

    /*
    fun whenExpressionTest(obj: Any): String
    {
        //switch - case 와 동일한 기능.

        when (obj)
        {
            1           -> "One"
            "Hello"     -> "Greeting"
            is Long     -> "Long"
            !is String  -> "Not a string"
            else        -> "Unknown"
        }
    }
    */
 */
