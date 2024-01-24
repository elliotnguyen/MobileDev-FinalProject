package com.example.edupro.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class FirebaseRepository<T> {
    private final MutableLiveData<T> mData;
    private final MutableLiveData<ArrayList<T>> mListData;
    private final MutableLiveData<Boolean> statusHandling;
    private final DatabaseReference myRef;

    private FirebaseRepository(Class<T> dataType, String reference) {
        mData = new MutableLiveData<>();
        mListData = new MutableLiveData<>();
        statusHandling = new MutableLiveData<>(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(reference);
    }

    public static <T> FirebaseRepository<T> getInstance(Class<T> dataType, String reference) {
        return new FirebaseRepository<>(dataType, reference);
    }

    public void getAllData(ValueEventListener valueEventListener) {
        myRef.addValueEventListener(valueEventListener);
    }

    public void getAllDataOfChild(ArrayList<String> children, ValueEventListener valueEventListener) {
        DatabaseReference childRef = myRef;
        for (String child : children) {
            childRef = childRef.child(child);
        }
        childRef.addValueEventListener(valueEventListener);
    }

    public void getDataById(String id, ValueEventListener valueEventListener) {
        myRef.child(id).addValueEventListener(valueEventListener);
    }

    // create data object by list of child id
    // example: createDataById(["child1", "child2"], data)
    // -> create data object at /child1/child2
    public void createDataById(ArrayList<String> childrenId, T data, OnCompleteListener<Void> completionListener) {
        DatabaseReference childRef = myRef;
        for (String child : childrenId) {
            childRef = childRef.child(child);
        }

        childRef.setValue(data).addOnCompleteListener(completionListener);
    }

    // update attribute of object by list of child id
    // example: updateDataById(["user", "id", "name"],"nguyen tien thanh")
    // -> update name attribute of object at /user/id/name
    public <U> void updateDataOfChild(ArrayList<String> children, U data, OnCompleteListener<Void> completionListener) {
        DatabaseReference childRef = myRef;
        for (String child : children) {
            childRef = childRef.child(child);
        }
        childRef.setValue(data).addOnCompleteListener(completionListener);
    }

    public MutableLiveData<ArrayList<T>> getListData() {
        return mListData;
    }

    public MutableLiveData<T> getData() {
        return mData;
    }

    public MutableLiveData<Boolean> getStatusHandling() {
        return statusHandling;
    }

    public void uploadFileToStorage(File audioFile, String speakingId) {


    }
}
