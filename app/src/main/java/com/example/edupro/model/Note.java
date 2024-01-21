package com.example.edupro.model;

import android.os.Parcelable;
import android.os.Parcel;
import java.util.Map;

public class Note implements Parcelable {
    String id;
    String user_id;
    String user_name;
    String category;
    Map<String, String> wordList;

    public Note()  {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }

    public Note(String id, String user_id, String category,String user_name, Map<String, String> wordList) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.category = category;
        this.wordList = wordList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getWordList() {
        return wordList;
    }

    public void setWordList(Map<String, String> wordList) {
        this.wordList = wordList;
    }


    protected Note(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        category = in.readString();
        // Assuming that your Map<String, String> is a non-null Map
        wordList = in.readHashMap(String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(category);
        // Assuming that your Map<String, String> is a non-null Map
        dest.writeMap(wordList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
