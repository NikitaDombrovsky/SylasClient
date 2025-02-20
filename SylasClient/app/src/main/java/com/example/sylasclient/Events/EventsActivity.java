package com.example.sylasclient.Events;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sylasclient.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class EventsActivity extends AppCompatActivity {

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

        // TODO 10.0.2.2 потому что локалхост родительской машины
        new FetchEventsTask().execute("http://10.0.2.2:4444/events");
    }

    private class FetchEventsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                // Или просто
                // URL url1 = new URL("http://10.0.2.2:4444/events");
                urlConnection = (HttpURLConnection) url.openConnection();

                // Это просто придется запомнить
                // TODO Наведи на HttpURLConnection и листни вниз документацию
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream())
                );
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Разбить результат на элементы по ключам
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Event event = new Event();
                    event.setTitle(jsonObject.getString("title"));
                    event.setDate(jsonObject.getString("date"));
                    event.setDescription(jsonObject.getString("description"));
                    event.setAuthor(jsonObject.getString("author"));
                    events.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}