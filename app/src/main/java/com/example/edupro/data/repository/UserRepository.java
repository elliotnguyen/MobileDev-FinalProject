package com.example.edupro.data.repository;

import android.util.Log;

import com.example.edupro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class UserRepository {
    private DatabaseReference databaseReference;

    public UserRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void addUser(User user) {
        databaseReference.child(user.getId()).setValue(user);
    }

    public void updateUser(User user) {
        // Update the user in the database based on user's ID
        databaseReference.child(user.getId()).setValue(user);
    }

    public void deleteUser(String userId) {
        // Delete the user from the database based on user's ID
        databaseReference.child(userId).removeValue();
    }

    public void getUser(String userId, final OnUserFetchedListener listener) {
        // Retrieve a user from the database based on user's ID
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    listener.onUserFetched(user);
                } else {
                    // Handle the case where the user with the specified ID is not found
                    listener.onError(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                listener.onError(databaseError.toException());
            }
        });
    }

    public void updateStreakCount(String userId, int newStreakCount) {
        // Update the streak count for the user in the database based on user's ID
        databaseReference.child(userId).child("streak_count").setValue(newStreakCount);
    }

    // Interface for callback when user is fetched
    public interface OnUserFetchedListener {
        void onUserFetched(User user);

        void onError(Exception e);
    }
}
