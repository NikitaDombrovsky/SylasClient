package com.example.okhttp.News;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okhttp.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(news);
        recyclerView.setAdapter(newsAdapter);

        new FetchNewsTask().execute();
    }

    private class FetchNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:4444/news")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            news.addAll(toList_DOM(result));
            newsAdapter.notifyDataSetChanged();
        }
    }

    public List<News> toList_DOM(String result) {
        try {
            // Создаем DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Парсим XML
            ByteArrayInputStream input = new ByteArrayInputStream(result.getBytes());
            Document document = builder.parse(input);

            // Получаем корневой элемент
            Element root = document.getDocumentElement();

            // Получаем все элементы <item>
            NodeList itemNodes = root.getElementsByTagName("item");

            List<News> newNewsList = new ArrayList<>();
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    News newNews = new News();
                    newNews.setTitle(itemElement.getElementsByTagName("title").item(0).getTextContent());
                    newNews.setDate(itemElement.getElementsByTagName("date").item(0).getTextContent());
                    newNews.setDescription(itemElement.getElementsByTagName("description").item(0).getTextContent());
                    newNews.setImageUrl(itemElement.getElementsByTagName("image").item(0).getTextContent());
                    newNewsList.add(newNews);
                }
            }
            return newNewsList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
