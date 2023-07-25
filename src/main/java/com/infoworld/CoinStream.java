package com.infoworld;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CoinStream {

    public static void main(String[] args) {
        //String websocketUrl = "wss://ws-feed.prime.coinbase.com"; //
	String websocketUrl = "wss://ws.coincap.io/trades/binance";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(websocketUrl).build();

        Observable<String> observable = Observable.create(emitter -> {
            WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                    // WebSocket connection is open
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    emitter.onNext(text); // Emit received message
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    // Handle binary message if needed
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    webSocket.close(code, reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    emitter.onComplete(); // WebSocket connection is closed
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                    emitter.onError(t); // WebSocket connection failure
                }
            });

            // Dispose WebSocket connection when the observer is disposed
            emitter.setDisposable(Disposables.fromRunnable(() -> webSocket.close(1000, "Closing WebSocket")));
        });

        observable
                .subscribeOn(Schedulers.io())
.map(event -> {
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(event, JsonObject.class);
    return jsonObject;
  })
  .filter(jsonObject -> {
    String base = jsonObject.get("base").getAsString();
    return base.equals("solana");
  })
  .subscribe(
    jsonObject -> System.out.println(jsonObject),
    Throwable::printStackTrace,
    () -> System.out.println("Completed")
  );

		/*
		.map(event -> {
                   Gson gson = new Gson();
                   return gson.fromJson(event, Trade.class);
                })
                .filter(trade -> trade.base.equals("solana"))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // No-op
                    }

                    @Override
                    public void onNext(String event) {
                        // Process each event here
                        System.out.println("FOO: " + event);
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
*/
        // Wait indefinitely (or use another mechanism to keep the program running)
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


