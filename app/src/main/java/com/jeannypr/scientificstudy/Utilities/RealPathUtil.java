package com.jeannypr.scientificstudy.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class RealPathUtil {

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath = "";
        try {
            // SDK < API11
            int version = Build.VERSION.SDK_INT;
            if (version < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, fileUri);
            }
            // SDK >= 11 && SDK < 19
            else if (version >= 11 && version < 19) {
                //  realPath = RealPathUtil.getRealPath(context.getContentResolver(), fileUri, null);
                realPath = RealPathUtil.getRealPathFromURI_API11to18(context, fileUri);
            }
            // SDK > 19 (Android 4.4) and up
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(context, fileUri);
            }

        } catch (Exception ex) {
            Log.e("Uploading attachment - ", ex.getMessage());
        }
        return realPath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        /*Cursor cursor = null;

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
            cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                cursor.close();
            }
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        String result = "";
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return result;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /* Return uri represented document file real local path.*/
    @SuppressLint("Recycle")
    private static String getRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        Cursor cursor = null;

        try {

            String ret = "";

            // Query the uri with condition.
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = contentResolver.query(uri, proj, whereClause, null, null);

            if (cursor != null) {
                boolean moveToFirst = cursor.moveToFirst();
                if (moveToFirst) {

                    // Get columns name by uri type.
                    String columnName = MediaStore.Images.Media.DATA;

                    if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Images.Media.DATA;
                    } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Audio.Media.DATA;
                    } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Video.Media.DATA;
                    }

                    // Get column index.
                    int columnIndex = cursor.getColumnIndexOrThrow(columnName);

                    // Get column value which is the uri related file local path.
                    ret = cursor.getString(columnIndex);
                }
            }
            return ret;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
}
