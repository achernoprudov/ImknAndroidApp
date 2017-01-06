package ru.naumen.imkn.task;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import ru.naumen.imkn.model.Post;
import ru.naumen.imkn.utils.Constants;
import ru.naumen.imkn.utils.ParseUtils;

/**
 * Данный {@link AsyncTask} занимается получением и парсингом постов.
 *
 * @author achernoprudov
 * @since 06 January 2017
 */
public class GetPostsTask extends AsyncTask<Void, Void, Collection<Post>> {

    private static final String REST_ADDRESS_FORMAT = "%s/rest/posts/?first_result=%d%n";

    private final long mFirstResult;

    public GetPostsTask(int firstResult) {
        mFirstResult = firstResult;
    }

    @Override
    protected Collection<Post> doInBackground(Void... voids) {
        try {
            // формируем адрес до REST API метода
            String restAddress = String.format(REST_ADDRESS_FORMAT, Constants.HOST_ADDRESS, mFirstResult);
            URL url = new URL(restAddress);

            // создаем GET соединение
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // преобразовывем поток в строку с помощью StringBuilder и BufferedReader
            InputStream inputStream = urlConnection.getInputStream();
            String resultJson = ParseUtils.readJsonFromInputStream(inputStream);
            return ParseUtils.parsePosts(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
