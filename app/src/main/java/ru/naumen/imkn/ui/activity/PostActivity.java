package ru.naumen.imkn.ui.activity;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.naumen.imkn.R;
import ru.naumen.imkn.model.Post;
import ru.naumen.imkn.utils.Constants;
import ru.naumen.imkn.utils.ParseUtils;

/**
 * Активити просмотра публикации.
 */
public class PostActivity extends AppCompatActivity {

    /**
     * Данный {@link AsyncTask} занимается получением конкретного поста.
     */
    public class GetPostTask extends AsyncTask<Void, Void, Post> {

        private static final String REST_ADDRESS_FORMAT = "%s/rest/posts/%d%n";
        private final long mPostId;

        public GetPostTask(long postId) {
            mPostId = postId;
        }

        /**
         * Действия, которые происходят в бэкграунд потоке приложения.
         */
        @Override
        protected Post doInBackground(Void... voids) {
            try {
                // формируем адрес до REST API метода
                String restAddress = String.format(REST_ADDRESS_FORMAT, Constants.HOST_ADDRESS, mPostId);
                URL url = new URL(restAddress);
                Log.i(TAG, "Request address: " + restAddress);

                // создаем GET соединение
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // преобразовывем поток в строку с помощью StringBuilder и BufferedReader
                InputStream inputStream = urlConnection.getInputStream();
                String resultJson = ParseUtils.readJsonFromInputStream(inputStream);
                Log.i(TAG, "Fetched json" + resultJson);
                JSONObject jsonObject = new JSONObject(resultJson);
                return ParseUtils.parsePost(jsonObject);
            } catch (final Exception throwable) {
                Log.e(TAG, "Error while fetching data", throwable);

                // выполняем показ окна с ошибкой в UI потоке, иначе приложение упадет
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // покаызваем snackbar с ошибкой
                        Snackbar.make(mSwipeRefreshLayout, throwable.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadPost();
                                    }
                                }).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
            // выполняется в UI потоке, поэтому мы можем спокойно работать с UI
            updateWithPost(post);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private static final String TAG = "PostActivity";
    private static final String POST_ID = "postId";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private long mPostId;

    public static void startActivity(Context context, long postId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_ID, postId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        extractExtras();

        setupCommentsButton();
        setupReloadSwipe();

        loadPost();
    }

    /**
     * Обновляет UI данными из полученного поста
     */
    private void updateWithPost(Post post) {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(post.getTitle());

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(post.getText());

        TextView date = (TextView) findViewById(R.id.date);
        String stringDate = ParseUtils.dateToString(post.getDate());
        date.setText(stringDate);
    }

    private void extractExtras() {
        mPostId = getIntent().getExtras().getLong(POST_ID);
    }

    private void setupReloadSwipe() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPost();
            }
        });
    }

    private void loadPost() {
        new GetPostTask(mPostId).execute();
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
}
