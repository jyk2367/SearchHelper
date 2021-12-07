package com.example.searchhelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import java.io.Serializable;

public final class DataBases implements Serializable,Parcelable {


    protected DataBases(Parcel in) {
    }

    public static final Creator<DataBases> CREATOR = new Creator<DataBases>() {
        @Override
        public DataBases createFromParcel(Parcel in) {
            return new DataBases(in);
        }

        @Override
        public DataBases[] newArray(int size) {
            return new DataBases[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static class CreateDB implements BaseColumns,Serializable,Parcelable {
        public static final String USERID = "userid";
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String DATE = "reportedDate";
        public static final String CONTENTS = "contents";


        public static String _TABLENAME0 = "URLClip";

        protected CreateDB(Parcel in) {
        }

        public static final Creator<CreateDB> CREATOR = new Creator<CreateDB>() {
            @Override
            public CreateDB createFromParcel(Parcel in) {
                return new CreateDB(in);
            }

            @Override
            public CreateDB[] newArray(int size) {
                return new CreateDB[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(USERID);
            parcel.writeString(TITLE);
            parcel.writeString(URL);
            parcel.writeString(DATE);
            parcel.writeString(CONTENTS);
            parcel.writeString(_TABLENAME0);

        }

        public static void setTableName(String tableName){
            _TABLENAME0=tableName;
        }

        public static String getCreate0(String tableName){

            return "create table if not exists "+tableName+" ("
                    +_ID+" integer primary key autoincrement, "//인덱스용
                    +USERID+" text not null, "
                    +TITLE+" text not null, "
                    +URL+" text unique not null, "
                    +DATE+" text not null, "
                    +CONTENTS+ " text not null);";
        }

        public static String getCreate0(){

            return "create table if not exists "+_TABLENAME0+" ("
                    +_ID+" integer primary key autoincrement, "//인덱스용
                    +USERID+" text not null, "
                    +TITLE+" text not null, "
                    +URL+" text unique not null, "
                    +DATE+" text not null, "
                    +CONTENTS+ " text not null);";
        }
    }
}
