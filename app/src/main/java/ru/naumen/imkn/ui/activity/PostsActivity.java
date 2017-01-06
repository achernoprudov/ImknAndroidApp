package ru.naumen.imkn.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import ru.naumen.imkn.R;
import ru.naumen.imkn.adapters.PostsAdapter;
import ru.naumen.imkn.model.Post;
import ru.naumen.imkn.utils.Constants;
import ru.naumen.imkn.utils.ParseUtils;

/**
 * Аткивити списка постов
 */
public class PostsActivity extends AppCompatActivity {

    /**
     * Данный {@link AsyncTask} занимается получением и парсингом постов.
     */
    public class GetPostsTask extends AsyncTask<Void, Void, Collection<Post>> {

        private static final String REST_ADDRESS_FORMAT = "%s/rest/posts/?first_result=%d%n";

        private final long mFirstResult;

        GetPostsTask(int firstResult) {
            mFirstResult = firstResult;
        }

        /**
         * Действия, которые происходят в бэкграунд потоке приложения.
         */
        @Override
        protected Collection<Post> doInBackground(Void... voids) {
            try {
                // формируем адрес до REST API метода
                String restAddress = String.format(REST_ADDRESS_FORMAT, Constants.HOST_ADDRESS, mFirstResult);
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
                return ParseUtils.parsePosts(resultJson);
            } catch (final Exception throwable) {
                Log.e(TAG, "Error while fetching data", throwable);

                // выполняем показ окна с ошибкой в UI потоке, иначе приложение упадет
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // покаызваем snackbar с ошибкой
                        Snackbar.make(mFloatingActionButton, throwable.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadPosts();
                                    }
                                }).show();
                    }
                });
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(Collection<Post> posts) {
            super.onPostExecute(posts);
            // выполняется в UI потоке, поэтому мы можем спокойно работать с UI
            mPostsAdapter.addItems(posts);
            mPostsAdapter.notifyDataSetChanged();
        }
    }

    private static final String TAG = "PostsActivity";

    private PostsAdapter mPostsAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        addCreatePostButton();
        setupToolbar();
        setupRecyclerView();

        loadPosts();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadPosts() {
        int firstResult = mPostsAdapter.getPostsCount();
        new GetPostsTask(firstResult).execute();
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.posts);

        // RecyclerView требует как адаптер, так и LayoutManager
        mPostsAdapter = new PostsAdapter();
        mRecyclerView.setAdapter(mPostsAdapter);

        // LayoutManager отвечает за расположение элементов в списке
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // добавление разделителя элементов
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Добавление кнопки создания поста
     */
    private void addCreatePostButton() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_post);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
