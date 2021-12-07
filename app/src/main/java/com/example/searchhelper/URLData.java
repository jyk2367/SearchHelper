package com.example.searchhelper;

import java.io.Serializable;

public class URLData implements Serializable {
    String userid;
    String title;
    String url;
    String date;
    String contents;

    public URLData(String tempID, String tempTitle, String tempURL, String tempREPORTDATE,String tempCONTENTS) {
        this.userid=tempID;
        this.title=tempTitle;
        this.url=tempURL;
        this.date=tempREPORTDATE;
        this.contents=tempCONTENTS;
    }

    public String getUserid(){
        return this.userid;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getContents() {
        return contents;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setContents(String contents) {
        this.contents=contents;
    }

}
