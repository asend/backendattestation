package com.fonctionpublique.enumpackage;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class Constantes {

    public static void sendNotificationWhatsapp(String message, String number) {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,

                "token=o5ev9jpddl8saakw&to=+"+number+"&body="+message+"&priority=1&referenceId=");

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance40778/messages/chat")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("############################## response"+response.body().string());

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
