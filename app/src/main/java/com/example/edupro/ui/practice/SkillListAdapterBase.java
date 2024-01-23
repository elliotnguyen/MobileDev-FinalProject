package com.example.edupro.ui.practice;

import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;

public class SkillListAdapterBase {
    public final RecyclerViewClickInterface recyclerViewClickInterface;
    public final int resourceId;
    public final int titleId;
    public final int progressId;
    public final int actionId;
    public final int progressBarId;

    public SkillListAdapterBase(RecyclerViewClickInterface recyclerViewClickInterface,
                                int resourceId, int titleId, int progressId, int actionId, int progressBarId) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.resourceId = resourceId;
        this.titleId = titleId;
        this.progressId = progressId;
        this.actionId = actionId;
        this.progressBarId = progressBarId;
    }
}
