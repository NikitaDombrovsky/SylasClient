package com.example.okhttp;

public class utils {
    private utils(){
    };

  //  public static final String API_URL = "http://169.254.35.189:8080/demo/";
    public static final String API_URL = "http://10.0.2.2:4444/";

    public static EventService getEventService(){
        return RetrofitClient.getClient(API_URL).create(EventService.class);
    }
}
