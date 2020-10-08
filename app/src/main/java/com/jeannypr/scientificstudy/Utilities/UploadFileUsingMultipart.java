package com.jeannypr.scientificstudy.Utilities;

import android.content.Context;
import android.os.AsyncTask;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.SptWall.api.SptWallService;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Utilities.UploadListner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFileUsingMultipart {

    public void UploadFile(String fileDataType, File file, int savedActivityId,
                           String activityType, int activityTypeInInt, Context context, UploadListner listner) {
        new UploadFileTask(fileDataType, file, savedActivityId, activityTypeInInt, activityType, context, listner).execute();
    }

    private class UploadFileTask extends AsyncTask<Void, Integer, Integer> {
        int savedActivityId, activityTypeId;
        UploadListner listner;
        File file;
        UserPreference userPrefer;
        UserModel userModel;
        Context context;
        CwHwService service;
        SptWallService sptWallService;
        StudentService studentService;
        String fileDataType, activityType;
        Integer response;


        @Override
        protected void onPreExecute() {
            listner.onUploadStart();
        }


        public UploadFileTask(String fileDataType, File file, int savedActivityId, int activityTypeInInt, String activityType, Context context, UploadListner listner) {
            this.file = file;
            this.savedActivityId = savedActivityId;
            this.activityTypeId = activityTypeInInt;
            this.context = context;
            this.listner = listner;
            this.fileDataType = fileDataType;
            this.activityType = activityType;

            userPrefer = UserPreference.getInstance(context);
            userModel = userPrefer.getUserData();
            service = new DataRepo<>(CwHwService.class, context).getService();
            sptWallService = new DataRepo<>(SptWallService.class, context).getService();
            studentService = new DataRepo<>(StudentService.class, context).getService();

        }

        @Override
        protected Integer doInBackground(Void... strings) {

            try {
                RequestBody reqFile = RequestBody.create(MediaType.parse(fileDataType), file);
                String fname = file.getName();
                MultipartBody.Part body = MultipartBody.Part.createFormData(fname, file.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());


                switch (activityType.toLowerCase()) {
                    case "classwork":
                    case "homework":
                        /*service.postImage(userModel.getSchoolId(), savedActivityId, activityTypeId, body, name).enqueue(new Callback<Bean>() {
                            @Override
                            public void onResponse(Call<Bean> call, Response<Bean> res) {
                                if (res.body() != null) {
                                    if (res.body().rcode == Constants.Rcode.OK) {
                                        response = Constants.UploadAttachmentRcode.OK;
                                    }
                                } else {
                                    response = Constants.UploadAttachmentRcode.NULL;
                                }
                            }

                            @Override
                            public void onFailure(Call<Bean> call, Throwable t) {
                                response = Constants.UploadAttachmentRcode.SERVER_ERROR;
                            }
                        });*/
                        break;

                    case Constants.PostType.NEWS:
                    case Constants.PostType.NOTICE:

                        if (activityType.toLowerCase().equals(Constants.PostType.NEWS)) {
                            activityType = "News";
                        } else if (activityType.toLowerCase().equals(Constants.PostType.NOTICE)) {
                            activityType = "Notice";
                        }

                        sptWallService.UploadNewsAttachment(userModel.getSchoolId(), savedActivityId, activityType, body, name).enqueue(new Callback<Bean>() {
                            @Override
                            public void onResponse(Call<Bean> call, Response<Bean> res) {
                                if (res.body() != null) {
                                    if (res.body().rcode == Constants.Rcode.OK) {
                                        response = Constants.UploadAttachmentRcode.OK;
                                    }
                                } else {
                                    response = Constants.UploadAttachmentRcode.NULL;
                                }

                            }

                            @Override
                            public void onFailure(Call<Bean> call, Throwable t) {
                                response = Constants.UploadAttachmentRcode.SERVER_ERROR;
                            }
                        });
                        break;

                    case Constants.PostType.EVENT:
                        if (activityType.toLowerCase().equals(Constants.PostType.EVENT)) {
                            activityType = "Event";
                        }

                        sptWallService.UploadEventAttachment(userModel.getSchoolId(), savedActivityId, activityType, body, name).enqueue(new Callback<Bean>() {
                            @Override
                            public void onResponse(Call<Bean> call, Response<Bean> res) {
                                if (res.body() != null) {
                                    if (res.body().rcode == Constants.Rcode.OK) {
                                        response = Constants.UploadAttachmentRcode.OK;
                                    }
                                } else {
                                    response = Constants.UploadAttachmentRcode.NULL;
                                }

                            }

                            @Override
                            public void onFailure(Call<Bean> call, Throwable t) {
                                response = Constants.UploadAttachmentRcode.SERVER_ERROR;
                            }
                        });
                        break;

                    case "studentprofile":
                        int studentId = savedActivityId;
                        int schoolId = activityTypeId;

                        studentService.UploadStudentPic(studentId, schoolId, body, name).enqueue(new Callback<Bean>() {
                            @Override
                            public void onResponse(Call<Bean> call, Response<Bean> res) {
                                if (res.body() != null) {
                                    if (res.body().rcode == Constants.Rcode.OK) {
                                        response = Constants.UploadAttachmentRcode.OK;
                                    }
                                } else {
                                    response = Constants.UploadAttachmentRcode.NULL;
                                }
                            }

                            @Override
                            public void onFailure(Call<Bean> call, Throwable t) {
                                response = Constants.UploadAttachmentRcode.SERVER_ERROR;
                            }
                        });
                        break;

                    case "teacherprofile":
                        int teacherId = savedActivityId;
                        int sclId = activityTypeId;

                        studentService.UploadTeacherPic(sclId, teacherId, body, name).enqueue(new Callback<Bean>() {
                            @Override
                            public void onResponse(Call<Bean> call, Response<Bean> res) {
                                if (res.body() != null) {

                                    if (res.body().rcode == Constants.Rcode.OK) {
                                        response = Constants.UploadAttachmentRcode.OK;
                                    }
                                } else {
                                    response = Constants.UploadAttachmentRcode.NULL;
                                }
                            }

                            @Override
                            public void onFailure(Call<Bean> call, Throwable t) {
                                response = Constants.UploadAttachmentRcode.SERVER_ERROR;
                            }
                        });
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = Constants.UploadAttachmentRcode.EXCEPTION;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer response) {
            listner.onUploadComplete(response);
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
