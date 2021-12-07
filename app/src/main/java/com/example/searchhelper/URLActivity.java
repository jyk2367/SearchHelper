package com.example.searchhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLActivity extends AppCompatActivity {
    private RecyclerView URLRV;
    private URLAdapter urlAdapter;
    private ArrayList<URLData> myurldatalist;
    private List<String> links,titles;
    private DBOpenHelper mDBOpenHelper;
    private Context ctx;

    private EditText searchET;
    private Button searchBtn,nextBtn,lastBtn;
    private String searchKeyword="코딩테스트";
    private int recN=20,Idx=1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clippingurl);

        Intent intent=getIntent();
        mDBOpenHelper=(DBOpenHelper)intent.getSerializableExtra("MyDBOpenHelper");
        searchBtn=(Button)findViewById(R.id.URLsearchBtn);
        lastBtn=(Button)findViewById(R.id.lastBtn);
        searchET=(EditText)findViewById(R.id.URLsearchET);
        nextBtn=(Button)findViewById(R.id.nextBtn);
        URLRV =(RecyclerView) findViewById(R.id.URLRV);
        urlAdapter=new URLAdapter();
        myurldatalist=new ArrayList<URLData>();
        ctx=getApplicationContext();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        URLRV.setLayoutManager(linearLayoutManager);
        URLRV.setAdapter(urlAdapter);
        urlAdapter.setMyDBOpenHelper(mDBOpenHelper);
        urlAdapter.setMyurllist(myurldatalist);
        urlAdapter.setContext(ctx);
        
        //디폴트
        showSearchResults();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchKeyword= searchET.getText().toString();
                Idx=1;
                showSearchResults();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Idx++;
                showSearchResults();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Idx>1)
                    Idx--;
                showSearchResults();
            }
        });

    }

    private void showSearchResults(){
        new Thread() {
            @Override
            public void run() {
                try {
                    //decode : 쓰레드 필요
                    myurldatalist=new ArrayList<>();
                    String query = URLDecoder.decode("http://" + "www.google.com/search?q=" + searchKeyword + "&num="+recN+"&start="+(Idx*recN), "UTF-8");
                    String page = getSearchContent(query);
                    links = urlLinks(page);
                    titles= titleLinks(page);
                    if(links!=null&&titles!=null) {
                        if (links.size() > 0 && titles.size() > 0 && links.size() == titles.size()) {
                            for (int i = 0; i < links.size(); i++) {
                                myurldatalist.add(new URLData("jyk2367",titles.get(i),links.get(i),"",""));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!myurldatalist.isEmpty())
                            urlAdapter.setMyurllist(myurldatalist);
                        else
                            Idx--;
                        urlAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }


    public String getSearchContent(String googleSearchQuery) throws Exception {
        googleSearchQuery = URLEncoder.encode(googleSearchQuery, StandardCharsets.UTF_8.toString());
        final String agent = "mozilla/5.0 (compatible googlebot/2.1 http //www.google.com/bot.html)";
        URL url = new URL(URLDecoder.decode(googleSearchQuery,"UTF-8"));

        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return getString(stream);
    }

    public String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public List<String> urlLinks(final String html) throws Exception {
        List<String> result = new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Elements results = doc.select("a > h3");
        boolean state=false;
        for (Element link : results) {
            Elements parent = link.parent().getAllElements();
            String relHref = parent.attr("href");
            if(state==false) {
//                Log.d("text", parent.html());
                state=true;
            }
//            Log.d("href",relHref);
            if (relHref.startsWith("/url?q=")) {
                relHref = relHref.replace("/url?q=", "");
            }
//            Log.d("href",relHref);
            String[] splittedString = relHref.split("&sa=");
            if (splittedString.length > 1) {
                relHref = splittedString[0];
            }
//            Log.d("urltest",relHref);
            result.add(relHref);
        }
        return result;
    }

    public List<String> titleLinks(final String html) throws Exception {
        List<String> result = new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Elements results = doc.select("a > h3");
        boolean state=false;
        for (Element link : results) {
            Elements parent = link.parent().getAllElements();
            List<String> titlelist= parent.eachText();
            if(titlelist.size()>1) {
                result.add(titlelist.get(1));
//                Log.d("test",titlelist.get(1));
            }
        }
        return result;
    }

}
