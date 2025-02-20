package com.example.sylasclient.News;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sylasclient.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = new ArrayList<News>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(news);
        recyclerView.setAdapter(newsAdapter);

        new FetchNewsTask().execute("http://10.0.2.2:4444/news");
    }

    private class FetchNewsTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected List<News> doInBackground(String... urls) {
            List<News> newsItems = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                InputStream inputStream = url.openConnection().getInputStream();

                // Мне нечем это комментировать
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);

                News currentItem = null;
                int eventType = parser.getEventType();

                // Я бы на вашем месте просто бы это не делал
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("item".equals(tagName)) {
                                currentItem = new News();
                            } else if (currentItem != null) {
                                if ("title".equals(tagName)) {
                                    currentItem.setTitle(parser.nextText());
                                } else if ("date".equals(tagName)) {
                                    currentItem.setDate(parser.nextText());
                                } else if ("description".equals(tagName)) {
                                    currentItem.setDescription(parser.nextText());
                                } else if ("image".equals(tagName)) {
                                    currentItem.setImageUrl(parser.nextText());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("item".equals(tagName)) {
                                newsItems.add(currentItem);
                            }
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return newsItems;
        }

        @Override
        protected void onPostExecute(List<News> newsItems) {
            Log.e("!", newsItems.toString());
            news.clear();
            news.addAll(newsItems);
            newsAdapter.notifyDataSetChanged();
        }
    }
}
