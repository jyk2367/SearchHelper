package com.example.searchhelper;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FolderURLAdapter extends RecyclerView.Adapter<FolderURLAdapter.ViewHolder>{
    private ArrayList<URLData> myurllist;
    private DBOpenHelper mDBOpenHelper;
    private Context ctx;


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fd_urlnameTV,fd_urlTV,fd_addedDateTV;
        ImageButton fd_urldeleteBtn,fd_memoBtn;

        ViewHolder(View itemView){
            super(itemView);
            fd_urlnameTV=(TextView) itemView.findViewById(R.id.fd_urlnameTV);
            fd_urlTV=(TextView) itemView.findViewById(R.id.fd_urlTV);
            fd_addedDateTV=(TextView) itemView.findViewById(R.id.fd_addedDateTV);
            fd_urldeleteBtn=(ImageButton) itemView.findViewById(R.id.fd_urldeleteBtn);
            fd_memoBtn=(ImageButton) itemView.findViewById(R.id.fd_memoBtn);
        }

        void onBind(URLData urlData){
            fd_urlnameTV.setText(urlData.title);
            fd_urlTV.setText(urlData.url);
            fd_addedDateTV.setText(urlData.date);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fd_rv_item,parent,false);
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
        holder.fd_memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx,MemoActivity.class);
                intent.putExtra("myurllist",myurllist);
                intent.putExtra("selectedDataPos",position);
                intent.putExtra("MyDBOpenHelper",(Parcelable) mDBOpenHelper);
                startActivityForResult((Activity) holder.fd_memoBtn.getContext(),intent,101,null);

            }
        });
        holder.fd_urldeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c=mDBOpenHelper.mDB.rawQuery("SELECT * FROM "+DataBases.CreateDB._TABLENAME0+" WHERE url=\""+myurllist.get(position).url+"\"",null);
                if(c.moveToFirst()) {
                    Toast.makeText(ctx,myurllist.get(position).title+"이 삭제되었습니다!",Toast.LENGTH_SHORT).show();
                    mDBOpenHelper.deleteColumnbyURL(myurllist.get(position).url);
                    myurllist.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return myurllist.size();
    }

    public void setmyURLList(ArrayList<URLData> myurllist) {
        this.myurllist = myurllist;
    }
    public void setmyDBHelper(DBOpenHelper openHelper){
        this.mDBOpenHelper=openHelper;
    }
    public void setContext(Context ctx){
        this.ctx=ctx;
    }


}
