package ru.naumen.imkn.model;

import java.util.Date;

/**
 * @author achernoprudov
 * @since 06 January 2017
 */

public final class Post {

    private final long mId;
    private final String mTitle;
    private final String mText;
    private final Date mDate;

    public Post(long id, String text, String title, Date date) {
        mDate = date;
        mText = text;
        mTitle = title;
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }
}
