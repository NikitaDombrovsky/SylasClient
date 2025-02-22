package com.example.okhttp.Event;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okhttp.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventsAdapter eventAdapter;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        events = new ArrayList<Event>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventsAdapter(events);
        recyclerView.setAdapter(eventAdapter);

        new FetchEventsTask().execute();

    }

    private class FetchEventsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:4444/events")
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
            try {
                // Можно упростить дело с помощью библиотек
                // GSON
                events.addAll(toList_GSON(result));
                // MOCHI
                //events.addAll(toList_Mochi(result));
                // Jackson
                //events.addAll(toList_Jackson(result));
                // Или без
                //events.addAll(toList(result));
                eventAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private List<Event> toList_GSON(String result) {
        try {
            Gson gson = new Gson();
            // Это придется просто запомнить
            Type eventListType = new TypeToken<ArrayList<Event>>() {
            }.getType();
            return gson.fromJson(result, eventListType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Event> toList_Mochi(String result) {
        try {
            // Вариант на Mochi
            Moshi moshi = new Moshi.Builder()
                    .build();
            // Тут хотя бы понятно
            Type type = Types.newParameterizedType(List.class, Event.class);
            // Mochi не очень хорошо работает с ArrayList но они с List заменяемы
            JsonAdapter<List<Event>> jsonAdapter = moshi.adapter(type);
            return jsonAdapter.fromJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Event> toList(String result) {
        try {
            List<Event> newEvents = new ArrayList<>();
            // Разбить результат на элементы по ключам
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Event event = new Event();
                event.setTitle(jsonObject.getString("title"));
                event.setDate(jsonObject.getString("date"));
                event.setDescription(jsonObject.getString("description"));
                event.setAuthor(jsonObject.getString("author"));
                newEvents.add(event);
            }
            return newEvents;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Event> toList_Jackson(String result) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Парсинг JSON в список объектов Event
            List<Event> newEvents = objectMapper.readValue(result, new TypeReference<List<Event>>() {
            });
            return newEvents;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}