package com.jeannypr.scientificstudy.Classwork.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.DownloadListener;
import com.jeannypr.scientificstudy.Utilities.FileDownloader;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.io.File;
import java.util.List;

public class CwHwDetailAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_LINK = 2;
    private static final int VIEW_TYPE_YOUTUBE = 3;
    private static final int VIEW_TYPE_FILE = 4;
    private static final int VIEW_TYPE_IMAGE = 5;
    private static final int VIEW_TYPE_AUDIO = 6;
    private static final int VIEW_TYPE_VIDEO = 7;

    private Context mContext;
    private List<ActivityItemModel> itemsList;
    private OnItemClickListner mListner;
    private int diaryType;

    public CwHwDetailAdapter(Context context, List<ActivityItemModel> items, int diaryType, OnItemClickListner listner) {
        super();
        this.mContext = context;
        this.itemsList = items;
        mListner = listner;
        this.diaryType = diaryType;
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ActivityItemModel items = itemsList.get(position);

        if (items.FileType == Constants.ActivityAttachmentType.FILE) {

            switch (items.FileExtension.toLowerCase()) {
                case Constants.FileType.JPEG:
                case Constants.FileType.JPG:
                case Constants.FileType.PNG:
                case Constants.FileType.BMP:
                case Constants.FileType.COD:
                case Constants.FileType.GIF:
                case Constants.FileType.IEF:
                case Constants.FileType.JPE:
                case Constants.FileType.JFIF:
                case Constants.FileType.SVG:
                case Constants.FileType.TIF:
                case Constants.FileType.TIFF:
                case Constants.FileType.RAS:
                case Constants.FileType.CMX:
                case Constants.FileType.ICO:
                case Constants.FileType.PNM:
                case Constants.FileType.PBM:
                case Constants.FileType.PGM:
                case Constants.FileType.RGB:
                case Constants.FileType.XBM:
                case Constants.FileType.XPM:
                case Constants.FileType.XWD:
                    return VIEW_TYPE_IMAGE;

                case Constants.FileType.MP3:
                case Constants.FileType.AMR:
                case Constants.FileType.M4A:
                case Constants.FileType.GPP:

                case Constants.FileType.AU:
                case Constants.FileType.SND:
                case Constants.FileType.RMI:
                case Constants.FileType.MID:
                case Constants.FileType.AIF:
                case Constants.FileType.AIFC:
                case Constants.FileType.AIFF:
                case Constants.FileType.M3U:
                case Constants.FileType.RA:
                case Constants.FileType.RAM:
                case Constants.FileType.WAV:
                    return VIEW_TYPE_AUDIO;

                case Constants.FileType.MP4:
                case Constants.FileType.AVI:
                case Constants.FileType.FLV:
                case Constants.FileType.M4V:
                case Constants.FileType.MP2:
                case Constants.FileType.MPA:
                case Constants.FileType.MPE:
                case Constants.FileType.MPG:
                case Constants.FileType.MPEG:
                case Constants.FileType.MPV2:
                case Constants.FileType.MOV:
                case Constants.FileType.QT:
                case Constants.FileType.LSF:
                case Constants.FileType.LSX:
                case Constants.FileType.ASR:
                case Constants.FileType.ASF:
                case Constants.FileType.ASX:

                    return VIEW_TYPE_VIDEO;
                default:
                    return VIEW_TYPE_FILE;
            }

        } else if (items.FileType == Constants.ActivityAttachmentType.MEDIA) {
            return VIEW_TYPE_YOUTUBE;
        } else if (items.FileType == Constants.ActivityAttachmentType.LINK) {
            return VIEW_TYPE_LINK;
        } else
            return VIEW_TYPE_TEXT;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_FILE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_file, parent, false);
                return new FileHolder(view);

            case VIEW_TYPE_YOUTUBE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_youtube, parent, false);
                return new YoutubeHolder(view);

            case VIEW_TYPE_LINK:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_link, parent, false);
                return new LinkHolder(view);

            case VIEW_TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_image, parent, false);
                return new ImageHolder(view);

            case VIEW_TYPE_AUDIO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_audio, parent, false);
                return new AudioHolder(view);

            case VIEW_TYPE_VIDEO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_video, parent, false);
                return new VideoHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_activity_item_text, parent, false);
                return new TextHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityItemModel item = (ActivityItemModel) itemsList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_FILE:
                ((FileHolder) holder).bind(item);
                break;
            case VIEW_TYPE_YOUTUBE:
                ((YoutubeHolder) holder).bind(item);
                break;
            case VIEW_TYPE_LINK:
                ((LinkHolder) holder).bind(item);
                break;
            case VIEW_TYPE_TEXT:
                ((TextHolder) holder).bind(item);
                break;
            case VIEW_TYPE_IMAGE:
                ((ImageHolder) holder).bind(item);
                break;
            case VIEW_TYPE_AUDIO:
                ((AudioHolder) holder).bind(item);
                break;
            case VIEW_TYPE_VIDEO:
                ((VideoHolder) holder).bind(item);
                break;
        }
    }

    private class FileHolder extends RecyclerView.ViewHolder {
        ImageView icDownload, icFile, thumbnail;
        TextView fileUrl;
        ProgressBar progressBar;
        ConstraintLayout pdfRow;

        FileHolder(View itemView) {
            super(itemView);

            fileUrl = itemView.findViewById(R.id.fileUrl);
            icDownload = itemView.findViewById(R.id.icDownload);
            icFile = itemView.findViewById(R.id.icFile);
            progressBar = itemView.findViewById(R.id.progressBar);
            pdfRow = itemView.findViewById(R.id.pdfRow);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        void bind(final ActivityItemModel item) {
            fileUrl.setText(item.AttachmentName);

            switch (item.FileExtension.toLowerCase()) {
                case Constants.FileType.PDF:
                    icFile.setImageResource(R.drawable.pdf);
                    break;

                case Constants.FileType.DOC:
                    icFile.setImageResource(R.drawable.doc);
                    break;

                case Constants.FileType.DOCX:
                    icFile.setImageResource(R.drawable.docx);
                    break;
            }

            if (item.HasThumbnail) {
                Glide.with(mContext).load(item.ThumbnailPath).into(thumbnail);
            } else {
                Glide.with(mContext).load(R.drawable.default_image).into(thumbnail);
            }

            if (Utility.IsFileExists(item.Path)) {
                icDownload.setVisibility(View.GONE);
                pdfRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                            }

                            @Override
                            public void onDownloadStart() {

                            }
                        });

                    }
                });

            } else {
                icDownload.setImageResource(android.R.drawable.stat_sys_download);
            }


            icDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);

                            pdfRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {

                                        }

                                        @Override
                                        public void onDownloadStart() {

                                        }
                                    });

                                }
                            });
                        }

                        @Override
                        public void onDownloadStart() {
                            icDownload.setEnabled(false);
                            icDownload.setClickable(false);
                            progressBar.setVisibility(View.VISIBLE);
                            icDownload.setVisibility(View.GONE);
                            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                        }
                    });
                }
            });

        }

    }

    private class AudioHolder extends RecyclerView.ViewHolder {
        ImageView icDownload;
        TextView fileUrl;
        ProgressBar progressBar;
        RelativeLayout contentRow;

        AudioHolder(View itemView) {
            super(itemView);

            fileUrl = itemView.findViewById(R.id.audioFileUrl);
            icDownload = itemView.findViewById(R.id.audioDownload);
            progressBar = itemView.findViewById(R.id.progressBar);
            contentRow = itemView.findViewById(R.id.contentRow);
        }


        void bind(final ActivityItemModel item) {
            fileUrl.setText(item.AttachmentName);

            if (Utility.IsFileExists(item.Path)) {
                icDownload.setImageResource(android.R.drawable.ic_media_play);

                contentRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                            }

                            @Override
                            public void onDownloadStart() {

                            }
                        });
                    }
                });

            } else {
                icDownload.setImageResource(android.R.drawable.stat_sys_download);
            }


            icDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);
                            icDownload.setImageResource(android.R.drawable.ic_media_play);

                            contentRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {

                                        }

                                        @Override
                                        public void onDownloadStart() {

                                        }
                                    });

                                }
                            });
                        }

                        @Override
                        public void onDownloadStart() {
                            icDownload.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                }
            });

        }

    }

    private class YoutubeHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, thumbnailImg;
        TextView mediaLink;
        ConstraintLayout youtubeRow;


        YoutubeHolder(View itemView) {
            super(itemView);

            icMedia = itemView.findViewById(R.id.icMedia);
            thumbnailImg = itemView.findViewById(R.id.thumbnail);
            mediaLink = itemView.findViewById(R.id.mediaLink);
            youtubeRow = itemView.findViewById(R.id.youtubeRow);
        }

        void bind(final ActivityItemModel item) {
            String thumbnailPath = Utility.getYoutubeThumbnail(item.Path);
            if (thumbnailPath != null) {
                Glide.with(mContext).load(thumbnailPath).into(thumbnailImg);
            } else {
                thumbnailImg.setImageResource(R.drawable.default_image);
            }

            mediaLink.setText(item.Title);
            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in youtube app
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.Title)));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            });
        }
    }

    private class VideoHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, playIc;
        TextView mediaLink;
        ConstraintLayout youtubeRow;
        ProgressBar progressBar;


        VideoHolder(View itemView) {
            super(itemView);

            icMedia = itemView.findViewById(R.id.icMedia);
            playIc = itemView.findViewById(R.id.playIc);
            mediaLink = itemView.findViewById(R.id.mediaLink);
            youtubeRow = itemView.findViewById(R.id.youtubeRow);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        void bind(final ActivityItemModel item) {
            //String thumbnailPath = Utility.getYoutubeThumbnail(item.Path);

            mediaLink.setText(item.AttachmentName);
            if (Utility.IsFileExists(item.Path)) {
                playIc.setImageResource(android.R.drawable.ic_media_play);

                youtubeRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                            }

                            @Override
                            public void onDownloadStart() {

                            }
                        });
                    }
                });

            } else {
                playIc.setImageResource(android.R.drawable.stat_sys_download);
            }

            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);
                            playIc.setImageResource(android.R.drawable.ic_media_play);
                            playIc.setVisibility(View.VISIBLE);

                            youtubeRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {

                                        }

                                        @Override
                                        public void onDownloadStart() {

                                        }
                                    });

                                }
                            });
                        }

                        @Override
                        public void onDownloadStart() {
                            playIc.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                        }
                    });
                }
            });

        }
    }

    private class LinkHolder extends RecyclerView.ViewHolder {
        ImageView icLink;
        TextView linkUrl;

        LinkHolder(View itemView) {
            super(itemView);

            icLink = itemView.findViewById(R.id.icLink);
            linkUrl = itemView.findViewById(R.id.linkUrl);
        }

        void bind(final ActivityItemModel item) {
            linkUrl.setText(item.Title);
            linkUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in browser

                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.Title)));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private class TextHolder extends RecyclerView.ViewHolder {
        TextView textMsg;

        TextHolder(View itemView) {
            super(itemView);
            textMsg = itemView.findViewById(R.id.textMsg);
        }

        void bind(final ActivityItemModel item) {
            if (diaryType == Constants.DiaryType.Classwork) {
                textMsg.setText(item.Title);
                itemView.findViewById(R.id.row_activity_item_file).setVisibility(View.VISIBLE);
            } else {
                mListner.onItemClick(item.Title);
                itemView.findViewById(R.id.row_activity_item_file).setVisibility(View.GONE);
            }
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        ImageView icDownload, image;
        TextView txtSize;
        LinearLayout downloadIcRow;
        ProgressBar progressBar;
        RelativeLayout imageRow;

        ImageHolder(View itemView) {
            super(itemView);

            txtSize = itemView.findViewById(R.id.size);
            icDownload = itemView.findViewById(R.id.icDownload);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
            downloadIcRow = itemView.findViewById(R.id.downloadIcRow);
            imageRow = (RelativeLayout) itemView;
        }

        void bind(final ActivityItemModel item) {
            txtSize.setText(item.Size == null ? "" : item.Size);

            if (Utility.IsFileExists(item.Path)) {
                downloadIcRow.setVisibility(View.GONE);

                Uri uri = Utility.GetUriFromPath(item.Path);
                if (uri != null) {
                    Glide.with(mContext).load(uri).into(image);
                }
                imageRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                            }

                            @Override
                            public void onDownloadStart() {

                            }
                        });
                    }
                });

            } else {
                if (item.HasThumbnail) {
                    Glide.with(mContext).load(item.ThumbnailPath).into(image);
                } else {
                    Glide.with(mContext).load(R.drawable.default_image).into(image);
                }
                downloadIcRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                progressBar.setVisibility(View.GONE);

                                Log.e("TAG", "onDownloadComplete: " + item.Path);
                                Uri uri = Utility.GetUriFromPath(item.Path);
                                if (uri != null) {
                                    Glide.with(mContext).load(uri).into(image);
                                }

                                imageRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.Path, item.FileExtension, new DownloadListener() {
                                            @Override
                                            public void onDownloadComplete() {

                                            }

                                            @Override
                                            public void onDownloadStart() {

                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onDownloadStart() {
                                downloadIcRow.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
                                if (!directory.exists()) {
                                    directory.mkdirs();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    public interface OnItemClickListner {
        void onItemClick(String title);
    }
}

