package com.itp.mytelephonerit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_mobile,et_msg;
    ImageButton btn_call,btn_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_mobile= findViewById(R.id.et_mobile_number);
        et_msg=findViewById(R.id.et_message);
        btn_call=findViewById(R.id.btn_call);
        btn_sms=findViewById(R.id.btn_sms);

        //Android Runtime permission code.
        //step 1: check the permission status whether it is granted or not??

       int call_status= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
       int sms_status= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);




        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(call_status== PackageManager.PERMISSION_GRANTED)
                {
                    call();
                }else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }


            }
        });

        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms=et_msg.getText().toString();
                if(TextUtils.isEmpty(sms))
                {
                    Toast.makeText(MainActivity.this, "Please enter Some mesg to send sms", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(sms_status== PackageManager.PERMISSION_GRANTED)
                    {
                        sendSMS();
                    }else
                    {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},2);
                    }
                }
              }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED && requestCode==1)
        {
            call();
        }else if(grantResults[0]==PackageManager.PERMISSION_GRANTED && requestCode==2)
        {
            sendSMS();
        }else
        {
            Toast.makeText(this, "user is not allowed", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendSMS() {
        SmsManager smsManager=SmsManager.getDefault();

        String MobileNumber=et_mobile.getText().toString();
        String[] MobileNumbers=MobileNumber.split(",");

        for (String number:MobileNumbers)
        {
            Intent sendIntent=new Intent(MainActivity.this,SendActivity.class);
            PendingIntent ps= PendingIntent.getActivity(MainActivity.this,0,sendIntent,PendingIntent.FLAG_MUTABLE);

            Intent deliveryIntent=new Intent(MainActivity.this,DeliveredActivity.class);
            PendingIntent pd= PendingIntent.getActivity(MainActivity.this,0,deliveryIntent,PendingIntent.FLAG_MUTABLE);

            smsManager.sendTextMessage(number,null,et_msg.getText().toString(),ps,pd);


        }

    }

    private void call() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+et_mobile.getText().toString()));
        startActivity(intent);
    }
}