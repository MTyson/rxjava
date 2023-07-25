package com.infoworld;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitStream {

    public static void main(String[] args) {
        String eventStreamUrl = "https://api.github.com/events";

        Observable<String> observable = Observable.create(emitter -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(eventStreamUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null && !emitter.isDisposed()) {
                    emitter.onNext(line);
                }
            } catch (IOException e) {
                emitter.onError(e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }

            emitter.onComplete();
        });

        observable
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // No-op
                    }

                    @Override
                    public void onNext(String event) {
                        // Process each event here
                        System.out.println(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Completed");
                    }
                });

        // Wait for the events to be retrieved
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

