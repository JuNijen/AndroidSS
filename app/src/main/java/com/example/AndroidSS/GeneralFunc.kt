package com.example.AndroidSS

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GeneralFunc
{
    // public fun ----------------------------------------------------------------------------------

    //add_negative_btn  default value : false
    //per_type          default value : MY_PERMISSION.E_NONE
    //per_type는 퍼미션요청이 필요할 경우에만 받도록 한다.
    fun CallCreateAlertDialog(app_activity: AppCompatActivity, s_dialog_title : String,
                        s_dialog_message : String, add_negative_btn : Boolean = false,
                        per_type: MY_PERMISSION = MY_PERMISSION.E_NONE)
    {
        CreateAlertDialog(app_activity, s_dialog_title, s_dialog_message, add_negative_btn, per_type)
    }


    // private fun ---------------------------------------------------------------------------------

    //20190918 15:05
    //굳이 이렇게 짜야하나에 대한 자괴감이 들고있지만 일단 이미 진행한거 만들기로 한다.
    //per_type는 퍼미션요청이 필요할 경우에만 받도록 한다.

    private fun CreateAlertDialog(app_activity: AppCompatActivity, s_dialog_title : String,
                                  s_dialog_message : String, add_negative_btn : Boolean = false,
                                  per_type: MY_PERMISSION = MY_PERMISSION.E_NONE)
    {
        var str_title = s_dialog_title
        var str_message = s_dialog_message
        val builder = AlertDialog.Builder(app_activity)

        //내용물이 없으면 기본값을 채워준다.
        //기본값은 공백으로 하는게 나을까? 굳이 기본값이 필요하지 않을지도.
        if(str_title.isNullOrEmpty()||str_message.isNullOrEmpty())
        {
            return
        }

        builder.setTitle(str_title)
        builder.setMessage(str_message)
        builder.setCancelable(false)

        //per_type가 비어있지 않다면
        if(per_type != MY_PERMISSION.E_NONE)
        {
            //요청 팝업을 띄워준다. 버튼을 누르면 리퀘스트.
            builder.setPositiveButton(R.string.BTN_OK)
            {
                    dialogInterface, i -> PermissionFunc().CallRequestPermission(app_activity, per_type)
            }
        }
        else
        {
            builder.setPositiveButton(R.string.BTN_OK)
            {
                    dialogInterface, i -> dialogInterface.cancel()
            }
        }

        if(add_negative_btn)
        {
            builder.setNegativeButton(R.string.BTN_CANCEL)
            {
                    dialogInterface, i -> dialogInterface.cancel()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}