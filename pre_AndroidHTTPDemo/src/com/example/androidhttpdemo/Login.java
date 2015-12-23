package com.example.androidhttpdemo;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.example.androidhttpdemo.R;
import com.web.WebService;
import com.web.WebServicePost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

    // 登陆按钮
    private Button logbtn;
    // 调试文本，注册文本
    private TextView infotv, regtv;
    // 显示用户名和密码
    EditText username, password;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回的数据
    private String info;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        // 获取控件
        username = (EditText) findViewById(R.id.user_et);
        password = (EditText) findViewById(R.id.pass_et);
        logbtn = (Button) findViewById(R.id.login);
        regtv = (TextView) findViewById(R.id.register);
        infotv = (TextView) findViewById(R.id.info);

        
        // 设置按钮监听器
        logbtn.setOnClickListener(this);
        regtv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login:
            // 检测网络，无法检测wifi
            if (!checkNetwork()) {
                Toast toast = Toast.makeText(Login.this,"The Network is Not Connected!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            }
            // 提示框
            dialog = new ProgressDialog(this);
            dialog.setTitle("Attention");
            dialog.setMessage("Logging on，Please Waiting...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyThread()).start();
            break;
        /*
        case R.id.register:
            Intent regItn = new Intent(Login.this, Register.class);
            // overridePendingTransition(anim_enter);
            startActivity(regItn);
            break;
         */
        }
    }

        // 子线程接收数据，主线程修改数据
        public class MyThread implements Runnable {
            @Override
            public void run() {
                info = WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
                // info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        infotv.setText(info);
                        dialog.dismiss();
                    }
                });
            }
        }

    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
