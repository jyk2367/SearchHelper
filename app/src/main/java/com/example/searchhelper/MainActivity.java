package com.example.searchhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {
    private DBOpenHelper mDBOpenHelper;
    private Button urlclipBtn,urlstorageBtn;
    private WebView githubWV;
    private WebSettings gitWebSettings;
    private static final String mygitURL="https://ghchart.rshah.org/jyk2367";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlclipBtn = (Button) findViewById(R.id.urlclipBtn);
        urlstorageBtn=(Button)findViewById(R.id.urlstorageBtn);
        githubWV=(WebView) findViewById(R.id.githubWV);

//        githubWV.setWebViewClient(new WebViewClient());//클릭시 새창 안뜨게
//        gitWebSettings = githubWV.getSettings();//세부세팅 등록
//        set_gitwebViewSettings(gitWebSettings);
//        githubWV.loadUrl(mygitURL);


        makeDB();

        urlstorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), FolderActivity.class);
                intent.putExtra("MyDBOpenHelper", (Parcelable) mDBOpenHelper);
                startActivity(intent);
            }
        });

        urlclipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), URLActivity.class);
                intent.putExtra("MyDBOpenHelper", (Parcelable) mDBOpenHelper);
                startActivity(intent);
            }
        });
    }
    private void makeDB(){
        if(mDBOpenHelper==null)
            mDBOpenHelper=DBOpenHelper.getInstance(this);
        mDBOpenHelper=mDBOpenHelper.open();
        mDBOpenHelper.create();
    }

    private void set_gitwebViewSettings(WebSettings gitWebSettings){
        gitWebSettings.setJavaScriptEnabled(true);//웹페이지 자바스크립트 허용
        gitWebSettings.setSupportMultipleWindows(false);//새창 띄우기 불가
        gitWebSettings.setJavaScriptCanOpenWindowsAutomatically(false);//자바스크립트 새창띄우기 불가
        gitWebSettings.setLoadWithOverviewMode(true);//메타태그 허용 여부
        gitWebSettings.setUseWideViewPort(false);//화면 사이즈 맞추기 허용 여부
        gitWebSettings.setSupportZoom(true);//화면 줌 허용 여부
        gitWebSettings.setBuiltInZoomControls(false);//화면 확대 축소 허용 여부
        gitWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//컨텐츠 사이즈 맞추기
        gitWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//브라우저 캐시 허용 여부
        gitWebSettings.setDomStorageEnabled(true);//로컬저장소 허용 여부
    }

}

