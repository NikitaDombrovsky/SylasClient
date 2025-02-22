package com.example.retrofit;

public class Test {
    /*
    private class NetworkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            // Perform network operation here
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


            List<Event> events;
            // Создаем объект Gson
            Gson gson = new Gson();

            Gson gson1 = new GsonBuilder().create();
            EventResponce eventResponce = gson1.fromJson(result, EventResponce.class);
            //events = gson1.fromJson(result, EventResponce.class);

            Gson gson1 = new GsonBuilder().create();
            events = gson1.fromJson(result, Event.class);

            // Указываем тип данных для парсинга (список объектов Event)
            Type eventListType = new TypeToken<List<Event>>() {}.getType();
            // Преобразуем JSON в список объектов
            List<Event> events = gson.fromJson(result, eventListType);
            // Выводим список для проверки
            for (Event event : events) {
                Log.e("!", event.toString());
                System.out.println(event);
            }

            // Update UI with the result
            if (result != null) {
                TextView textView = findViewById(R.id.textView);
                textView.setText(result);
            }
}
    }

     */
}
