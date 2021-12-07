package com.example.searchhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FolderURLActivity extends AppCompatActivity{
    private RecyclerView folderURL_RV;
    private FolderURLAdapter folderURLAdapter;
    private DBOpenHelper mDBOpenHelper;
    private ArrayList<URLData> myurldatalist;
    private Context ctx;
    private EditText storedSearchET;
    private Button storedSearchBtn;
    private String tableName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folderurl);

        Intent intent=getIntent();
        mDBOpenHelper=(DBOpenHelper)intent.getSerializableExtra("MyDBOpenHelper");
        tableName=(String)intent.getStringExtra("tableName");
        if(mDBOpenHelper!=null)
            mDBOpenHelper.setTableName(tableName);


        folderURL_RV =(RecyclerView)findViewById(R.id.folderURL_RV);
        storedSearchET=(EditText)findViewById(R.id.storedSearchET);
        storedSearchBtn=(Button)findViewById(R.id.storedSearchBtn);
        folderURLAdapter =new FolderURLAdapter();
        ctx=getApplicationContext();


        getMyURLFromDB();


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        folderURL_RV.setLayoutManager(linearLayoutManager);
        folderURL_RV.setAdapter(folderURLAdapter);

        folderURLAdapter.setmyDBHelper(mDBOpenHelper);
        folderURLAdapter.setContext(ctx);

        storedSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp=storedSearchET.getText().toString();
                if(tmp.isEmpty()){
                    getMyURLFromDB();
                }
                else {
                    getMyURLFromDB(tmp);
                }
                folderURLAdapter.notifyDataSetChanged();
            }
        });

    }

    private void getMyURLFromDB(String keyword){
        myurldatalist=new ArrayList<>();
        Cursor iCursor=mDBOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            @SuppressLint("Range")String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            @SuppressLint("Range") String tempTitle = iCursor.getString(iCursor.getColumnIndex("title"));
            @SuppressLint("Range")String tempURL = iCursor.getString(iCursor.getColumnIndex("url"));
            @SuppressLint("Range") String tempREPORTDATE = iCursor.getString(iCursor.getColumnIndex("reportedDate"));
            @SuppressLint("Range") String tempCONTENTS=iCursor.getString(iCursor.getColumnIndex("contents"));
            URLData data=new URLData(tempID,tempTitle,tempURL,tempREPORTDATE,tempCONTENTS);
            if(tempID.equals("jyk2367")){
                //이미 myurldatalist에 값 존재하면 skip
                boolean state=true;
                if(!(data.getTitle().contains(keyword)))
                    state=false;
                for(URLData ud:myurldatalist){
                    if(ud.getUrl().equals(data.getUrl()))
                        state=false;
                }
                if(state)//중복이 없다면 추가
                    myurldatalist.add(data);
            }
        }
        folderURLAdapter.setmyURLList(myurldatalist);
        folderURLAdapter.notifyDataSetChanged();
    }

    private void getMyURLFromDB(){
        myurldatalist=new ArrayList<>();
        Cursor iCursor=mDBOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            @SuppressLint("Range")String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            @SuppressLint("Range") String tempTitle = iCursor.getString(iCursor.getColumnIndex("title"));
            @SuppressLint("Range")String tempURL = iCursor.getString(iCursor.getColumnIndex("url"));
            @SuppressLint("Range") String tempREPORTDATE = iCursor.getString(iCursor.getColumnIndex("reportedDate"));
            @SuppressLint("Range") String tempCONTENTS=iCursor.getString(iCursor.getColumnIndex("contents"));
            URLData data=new URLData(tempID,tempTitle,tempURL,tempREPORTDATE,tempCONTENTS);
            if(tempID.equals("jyk2367")){
                //이미 myurldatalist에 값 존재하면 skip
                boolean state=true;
                for(URLData ud:myurldatalist){
                    if(ud.getUrl().equals(data.getUrl()))
                        state=false;
                    
                }
                if(state)//중복이 없다면 추가
                    myurldatalist.add(data);
            }
        }
        folderURLAdapter.setmyURLList(myurldatalist);
        folderURLAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode!=RESULT_CANCELED){
            String tmp=data.getStringExtra("sendData");
            int selectedDataPos=data.getIntExtra("selectedDataPos",0);
            myurldatalist.get(selectedDataPos).contents=tmp;
            folderURLAdapter.notifyDataSetChanged();
        }
    }
}
