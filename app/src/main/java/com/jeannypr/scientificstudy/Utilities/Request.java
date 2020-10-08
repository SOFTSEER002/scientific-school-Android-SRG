package com.jeannypr.scientificstudy.Utilities;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
    public static String post(String serverUrl, String dataToSend, Context context, String schoolCode) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            con.setRequestMethod("POST");
            con.setRequestProperty("AuthKey", "123");
            con.setRequestProperty("DeviceId", "7878");
            con.setRequestProperty("SchoolCode", schoolCode);

            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            //make request
            writer.write(dataToSend);
            writer.flush();
            writer.close();

            //get response
            int connectionResCode = con.getResponseCode();
            if (connectionResCode == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                //loop through the response from the server
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                //return the response
                return builder.toString();

            } else {
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
                return "";
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
