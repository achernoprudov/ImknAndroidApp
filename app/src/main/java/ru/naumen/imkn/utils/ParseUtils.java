package ru.naumen.imkn.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ru.naumen.imkn.model.Post;

/**
 * @author achernoprudov
 * @since 06 January 2017
 */

public final class ParseUtils {

    private static final String TAG = "ParseUtils";

    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

    public static String dateToString(Date date) {
        return mSimpleDateFormat.format(date);
    }

    /**
     * @param inputStream поток с сервера
     * @return raw json
     */
    @NonNull
    public static String readJsonFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }


    public static Post parsePost(JSONObject jsonObject) throws JSONException {
        final long id = jsonObject.getLong("id");
        final String title = jsonObject.getString("title");
        final String text = jsonObject.getString("text");
        final long dateMills = jsonObject.getLong("date");
        final Date date = new Date(dateMills);

        return new Post(id, text, title, date);
    }

    /**
     * Парсинг JSON и преобразование в список постов {@link Collection <Post>}
     *
     * @param jsonString полученная строка
     * @return список постов
     * @throws JSONException если в результате парсинга возникла ошибка
     */
    public static Collection<Post> parsePosts(String jsonString) throws JSONException {
        JSONArray postsJsonArray = new JSONArray(jsonString);

        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < postsJsonArray.length(); i++) {
            JSONObject jsonObject = postsJsonArray.getJSONObject(i);

            // парсинг объекта массива
            Post post = ParseUtils.parsePost(jsonObject);
            posts.add(post);
        }
        return posts;
    }
}
