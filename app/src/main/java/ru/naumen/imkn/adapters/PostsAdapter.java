package ru.naumen.imkn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.naumen.imkn.R;
import ru.naumen.imkn.model.Post;

/**
 * @author achernoprudov
 * @since 06 January 2017
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHolder> {

    class PostHolder extends RecyclerView.ViewHolder {

        PostHolder(View itemView) {
            super(itemView);
        }

        void bindPost(Post post) {
            TextView titleView = (TextView) itemView.findViewById(R.id.title);
            titleView.setText(post.getTitle());

            TextView textView = (TextView) itemView.findViewById(R.id.text);
            textView.setText(post.getText());

            TextView dateView = (TextView) itemView.findViewById(R.id.date);
            dateView.setText(post.getDate().toString());
        }
    }

    private List<Post> mPosts = new ArrayList<>();

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // создаем view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bindPost(post);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void addItems(Collection<Post> posts) {
        mPosts.addAll(posts);
    }

    /**
     * Отдельный метод из-за кнопки "показать еще"
     */
    public int getPostsCount() {
        return mPosts.size();
    }
}
