package com.example.edupro.model;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {
    private String name;
    private String id;
    private String nickName;
    private String email;
    private Date lastAccessed;
    private int streakCount;
    private String phoneNumber;

    public User(String id, String email, String name) {
        this.email = email;
        this.name = name;
        this.nickName = name;
        this.lastAccessed = new Date();
        this.phoneNumber = phoneNumber;
        this.streakCount = 1;
        this.id = id;
    }

    public User(String id, String name, String nickName, String email, Date lastAccessed, int streakCount, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.lastAccessed = lastAccessed;
        this.streakCount = streakCount;
        this.phoneNumber = phoneNumber;
    }

    public User() {
        this.email = null;
        this.name = null;
        this.nickName = null;
        this.lastAccessed = null;
        this.phoneNumber = null;
        this.streakCount = 1;
        this.id = null;
    }

    public static User fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.child("id").getValue(String.class);
        String name = dataSnapshot.child("name").getValue(String.class);
        String nickName = dataSnapshot.child("nickName").getValue(String.class);
        String email = dataSnapshot.child("email").getValue(String.class);

        // Convert lastAccessed from String to Date
        String lastAccessedStr = dataSnapshot.child("lastAccessed").getValue(String.class);
        Date lastAccessed = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastAccessed = dateFormat.parse(lastAccessedStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int streakCount = dataSnapshot.child("streakCount").getValue(Integer.class);
        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);

        return new User(id, name, nickName, email, lastAccessed, streakCount, phoneNumber);
    }

    public void updateStreakIfLastAccessYesterday() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar today = Calendar.getInstance();
        Calendar lastAccessedCalendar = Calendar.getInstance();
        lastAccessedCalendar.setTime(lastAccessed);

        if (lastAccessedCalendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                lastAccessedCalendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            // Last access was yesterday, increment streak count
            streakCount++;
        } else if (lastAccessedCalendar.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
                lastAccessedCalendar.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
            streakCount = 1;
            lastAccessed = new Date();
        }

    }

    // Getter and Setter methods for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for 'id'
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter methods for 'nickName'
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    // Getter and Setter methods for 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter methods for 'lastAccessed'
    public Date getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    // Getter and Setter methods for 'streakCount'
    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    // Getter and Setter methods for 'phoneNumber'
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
