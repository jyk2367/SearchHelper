package com.example.searchhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {
    private DBOpenHelper mDBOpenHelper;
    private ArrayList<URLData> myurllist;
    private URLData currentURLData;
    private int Pos;

    private WebView wView;
    private ProgressBar pgBar;
    private EditText memoET;
    private Button memoStoreBtn;
    private TextView memoTV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Intent intent=getIntent();
        mDBOpenHelper=(DBOpenHelper)intent.getSerializableExtra("MyDBOpenHelper");
        myurllist=(ArrayList<URLData>)intent.getSerializableExtra("myurllist");
        Pos=(int)intent.getIntExtra("selectedDataPos",0);
        currentURLData=myurllist.get(Pos);

        wView=(WebView) findViewById(R.id.wView);
        pgBar=(ProgressBar) findViewById(R.id.pgBar);
        memoET=(EditText) findViewById(R.id.memoET);
        memoStoreBtn=(Button)findViewById(R.id.memoStoreBtn);
        memoTV=(TextView)findViewById(R.id.memoTV);

        initWebView();

        memoTV.setText(currentURLData.title);
        memoET.setText(currentURLData.contents);

        memoStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp=memoET.getText().toString();
                if(tmp.equals(currentURLData.contents)) {
                    Toast.makeText(getApplicationContext(), "바뀐 내용이 없습니다!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mDBOpenHelper.updateColumn(currentURLData.userid,currentURLData.title,currentURLData.url,currentURLData.date,tmp)){
                    Toast.makeText(getApplicationContext(),"성공적으로 메모 업데이트 성공!!",Toast.LENGTH_SHORT).show();
                    memoET.setText(tmp);
                    Intent intentR=new Intent();
                    intentR.putExtra("selectedDataPos",Pos);
                    intentR.putExtra("sendData",tmp);
                    setResult(101,intentR);
                }
                else
                    Toast.makeText(getApplicationContext(),"메모 업데이트 실패 ㅠㅜ",Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }

    public void initWebView(){
        wView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pgBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pgBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings ws=wView.getSettings();
        ws.setJavaScriptEnabled(true);
        wView.loadUrl(currentURLData.url);
    }

    //wView에서 이전페이지 존재하면 돌아가고 없으면 종료
    @Override
    public void onBackPressed() {
        if(wView.canGoBack())
            wView.goBack();
        else
            super.onBackPressed();
    }
}
