package com.jeannypr.scientificstudy.Classwork.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Classwork.model.HWCommentModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.DownloadListener;
import com.jeannypr.scientificstudy.Utilities.FileDownloader;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowHwAudioReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwAudioSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwFileReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwFileSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwImageReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwImageSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwLinkReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwLinkSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwTextReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwTextSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwVideoReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwVideoSentBinding;
import com.jeannypr.scientificstudy.databinding.RowHwYoutubeReceivedBinding;
import com.jeannypr.scientificstudy.databinding.RowHwYoutubeSentBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kannu
 */
public class HWInstructionCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HWCommentModel> messages = new ArrayList<>();
    private static SimpleDateFormat timeFormatter;
    private HWInstructionCommentAdapter.MessageInterface mListner;
    private UserPreference mUserPref;

    private static final int VIEW_TYPE_TEXT_SENT = 1;
    private static final int VIEW_TYPE_LINK_SENT = 2;
    private static final int VIEW_TYPE_YOUTUBE_SENT = 3;
    private static final int VIEW_TYPE_FILE_SENT = 4;
    private static final int VIEW_TYPE_IMAGE_SENT = 5;
    private static final int VIEW_TYPE_AUDIO_SENT = 6;
    private static final int VIEW_TYPE_VIDEO_SENT = 7;

    private static final int VIEW_TYPE_TEXT_RECEIVED = 8;
    private static final int VIEW_TYPE_LINK_RECEIVED = 9;
    private static final int VIEW_TYPE_YOUTUBE_RECEIVED = 10;
    private static final int VIEW_TYPE_FILE_RECEIVED = 11;
    private static final int VIEW_TYPE_IMAGE_RECEIVED = 12;
    private static final int VIEW_TYPE_AUDIO_RECEIVED = 13;
    private static final int VIEW_TYPE_VIDEO_RECEIVED = 14;
    private Context mContext;
