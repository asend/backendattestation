package com.fonctionpublique.whatsapp;

import okhttp3.*;

import java.io.IOException;

public class Constantes {


    public static void sendDocumentByWhatsapp(String telephone, String document) throws IOException {
        OkHttpClient client = new OkHttpClient();


        RequestBody body = new FormBody.Builder()
                .add("token", "o5ev9jpddl8saakw")
                .add("to", telephone)
                .add("filename", "attestation_nafp.pdf")
                .add("document", document)
                .add("caption", "veuillez recevoir votre attestaton, vous pouvez toujours l'utiliser tant qu'elle est valable. ")


                .build();

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance40778/messages/document")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());


    }
    public static void sendDocumentRejetByWhatsapp(String telephone, String document) throws IOException {
        OkHttpClient client = new OkHttpClient();


        RequestBody body = new FormBody.Builder()
                .add("token", "o5ev9jpddl8saakw")
                .add("to", telephone)
                .add("filename", "attestation_nafp.pdf")
                .add("document", document)
                .add("caption", "Nous sommmes désolé, mais vous ne pouvez disposer de cet act. ")


                .build();

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance40778/messages/document")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());


    }


    public static void sendNotificationWhatsapp(String message, String number) {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType,

                "token=o5ev9jpddl8saakw&to=+" + number + "&body=" + message + "&priority=1&referenceId=");


        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance40778/messages/chat")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("############################## response" + response.body().string());

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
