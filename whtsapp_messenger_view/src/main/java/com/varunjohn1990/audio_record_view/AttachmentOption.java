package com.varunjohn1990.audio_record_view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Varun John on 12 Feb, 2020
 * Github : https://github.com/varunjohn
 */
public class AttachmentOption implements Serializable {

    private int id;
    private String title;
    private int resourceImage;

    public static final byte DOCUMENT_ID = 101;
//    public static final byte CAMERA_ID = 102;
    public static final byte GALLERY_ID = 103;
    public static final byte YOUTUBE_LINK_ID = 104;
    public static final byte EXTERNAL_LINK_ID = 105;
//    public static final byte CONTACT_ID = 106;

    public static List<AttachmentOption> getDefaultList() {
        List<AttachmentOption> attachmentOptions = new ArrayList<>();
//        attachmentOptions.add(new AttachmentOption(CAMERA_ID, "Camera", R.drawable.ic_camera_lg));
        attachmentOptions.add(new AttachmentOption(GALLERY_ID, "Gallery", R.drawable.ic_gallery_lg));
        attachmentOptions.add(new AttachmentOption(DOCUMENT_ID, "Document", R.drawable.ic_docs_lg));
        attachmentOptions.add(new AttachmentOption(YOUTUBE_LINK_ID, "Youtube Link", R.drawable.ic_youtube_lg));
        attachmentOptions.add(new AttachmentOption(EXTERNAL_LINK_ID, "External Link", R.drawable.ic_link_lg));
//        attachmentOptions.add(new AttachmentOption(CONTACT_ID, "Contact", R.drawable.ic_attachment_contact));

        return attachmentOptions;
    }

    public AttachmentOption(int id, String title, int resourceImage) {
        this.id = id;
        this.title = title;
        this.resourceImage = resourceImage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceImage() {
        return resourceImage;
    }
}
