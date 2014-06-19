package com.wm.bookshare.model;

/**
 * Created by storm on 14-3-25.
 */
public enum Category {
    bestBook("好书"),bestSentence("好句"),BestInspiration("好感悟"), fresh("刷新");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
