package ru.naumen.imkn.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.naumen.imkn.R;
import ru.naumen.imkn.model.Post;
import ru.naumen.imkn.utils.ParseUtils;

/**
 * Холдер для отображения данных поста
 */
public class PostHolder extends RecyclerView.ViewHolder {

    private final TextView mTitle;
    private final TextView mText;
    private final TextView mDate;

    public PostHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(R.id.title);
        mText = (TextView) itemView.findViewById(R.id.text);
        mDate = (TextView) itemView.findViewById(R.id.date);
    }

    public void bindPost(Post post) {
        mTitle.setText(post.getTitle());
        mText.setText(post.getText());

        String stringDate = ParseUtils.dateToString(post.getDate());
        mDate.setText(stringDate);
    }
}