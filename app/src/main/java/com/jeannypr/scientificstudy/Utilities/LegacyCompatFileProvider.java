package com.jeannypr.scientificstudy.Utilities;

import android.database.Cursor;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.commonsware.cwac.provider.LegacyCompatCursorWrapper;

/**
 * Created by USER on 2018-01-03.
 */

public class LegacyCompatFileProvider extends FileProvider {
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return(new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
    }
}