//    private Boolean isLastItem = false;

    public HWInstructionCommentAdapter(MessageInterface listner, Context context) {
        mListner = listner;
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
        mUserPref = UserPreference.getInstance(context);
        mContext = context;
    }

    public void add(HWCommentModel message) {
        messages.add(message);
//        notifyItemChanged(messages.lastIndexOf(message));
        notifyItemChanged(message.AdapterPosition, message);
    }

    public void clear() {
        messages.clear();
//        notifyDataSetChanged();
    }

    public List<HWCommentModel> getData() {
        return messages;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        HWCommentModel message = messages.get(position);
        message.adapterPosition = position;
//        if (position == messages.size() - 1) isLastItem = true;
//        else isLastItem = false;

        if (!message.getFilePath().contains(Constants.HTTP + Constants.MOBILE))
            message.setFilePath(Constants.IMAGE_BASE_URL + Constants.SLASH + message.getFilePath());

        if (mUserPref.getUserData().getRoleTitle().equals(Constants.Role.PARENT))
            message.isMsgSent = message.getCommentedBy() == Constants.HWCommentBy.STUDENT;
        else message.isMsgSent = message.getCommentedBy() == Constants.HWCommentBy.TEACHER;

        if (message.getFileExtension().contains("."))
            message.setFileExtension(message.getFileExtension().substring(1).toLowerCase());
        else message.setFileExtension(message.getFileExtension().toLowerCase());

        if (message.getFileType() == Constants.ActivityAttachmentType.FILE && message.isMsgSent) {
            switch (message.getFileExtension()) {
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
                    return VIEW_TYPE_IMAGE_SENT;

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
                    return VIEW_TYPE_AUDIO_SENT;

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

                    return VIEW_TYPE_VIDEO_SENT;
                default:
                    return VIEW_TYPE_FILE_SENT;
            }

        } else if (message.getFileType() == Constants.ActivityAttachmentType.MEDIA && message.isMsgSent) {
            return VIEW_TYPE_YOUTUBE_SENT;
        } else if (message.getFileType() == Constants.ActivityAttachmentType.LINK && message.isMsgSent) {
            return VIEW_TYPE_LINK_SENT;
        } else if (message.getFileType() == Constants.ActivityAttachmentType.TEXT && message.isMsgSent) {
            return VIEW_TYPE_TEXT_SENT;

        } else {
            if (message.getFileType() == Constants.ActivityAttachmentType.FILE && !message.isMsgSent) {
                if (message.getFileExtension().contains("."))
                    message.setFileExtension(message.getFileExtension().substring(1).toLowerCase());
                else message.setFileExtension(message.getFileExtension().toLowerCase());
                switch (message.getFileExtension()) {
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
                        return VIEW_TYPE_IMAGE_RECEIVED;

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
                        return VIEW_TYPE_AUDIO_RECEIVED;

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

                        return VIEW_TYPE_VIDEO_RECEIVED;
                    default:
                        return VIEW_TYPE_FILE_RECEIVED;
                }

            } else if (message.getFileType() == Constants.ActivityAttachmentType.MEDIA && !message.isMsgSent) {
                return VIEW_TYPE_YOUTUBE_RECEIVED;
            } else if (message.getFileType() == Constants.ActivityAttachmentType.LINK && !message.isMsgSent) {
                return VIEW_TYPE_LINK_RECEIVED;
            } else {
                return VIEW_TYPE_TEXT_RECEIVED;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE_SENT) {
            return new HWInstructionCommentAdapter.SentImageHolder((RowHwImageSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_image_sent, parent, false));

        } else if (viewType == VIEW_TYPE_AUDIO_SENT) {
            return new HWInstructionCommentAdapter.SentAudioHolder((RowHwAudioSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_audio_sent, parent, false));

        } else if (viewType == VIEW_TYPE_VIDEO_SENT) {
            return new HWInstructionCommentAdapter.SentVideoHolder((RowHwVideoSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_video_sent, parent, false));

        } else if (viewType == VIEW_TYPE_FILE_SENT) {
            return new HWInstructionCommentAdapter.SentFileHolder((RowHwFileSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_file_sent, parent, false));

        } else if (viewType == VIEW_TYPE_YOUTUBE_SENT) {
            return new HWInstructionCommentAdapter.SentYoutubeHolder((RowHwYoutubeSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_youtube_sent, parent, false));

        } else if (viewType == VIEW_TYPE_LINK_SENT) {
            return new HWInstructionCommentAdapter.SentLinkHolder((RowHwLinkSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_link_sent, parent, false));

        } else if (viewType == VIEW_TYPE_TEXT_SENT)
            return new HWInstructionCommentAdapter.SentTextHolder((RowHwTextSentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_text_sent, parent, false));

        else if (viewType == VIEW_TYPE_IMAGE_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedImageHolder((RowHwImageReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_image_received, parent, false));

        else if (viewType == VIEW_TYPE_AUDIO_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedAudioHolder((RowHwAudioReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_audio_received, parent, false));

        else if (viewType == VIEW_TYPE_VIDEO_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedVideoHolder((RowHwVideoReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_video_received, parent, false));

        else if (viewType == VIEW_TYPE_FILE_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedFileHolder((RowHwFileReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_file_received, parent, false));

        else if (viewType == VIEW_TYPE_YOUTUBE_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedYoutubeHolder((RowHwYoutubeReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_youtube_received, parent, false));

        else if (viewType == VIEW_TYPE_LINK_RECEIVED)
            return new HWInstructionCommentAdapter.ReceivedLinkHolder((RowHwLinkReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_hw_link_received, parent, false));

        return new HWInstructionCommentAdapter.ReceivedTextHolder((RowHwTextReceivedBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_hw_text_received, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HWCommentModel message = messages.get(position);
        message.AdapterPosition = position;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_IMAGE_SENT:
                ((HWInstructionCommentAdapter.SentImageHolder) holder).bind(message);
                break;

            case VIEW_TYPE_AUDIO_SENT:
                ((HWInstructionCommentAdapter.SentAudioHolder) holder).bind(message);
                break;

            case VIEW_TYPE_VIDEO_SENT:
                ((HWInstructionCommentAdapter.SentVideoHolder) holder).bind(message);
                break;

            case VIEW_TYPE_FILE_SENT:
                ((HWInstructionCommentAdapter.SentFileHolder) holder).bind(message);
                break;

            case VIEW_TYPE_YOUTUBE_SENT:
                ((HWInstructionCommentAdapter.SentYoutubeHolder) holder).bind(message);
                break;

            case VIEW_TYPE_LINK_SENT:
                ((HWInstructionCommentAdapter.SentLinkHolder) holder).bind(message);
                break;

            case VIEW_TYPE_TEXT_SENT:
                ((HWInstructionCommentAdapter.SentTextHolder) holder).bind(message);
                break;

            case VIEW_TYPE_IMAGE_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedImageHolder) holder).bind(message);
                break;

            case VIEW_TYPE_AUDIO_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedAudioHolder) holder).bind(message);
                break;

            case VIEW_TYPE_VIDEO_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedVideoHolder) holder).bind(message);
                break;

            case VIEW_TYPE_FILE_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedFileHolder) holder).bind(message);
                break;

            case VIEW_TYPE_YOUTUBE_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedYoutubeHolder) holder).bind(message);
                break;

            case VIEW_TYPE_LINK_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedLinkHolder) holder).bind(message);
                break;

            case VIEW_TYPE_TEXT_RECEIVED:
                ((HWInstructionCommentAdapter.ReceivedTextHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static String getAudioTime(long time) {
        time *= 1000;
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(time));
    }

    private class SentFileHolder extends RecyclerView.ViewHolder {
        ImageView icDownload, icFile, thumbnail;
        TextView fileUrl;
        ProgressBar progressBar;
        ConstraintLayout pdfRow;
        RowHwFileSentBinding mViewBinding;

        SentFileHolder(RowHwFileSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            //TODO: show comment time
            fileUrl = mViewBinding.fileUrl;
            icDownload = mViewBinding.icDownload;
            icFile = mViewBinding.icFile;
            progressBar = mViewBinding.progressBar;
            pdfRow = mViewBinding.pdfRow;
            thumbnail = mViewBinding.thumbnail;
        }

        void bind(final HWCommentModel item) {
            fileUrl.setText(item.getFileName());
            mViewBinding.dateView.setText(item.getSentOn());
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());

            switch (item.getFileExtension().toLowerCase()) {
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

            if (item.getHasThumbnail()) {
                Glide.with(mContext).load(item.getThumbnailPath()).into(thumbnail);
            } else {
                Glide.with(mContext).load(R.drawable.default_image).into(thumbnail);
            }

            if (Utility.IsFileExists(item.getFilePath())) {
                icDownload.setVisibility(View.GONE);
                pdfRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);

                            pdfRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                        }
                    });
                }
            });

        }

    }

    private class SentAudioHolder extends RecyclerView.ViewHolder {
        ImageView icDownload;
        TextView fileUrl;
        ProgressBar progressBar;
        RelativeLayout contentRow;
        RowHwAudioSentBinding mViewBinding;

        SentAudioHolder(RowHwAudioSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            fileUrl = mViewBinding.audioFileUrl;
            icDownload = mViewBinding.audioDownload;
            progressBar = mViewBinding.progressBar;
            contentRow = mViewBinding.contentRow;
        }


        void bind(final HWCommentModel item) {
            fileUrl.setText(item.getFileName());
            mViewBinding.dateView.setText(item.getSentOn());
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());

            if (Utility.IsFileExists(item.getFilePath())) {
                icDownload.setImageResource(android.R.drawable.ic_media_play);

                contentRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            progressBar.setVisibility(View.GONE);
                            icDownload.setImageResource(android.R.drawable.ic_media_play);

                            contentRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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

    private class SentYoutubeHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, thumbnailImg;
        TextView mediaLink;
        ConstraintLayout youtubeRow;
        RowHwYoutubeSentBinding mViewBinding;

        SentYoutubeHolder(RowHwYoutubeSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            icMedia = mViewBinding.icMedia;
            thumbnailImg = mViewBinding.thumbnail;
            mediaLink = mViewBinding.mediaLink;
            youtubeRow = mViewBinding.youtubeRow;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            String thumbnailPath = Utility.getYoutubeThumbnail(item.getFilePath());
            if (thumbnailPath != null) {
                Glide.with(mContext).load(thumbnailPath).into(thumbnailImg);
            } else {
                thumbnailImg.setImageResource(R.drawable.default_image);
            }

            mediaLink.setText(item.getComment());
            mViewBinding.dateView.setText(item.getSentOn());
            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in youtube app
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getComment())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            });
        }
    }

    private class SentVideoHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, playIc;
        TextView mediaLink;
        ConstraintLayout youtubeRow;
        ProgressBar progressBar;
        RowHwVideoSentBinding mViewBinding;

        SentVideoHolder(RowHwVideoSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            playIc = mViewBinding.playIc;
            mediaLink = mViewBinding.mediaLink;
            youtubeRow = mViewBinding.youtubeRow;
            progressBar = mViewBinding.progressBar;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            //String thumbnailPath = Utility.getYoutubeThumbnail(item.Path);
            mViewBinding.dateView.setText(item.getSentOn());

            mediaLink.setText(item.getFileName());
            if (Utility.IsFileExists(item.getFilePath())) {
                playIc.setImageResource(android.R.drawable.ic_media_play);

                playIc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListner.onClickAudio(item.getFilePath());
                    }
                });
                /*  youtubeRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                            }

                            @Override
                            public void onDownloadStart() {

                            }
                        });
                    }
                });*/

            } else {
                playIc.setImageResource(android.R.drawable.stat_sys_download);
            }

            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);
                            playIc.setImageResource(android.R.drawable.ic_media_play);
                            playIc.setVisibility(View.VISIBLE);

                          /*  youtubeRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {

                                        }

                                        @Override
                                        public void onDownloadStart() {

                                        }
                                    });

                                }
                            });*/
                            playIc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mListner.onClickAudio(item.getFilePath());
                                }
                            });
                        }

                        @Override
                        public void onDownloadStart() {
                            playIc.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });

        }
    }

    private class SentLinkHolder extends RecyclerView.ViewHolder {
        ImageView icLink;
        TextView linkUrl;
        RowHwLinkSentBinding mViewBinding;

        SentLinkHolder(RowHwLinkSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            icLink = mViewBinding.icLink;
            linkUrl = mViewBinding.linkUrl;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            linkUrl.setText(item.getComment());
            mViewBinding.dateView.setText(item.getSentOn());

            linkUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in browser
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getComment())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private class SentTextHolder extends RecyclerView.ViewHolder {
        //        TextView textMsg;
        RowHwTextSentBinding mViewBinding;

        SentTextHolder(RowHwTextSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;
//            textMsg = itemView.findViewById(R.id.textMsg);
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            if (!item.getComment().equals("")) mViewBinding.textMsg.setText(item.getComment());
            else mViewBinding.textMsg.setText(item.getCurrentStatus());
            mViewBinding.dateView.setText(item.getSentOn());
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {
        /*  ImageView icDownload, image;
          TextView txtSize;
          LinearLayout downloadIcRow;
          ProgressBar progressBar;
          RelativeLayout imageRow;*/
        RowHwImageSentBinding mViewBinding;

        SentImageHolder(RowHwImageSentBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

          /*  txtSize = itemView.findViewById(R.id.size);
            icDownload = itemView.findViewById(R.id.icDownload);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
            downloadIcRow = itemView.findViewById(R.id.downloadIcRow);
            imageRow = (RelativeLayout) itemView;*/
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            mViewBinding.size.setText(item.getFileSize() == null ? "" : item.getFileSize());
            mViewBinding.dateView.setText(item.getSentOn());

//            item.setFilePath("http://mobile.scientificstudy.in/" + item.getFilePath());
            if (Utility.IsFileExists(item.getFilePath())) { //http://mobile.scientificstudy.in/SCHOOLDATA/dev/ClassWork/66/Screenshot(319).png
                mViewBinding.downloadIcRow.setVisibility(View.GONE);

                Uri uri = Utility.getUriFromPath(item.getFilePath());
                if (uri != null)
                    Glide.with(mContext).load(uri).into(mViewBinding.image);
                else Glide.with(mContext).load(R.drawable.default_image).into(mViewBinding.image);

                mViewBinding.imageRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
//                if (item.uri != null) {
//                    Glide.with(mContext).load(item.uri).into(mViewBinding.image);
//                    mViewBinding.downloadIcRow.setVisibility(View.GONE);
//                } else {
                if (item.getHasThumbnail())
                    Glide.with(mContext).load(item.getThumbnailPath()).into(mViewBinding.image);
                else
                    Glide.with(mContext).load(R.drawable.default_image).into(mViewBinding.image);
//                }

                mViewBinding.downloadIcRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                mViewBinding.progressBar.setVisibility(View.GONE);

                                Uri uri = Utility.getUriFromPath(item.getFilePath());
                                if (uri != null) {
                                    Glide.with(mContext).load(uri).into(mViewBinding.image);
                                } else
                                    Glide.with(mContext).load(R.drawable.default_image).into(mViewBinding.image);

                                mViewBinding.imageRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                                mViewBinding.downloadIcRow.setVisibility(View.GONE);
                                mViewBinding.progressBar.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }
        }
    }

    private class ReceivedFileHolder extends RecyclerView.ViewHolder {
        RowHwFileReceivedBinding mViewBinding;

        ReceivedFileHolder(RowHwFileReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            mViewBinding.fileUrl.setText(item.getFileName());
            mViewBinding.dateView.setText(item.getSentOn());

            switch (item.getFileExtension().toLowerCase()) {
                case Constants.FileType.PDF:
                    mViewBinding.icFile.setImageResource(R.drawable.pdf);
                    break;

                case Constants.FileType.DOC:
                    mViewBinding.icFile.setImageResource(R.drawable.doc);
                    break;

                case Constants.FileType.DOCX:
                    mViewBinding.icFile.setImageResource(R.drawable.docx);
                    break;
            }

            if (item.getHasThumbnail()) {
                Glide.with(mContext).load(item.getThumbnailPath()).into(mViewBinding.thumbnail);
            } else {
                Glide.with(mContext).load(R.drawable.default_image).into(mViewBinding.thumbnail);
            }

            if (Utility.IsFileExists(item.getFilePath())) {
                mViewBinding.icDownload.setVisibility(View.GONE);
                mViewBinding.pdfRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                mViewBinding.icDownload.setImageResource(android.R.drawable.stat_sys_download);
            }

            mViewBinding.icDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            mViewBinding.progressBar.setVisibility(View.GONE);

                            mViewBinding.pdfRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                            mViewBinding.icDownload.setEnabled(false);
                            mViewBinding.icDownload.setClickable(false);
                            mViewBinding.progressBar.setVisibility(View.VISIBLE);
                            mViewBinding.icDownload.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    private class ReceivedAudioHolder extends RecyclerView.ViewHolder {
        RowHwAudioReceivedBinding mViewBinding;

        ReceivedAudioHolder(RowHwAudioReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

//            fileUrl = itemView.findViewById(R.id.audioFileUrl);
//            icDownload = itemView.findViewById(R.id.audioDownload);
//            progressBar = itemView.findViewById(R.id.progressBar);
//            contentRow = itemView.findViewById(R.id.contentRow);
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            mViewBinding.audioFileUrl.setText(item.getFileName());
            mViewBinding.dateView.setText(item.getSentOn());

            if (Utility.IsFileExists(item.getFilePath())) {
                mViewBinding.audioDownload.setImageResource(android.R.drawable.ic_media_play);

                mViewBinding.contentRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                mViewBinding.audioDownload.setImageResource(android.R.drawable.stat_sys_download);
            }

            mViewBinding.audioDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            mViewBinding.progressBar.setVisibility(View.GONE);
                            mViewBinding.audioDownload.setImageResource(android.R.drawable.ic_media_play);

                            mViewBinding.contentRow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                            mViewBinding.audioDownload.setVisibility(View.GONE);
                            mViewBinding.progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                }
            });
        }
    }

    private class ReceivedYoutubeHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, thumbnailImg;
        TextView mediaLink;
        ConstraintLayout youtubeRow;
        RowHwYoutubeReceivedBinding mViewBinding;

        ReceivedYoutubeHolder(RowHwYoutubeReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            icMedia = mViewBinding.icMedia;
            thumbnailImg = mViewBinding.thumbnail;
            mediaLink = mViewBinding.mediaLink;
            youtubeRow = mViewBinding.youtubeRow;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            String thumbnailPath = Utility.getYoutubeThumbnail(item.getFilePath());
            mViewBinding.dateView.setText(item.getSentOn());

            if (thumbnailPath != null) {
                Glide.with(mContext).load(thumbnailPath).into(thumbnailImg);
            } else {
                thumbnailImg.setImageResource(R.drawable.default_image);
            }

            mediaLink.setText(item.getComment());
            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in youtube app
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getComment())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            });
        }
    }

    private class ReceivedVideoHolder extends RecyclerView.ViewHolder {
        ImageView icMedia, playIc;
        TextView mediaLink;
        ConstraintLayout youtubeRow;
        ProgressBar progressBar;
        RowHwVideoReceivedBinding mViewBinding;

        ReceivedVideoHolder(RowHwVideoReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            playIc = mViewBinding.playIc;
            mediaLink = mViewBinding.mediaLink;
            youtubeRow = mViewBinding.youtubeRow;
            progressBar = mViewBinding.progressBar;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            //String thumbnailPath = Utility.getYoutubeThumbnail(item.Path);

            mediaLink.setText(item.getFileName());
            mViewBinding.dateView.setText(item.getSentOn());

            if (Utility.IsFileExists(item.getFilePath())) {
                playIc.setImageResource(android.R.drawable.ic_media_play);

                playIc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListner.onClickAudio(item.getFilePath());
                    }
                });

            } else {
                playIc.setImageResource(android.R.drawable.stat_sys_download);
            }

            youtubeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            progressBar.setVisibility(View.GONE);
                            playIc.setImageResource(android.R.drawable.ic_media_play);
                            playIc.setVisibility(View.VISIBLE);

                            playIc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mListner.onClickAudio(item.getFilePath());
                                }
                            });
                        }

                        @Override
                        public void onDownloadStart() {
                            playIc.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });

        }
    }

    private class ReceivedLinkHolder extends RecyclerView.ViewHolder {
        ImageView icLink;
        TextView linkUrl;
        RowHwLinkReceivedBinding mViewBinding;

        ReceivedLinkHolder(RowHwLinkReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            icLink = mViewBinding.icLink;
            linkUrl = mViewBinding.linkUrl;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            linkUrl.setText(item.getComment());
            mViewBinding.dateView.setText(item.getSentOn());
            linkUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: open link in browser

                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getComment())));
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private class ReceivedTextHolder extends RecyclerView.ViewHolder {
        RowHwTextReceivedBinding mViewBinding;

        ReceivedTextHolder(RowHwTextReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;
//            textMsg = itemView.findViewById(R.id.textMsg);
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            if (!item.getComment().equals("")) mViewBinding.textMsg.setText(item.getComment());
            else mViewBinding.textMsg.setText(item.getCurrentStatus());
            mViewBinding.dateView.setText(item.getSentOn());
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView icDownload, image;
        TextView txtSize;
        LinearLayout downloadIcRow;
        ProgressBar progressBar;
        RelativeLayout imageRow;
        RowHwImageReceivedBinding mViewBinding;

        ReceivedImageHolder(RowHwImageReceivedBinding binding) {
            super(binding.getRoot());
            mViewBinding = binding;

            txtSize = mViewBinding.size;
            icDownload = mViewBinding.icDownload;
            image = mViewBinding.image;
            progressBar = mViewBinding.progressBar;
            downloadIcRow = mViewBinding.downloadIcRow;
            imageRow = mViewBinding.imageRow;
        }

        void bind(final HWCommentModel item) {
//            if (isLastItem) mListner.setCurrentStatus(item.getCurrentStatus());
            txtSize.setText(item.getFileSize() == null ? "" : item.getFileSize());
            mViewBinding.dateView.setText(item.getSentOn());

//            item.setFilePath("http://mobile.scientificstudy.in/" + item.getFilePath());

            if (Utility.IsFileExists(item.getFilePath())) {
                downloadIcRow.setVisibility(View.GONE);

                Uri uri = Utility.getUriFromPath(item.getFilePath());
                if (uri != null) {
                    Glide.with(mContext).load(uri).into(image);
                }
                imageRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                if (item.getHasThumbnail()) {
                    Glide.with(mContext).load(item.getThumbnailPath()).into(image);
                } else {
                    Glide.with(mContext).load(R.drawable.default_image).into(image);
                }
                downloadIcRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                progressBar.setVisibility(View.GONE);


                                Uri uri = Utility.getUriFromPath(item.getFilePath());
                                if (uri != null) {
                                    Glide.with(mContext).load(uri).into(image);
                                }

                                imageRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new FileDownloader().DownloadAndOpen((Activity) mContext, item.getFilePath(), item.getFileExtension(), new DownloadListener() {
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
                            }
                        });
                    }
                });
            }
        }
    }

    public interface MessageInterface {
        void onClickAudio(String filePath);

        void setCurrentStatus();
    }
}