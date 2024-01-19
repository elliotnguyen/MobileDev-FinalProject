package com.example.edupro.model;

import java.util.ArrayList;

public class SkillDto {
    private final String id;
    private final ArrayList<Long> type;
    private final long topic;

    public SkillDto() {
        id = "";
        type = new ArrayList<>();
        topic = 0;
    }

    public SkillDto(String id, ArrayList<Long> type, long topic) {
        this.id = id;
        this.type = type;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Long> getType() {
        return type;
    }

    public long getTopic() {
        return topic;
    }
}
