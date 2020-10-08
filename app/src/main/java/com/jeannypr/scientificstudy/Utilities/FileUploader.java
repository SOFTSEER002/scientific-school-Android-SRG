package com.jeannypr.scientificstudy.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FileUploader {

    public void UploadFile(String filePath, Bitmap img, String fileName, String schoolCode, Context context, String serverUrl, UploadListner uploadListnerl) {
        new UploadFileTask(filePath,img, fileName,schoolCode, context, serverUrl, uploadListnerl).execute();
    }

    private class UploadFileTask extends AsyncTask<Void, Integer, String> {
        Bitmap img;
        String fileName;
        String serverUrl;
        Context context;
        UploadListner listner;
        String schoolCode;
       String filePath;

        @Override
        protected void onPreExecute() {
            listner.onUploadStart();
        }

        @Override
        protected void onPostExecute(String response) {
            //listner.onUploadComplete(response);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
        }

        public UploadFileTask(String filePath, Bitmap bmpImg, String fileName, String schoolCode, Context context, String serverUrl, UploadListner listner) {
            this.img = bmpImg;
            this.fileName = fileName;
            this.context = context;
            this.serverUrl = serverUrl;
            this.listner = listner;
            this.schoolCode=schoolCode;
            this.filePath=filePath;
        }

        @Override
        protected String doInBackground(Void... strings) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String encodedImg = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            //generate hashMap to store encodedImage and the name
            HashMap<String, String> detail = new HashMap<>();
            detail.put("name", fileName);
            detail.put("image", encodedImg);

            try {
                //convert this HashMap to encodedUrl to send to server
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to server
//                String response = Request.post(serverUrl, dataToSend, context,schoolCode);
                String response=     UploadMultipartData_without_retrofit.post(fileName,filePath,serverUrl,context,schoolCode);

                return response;

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
