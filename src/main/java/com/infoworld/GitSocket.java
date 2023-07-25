package com.infoworld;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import io.reactivex.disposables.Disposables;

public class GitSocket {

    public static void main(String[] args) {
        String eventStreamUrl = "wss://api.github.com/events"; // WebSocket URL

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(eventStreamUrl).build();

        Observable<String> observable = Observable.create(emitter -> {
            WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    // WebSocket connection is open
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    emitter.onNext(text); // Emit received message
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    emitter.onComplete(); // WebSocket connection is closed
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    emitter.onError(t); // WebSocket connection failure
                }
            });
            
            // Dispose WebSocket connection when the observer is disposed
            emitter.setDisposable(Disposables.fromRunnable(() -> webSocket.close(1000, "Closing WebSocket")));
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

        // Wait indefinitely (or use another mechanism to keep the program running)
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

