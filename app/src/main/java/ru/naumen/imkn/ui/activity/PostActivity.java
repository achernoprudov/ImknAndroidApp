package ru.naumen.imkn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ru.naumen.imkn.R;

/**
 * Активити просмотра публикации.
 */
public class PostActivity extends AppCompatActivity {

    public static final String POST_ID = "postId";

    public static void startActivity(Context context, long postId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_ID, postId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
//        setupToolbar();
        setupCommentsButton();
    }

    private void setupCommentsButton() {
        Button commentsButton = (Button) findViewById(R.id.comments);
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
