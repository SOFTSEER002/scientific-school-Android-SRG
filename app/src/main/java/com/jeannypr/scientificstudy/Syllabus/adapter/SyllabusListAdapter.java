package com.jeannypr.scientificstudy.Syllabus.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Syllabus.model.SyllabusResponse;
import com.jeannypr.scientificstudy.Utilities.DownloadListener;
import com.jeannypr.scientificstudy.Utilities.FileDownloader;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowSyllabusBinding;

import java.io.File;
import java.util.ArrayList;

public class SyllabusListAdapter extends RecyclerView.Adapter {

    private ArrayList<SyllabusResponse> dataSet;
    private Context mContext;
    private OnItemClickListener listener;
    private String role;

    public SyllabusListAdapter(Context context, ArrayList<SyllabusResponse> data, OnItemClickListener listener, String roleTitle) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
        this.role = roleTitle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowSyllabusBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_syllabus,
                parent, false);
        return new SyllabusListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SyllabusResponse classworkModel = dataSet.get(position);

        ((SyllabusListAdapter.ViewHolder) holder).bind(classworkModel, listener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RowSyllabusBinding itemBinding;
        private SyllabusResponse syllabus;
        private String extension;
        View.OnClickListener mListner = this;

        ViewHolder(RowSyllabusBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final SyllabusResponse dataModel, final SyllabusListAdapter.OnItemClickListener itemListener) {
            itemBinding.setModel(dataModel);
            this.syllabus = dataModel;

            if (role.equals(Constants.Role.TEACHER))
                itemBinding.classTxt.setVisibility(View.VISIBLE);
            else itemBinding.classTxt.setVisibility(View.GONE);

            if (dataModel.getAttachment() != null && !dataModel.getAttachment().equals("")) {
                File file = new File(dataModel.getAttachment());
                Uri uri = Uri.fromFile(file);
                extension = Utility.getMimeType(mContext, uri);


                if (Utility.IsFileExists(dataModel.getAttachment())) {
                    itemBinding.downloadIc.setVisibility(View.GONE);
                    itemBinding.view.setVisibility(View.VISIBLE);
                    itemBinding.syllabusRow.setOnClickListener(mListner);
                } else {
                    itemBinding.downloadIc.setVisibility(View.VISIBLE);
                    itemBinding.view.setVisibility(View.GONE);
                }

                itemBinding.downloadIc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String finalExt = extension;

                        new FileDownloader().DownloadAndOpen((Activity) mContext, dataModel.getAttachment(), finalExt, new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                itemBinding.pb.setVisibility(View.GONE);
                                itemBinding.view.setVisibility(View.VISIBLE);
                                itemBinding.syllabusRow.setOnClickListener(mListner);
                            }

                            @Override
                            public void onDownloadStart() {
//                            itemBinding.downloadIc.setEnabled(false);
//                            itemBinding.downloadIc.setClickable(false);
                                itemBinding.pb.setVisibility(View.VISIBLE);
                                itemBinding.downloadIc.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            } else {
                itemBinding.downloadIc.setVisibility(View.GONE);
                itemBinding.view.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.syllabusRow:
                    new FileDownloader().DownloadAndOpen((Activity) mContext, syllabus.getAttachment(), extension, new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                        }

                        @Override
                        public void onDownloadStart() {
                        }
                    });
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(SyllabusResponse teacherModel);
    }
}