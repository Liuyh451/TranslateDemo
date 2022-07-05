package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    Button button;
    EditText editText;
    TextView textView;
    static String to;//目标译文 可变 zh中文 en英文
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.ed1);
        textView = findViewById(R.id.tv1);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.languages);
                from=languages[pos];
                from=Convert.conv(from);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.languages);
                to=languages[pos];
                to=Convert.conv(to);
                //Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    //Button onclick属性对应的监听函数
    public void translate(View view) {

        //准备请求百度翻译接口需要的参数
        String word = editText.getText().toString();//需查询的单词 q
        System.out.println(from);
        System.out.println(to);
        String appid = "20211214001028008";//appid 管理控制台有
        String salt = (int) (Math.random() * 100 + 1) + "";//随机数 这里范围是[0,100]整数 无强制要求
        String key = "UDJ_4OELBt2lJ6QpRnrn";//密钥 管理控制台有
        String string1 = appid + word + salt + key;// string1 = appid+q+salt+密钥
        String sign = MD5Utils.getMD5Code(string1);// 签名 = string1的MD5加密 32位字母小写
        Log.d(TAG, "string1：" + string1);
        Log.d(TAG, "sign: " + sign);

        Retrofit retrofitBaidu = new Retrofit.Builder()
                .baseUrl("https://fanyi-api.baidu.com/api/trans/vip/")
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .build();
        BaiduTranslateService baiduTranslateService = retrofitBaidu.create(BaiduTranslateService.class);


        Call<RespondBean> call = baiduTranslateService.translate(word, from, to, appid, salt, sign);
        call.enqueue(new Callback<RespondBean>() {
            @Override
            public void onResponse(Call<RespondBean> call, Response<RespondBean> response) {
                //请求成功
                Log.d(TAG, "onResponse: 请求成功");
                RespondBean respondBean = response.body();//返回的JSON字符串对应的对象
                String result = respondBean.getTrans_result().get(0).getDst();//获取翻译的字符串String
                Log.d(TAG, "英译中结果" + result);
                textView.setText(result);
            }

            @Override
            public void onFailure(Call<RespondBean> call, Throwable t) {
                //请求失败 打印异常
                Log.d(TAG, "onResponse: 请求失败 " + t);
            }
        });
    }
}

