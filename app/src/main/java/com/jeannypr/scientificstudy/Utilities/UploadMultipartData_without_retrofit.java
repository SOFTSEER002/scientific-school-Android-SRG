package com.jeannypr.scientificstudy.Utilities;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class UploadMultipartData_without_retrofit {
    public static String post(String fileName, String fileUrl, String serverUrl, Context context, String schoolCode) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            String boundaryString = "----SomeRandomText";
            File logFileToUpload = new File(fileUrl);

            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
            con.setRequestProperty("AuthKey", "123");
            con.setRequestProperty("DeviceId", "7878");
            con.setRequestProperty("SchoolCode", schoolCode);

            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            //make request
            // Include value from the myFileDescription text area in the post data
            writer.write("\n\n--" + boundaryString + "\n");
            writer.write("Content-Disposition: form-data; name=\"myFileDescription\"");
            writer.write("\n\n");
            writer.write("Log file for 20150208");

            // Include the section to describe the file
            writer.write("\n--" + boundaryString + "\n");
            writer.write("Content-Disposition: form-data;"
                    + "name=\"myFile\";"
                    + "filename=\"" + fileName + "\""
                    + "\nContent-Type: text/plain\n\n");
            writer.flush();


            // Write the actual file contents
            FileInputStream inputStreamToLogFile = new FileInputStream(logFileToUpload);

            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while ((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
                os.write(dataBuffer, 0, bytesRead);
            }

            os.flush();

// Mark the end of the multipart http request
            writer.write("\n--" + boundaryString + "--\n");
            writer.flush();

// Close the streams
            os.close();
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
