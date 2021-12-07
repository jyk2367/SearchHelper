package com.example.searchhelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class URLAdapter extends RecyclerView.Adapter<URLAdapter.ViewHolder> {
    private ArrayList<URLData> myurllist;
    private DBOpenHelper mDBOpenHelper;
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView urlnameTV,urlTV,addedDateTV;
        ImageButton URLAddBtn;
        String tmpS;


        ViewHolder(View itemView){
            super(itemView);
            urlnameTV=(TextView) itemView.findViewById(R.id.urlnameTV);
            urlTV=(TextView) itemView.findViewById(R.id.urlTV);
            addedDateTV=(TextView) itemView.findViewById(R.id.addedDateTV);
            URLAddBtn=(ImageButton) itemView.findViewById(R.id.URLAddBtn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(myurllist.get(pos).url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }
                }
            });
        }

        void onBind(URLData urlData){

            urlnameTV.setText(urlData.title);
            urlTV.setText(urlData.url);
            addedDateTV.setText(urlData.date);
            URLAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> al=mDBOpenHelper.getTableNameList();
                    String[] tableNamelist=al.toArray(new String[al.size()]);
                    EditText tmpET=new EditText(view.getContext());
                    AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());

                    dlg.setTitle("저장할 파일명을 입력해주세요\n(디폴트 : URLClip)");
                    dlg.setView(tmpET);
                    dlg.setIcon(R.drawable.ic_addfolder);

                    //기존에 있던 파일들
                    dlg.setItems(tableNamelist, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tmpS=tableNamelist[i];
                            Toast.makeText(view.getContext(), tmpS+" 폴더에 넣었습니다!!", Toast.LENGTH_SHORT).show();
                            mDBOpenHelper.setTableName(tmpS);
                            mDBOpenHelper.insertColumn(urlData.userid,urlData.title, urlData.url,"","");
                        }
                    });
                    
                    //새 파일 생성
                    dlg.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tmpS=tmpET.getText().toString();
                            if(tmpS.isEmpty()){
                                tmpS="URLClip";
                            }
                            Toast.makeText(view.getContext(), tmpS+" 폴더에 넣었습니다!!", Toast.LENGTH_SHORT).show();
                            mDBOpenHelper.mDB.execSQL(DataBases.CreateDB.getCreate0(tmpS));
                            mDBOpenHelper.setTableName(tmpS);
                            mDBOpenHelper.insertColumn(urlData.userid,urlData.title, urlData.url,"","");
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.url_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(myurllist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(myurllist.get(position).url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myurllist.size();
    }

    public void addItem(URLData data){
        myurllist.add(data);
        notifyDataSetChanged();
    }

    public void setMyDBOpenHelper(DBOpenHelper mDBOpenHelper){
        this.mDBOpenHelper=mDBOpenHelper;
    }

    public void setMyurllist(ArrayList<URLData> myurllist) {
        this.myurllist = myurllist;
    }

    public void setContext(Context ctx){
        this.ctx=ctx;
    }
}
