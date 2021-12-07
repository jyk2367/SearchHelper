package com.example.searchhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {
    private RecyclerView folder_RV;
    private ImageButton folderAddBtn;

    private FolderAdapter folderAdapter;
    private DBOpenHelper mDBOpenHelper;
    private Context ctx;

    private ArrayList<String> fdnamelist;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Intent intent=getIntent();
        mDBOpenHelper=(DBOpenHelper)intent.getSerializableExtra("MyDBOpenHelper");
        folderAdapter=new FolderAdapter();
        ctx=getBaseContext();

        folder_RV=(RecyclerView) findViewById(R.id.folder_RV);
        folderAddBtn=(ImageButton) findViewById(R.id.folderAddBtn);

        fdnamelist=mDBOpenHelper.getTableNameList();


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        folder_RV.setLayoutManager(linearLayoutManager);
        folder_RV.setAdapter(folderAdapter);

        folderAdapter.setFdnamelist(fdnamelist);
        folderAdapter.setmyDBHelper(mDBOpenHelper);
        folderAdapter.setContext(ctx);

        folderAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                EditText tmpET=new EditText(view.getContext());
                dlg.setTitle("추가할 파일명을 입력해주세요");
                dlg.setView(tmpET);
                dlg.setIcon(R.drawable.ic_addfolder);
                dlg.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tmpS=tmpET.getText().toString();
                        if(tmpS.isEmpty()){
                            tmpS="URLClip";
                        }
                        if(!fdnamelist.contains(tmpS.toUpperCase())) {
                            Toast.makeText(view.getContext(), tmpS + " 폴더를 추가했습니다!", Toast.LENGTH_SHORT).show();
                            fdnamelist.add(tmpS);
                            folderAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(view.getContext(), tmpS+" 폴더는 이미 있습니다!", Toast.LENGTH_SHORT).show();
                        mDBOpenHelper.mDB.execSQL(DataBases.CreateDB.getCreate0(tmpS));
                        mDBOpenHelper.setTableName(tmpS);
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });



    }

}
