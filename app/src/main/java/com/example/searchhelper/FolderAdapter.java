package com.example.searchhelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
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

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{
    private ArrayList<String> fdnamelist;
    private DBOpenHelper mDBOpenHelper;
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton folderDeleteBtn,correctionBtn;
        TextView itemTV;

        ViewHolder(View itemView){
            super(itemView);
            folderDeleteBtn=(ImageButton) itemView.findViewById(R.id.folderDeleteBtn);
            correctionBtn=(ImageButton) itemView.findViewById(R.id.correctionBtn);
            itemTV=(TextView) itemView.findViewById(R.id.itemTV);
        }

        void onBind(String folderName){
            itemTV.setText(folderName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(fdnamelist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx,FolderURLActivity.class);
                intent.putExtra("MyDBOpenHelper",(Parcelable) mDBOpenHelper);
                intent.putExtra("tableName",fdnamelist.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        //table ?????? ??????
        holder.correctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(view.getContext());
                EditText tmpET=new EditText(view.getContext());
                dlg.setTitle("?????? ?????? ??????");
                dlg.setView(tmpET);
                dlg.setIcon(R.drawable.ic_addfolder);
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String lastfdName=fdnamelist.get(position);
                        String newfdName=tmpET.getText().toString();
                        if(newfdName.isEmpty()){
                            Toast.makeText(view.getContext(), "????????? ???????????? ???????????????!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!fdnamelist.contains(newfdName.toUpperCase())) {
                            Toast.makeText(view.getContext(), "????????? ?????? : "+lastfdName+ " -> "+newfdName, Toast.LENGTH_SHORT).show();
                            mDBOpenHelper.setTableName(newfdName);
                            mDBOpenHelper.mDB.execSQL("ALTER TABLE " + fdnamelist.get(position) + " RENAME TO " + DataBases.CreateDB._TABLENAME0);
                            fdnamelist.set(position,newfdName);
                            notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(view.getContext(), "????????? ?????? ?????? : ?????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });
        //table ??????
        holder.folderDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBOpenHelper.deleteTable(fdnamelist.get(position));
                fdnamelist.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fdnamelist.size();
    }

    public void setFdnamelist(ArrayList<String> fdnames){
        this.fdnamelist=fdnames;
    }
    public void setmyDBHelper(DBOpenHelper openHelper){
        this.mDBOpenHelper=openHelper;
    }
    public void setContext(Context ctx){
        this.ctx=ctx;
    }
}
