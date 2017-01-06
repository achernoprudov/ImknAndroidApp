package ru.naumen.imkn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.naumen.imkn.R;
import ru.naumen.imkn.model.Post;
import ru.naumen.imkn.ui.activity.PostActivity;
import ru.naumen.imkn.ui.holder.PostHolder;

/**
 * @author achernoprudov
 * @since 06 January 2017
 */
public class PostsAdapter extends RecyclerView.Adapter<PostHolder> {

    private List<Post> mPosts = new ArrayList<>();

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // создаем view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        final Post post = mPosts.get(position);
        holder.bindPost(post);

        // хэндлер для перехода на активити по клику
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostActivity.startActivity(view.getContext(), post.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void addItems(Collection<Post> posts) {
        mPosts.addAll(posts);
    }

    public void clear() {
        mPosts.clear();
    }
}
