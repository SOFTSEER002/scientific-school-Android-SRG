package com.jeannypr.scientificstudy.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.jeannypr.scientificstudy.Base.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import static com.jeannypr.scientificstudy.Base.Constants.FILE_AUTHORITY;


public class FileDownloader {
    static int downloadedSize = 0, totalsize;
    static float per = 0;
    private static final int MEGABYTE = 1024 * 1024;

    private File _download(String fileUrl, String directory) {

        File file = null;
        try {

            URL url = new URL(fileUrl.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            //   urlConnection.setDoOutput(true);
            // connect
            urlConnection.connect();
            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, directory);
            FileOutputStream fileOutput = new FileOutputStream(file);
            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();
            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();


            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
             /*   setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");*/
            }
            // close the output stream when complete //
            fileOutput.close();
            //setText("Download Complete. Open PDF Application installed in the device.");
        } catch (final Exception e) {
            Log.e("File downloader: ", e.getMessage());
        }
        return file;

    }

    public void DownloadAndOpen(final Activity mContext, String download_file_url, final String fileExtension, DownloadListener downloadListener) {
        URI uri = null;
        try {
            int lastIndex = download_file_url.lastIndexOf('/');

            String fileName = download_file_url.substring(lastIndex + 1);
            String fileEncodedName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            String basefilePath = download_file_url.substring(0, lastIndex + 1);
            download_file_url = basefilePath.concat(fileEncodedName);
            uri = new URI(download_file_url);

            File storage = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            final File file = new File(storage, Constants.Directory.Base + File.separator + fileName);
            String fileDataType = Utility.getFileDataType(fileExtension);
            if (!file.exists()) {
                new DownloadFileTask(downloadListener).execute(download_file_url, fileDataType);
            } else {
                downloadListener.onDownloadComplete();
                _open(mContext, file, fileDataType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return;
        }

    }

    private void _open(Activity mContext, File file, String fileDataType) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, FILE_AUTHORITY, file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, fileDataType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(mContext, "Please click again to view the file", Toast.LENGTH_LONG).show();
            }

        } else {
            Uri path1 = Uri.fromFile(file);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path1, fileDataType);
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        }
    }


    private class DownloadFileTask extends AsyncTask<String, Void, File> {
        public ProgressDialog dialog;
        String dataType;
        DownloadListener downloadListner;

        public DownloadFileTask(DownloadListener downloadListner) {
            this.downloadListner = downloadListner;
        }

        @Override
        protected void onPreExecute() {
            downloadListner.onDownloadStart();
        }

        @Override
        protected File doInBackground(String... params) {
            File file = null;
            try {
                String fileUrl = params[0];
                dataType = params[1];
                URI uri = new URI(fileUrl);
                String path = uri.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                file = _download(fileUrl, Constants.Directory.Base + File.separator + fileName);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            downloadListner.onDownloadComplete();
        }
    }
}



