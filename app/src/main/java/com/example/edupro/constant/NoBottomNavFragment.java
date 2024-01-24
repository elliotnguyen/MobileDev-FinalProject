package com.example.edupro.constant;

import com.example.edupro.R;

import java.util.ArrayList;
import java.util.Arrays;

public class NoBottomNavFragment {
    private static final ArrayList<Integer> hidingBottomBarFragmentIds = new ArrayList<>(
            Arrays.asList(
                    R.id.navigation_practice_reading,
                    R.id.navigation_practice_reading_practice,
                    R.id.navigation_practice_writing_practice,
                    R.id.navigation_practice_reading_result,
                    R.id.navigation_practice_listening,
                    R.id.navigation_practice_listening_practice,
                    R.id.navigation_practice_speaking,
                    R.id.navigation_practice_speaking_practice,
                    R.id.navigation_chat,
                    R.id.navigation_note_detail,
                    R.id.navigation_practice_writing_result_detail,
                    R.id.navigation_practice_writing
            )
    );

    public ArrayList<Integer> getHidingBottomBarFragmentIds() {
        return hidingBottomBarFragmentIds;
    }

    public static boolean shouldHideBottomBar(int id) {
        return hidingBottomBarFragmentIds.contains(id);
    }
}
