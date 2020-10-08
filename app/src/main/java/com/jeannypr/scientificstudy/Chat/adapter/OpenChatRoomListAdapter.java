package com.jeannypr.scientificstudy.Chat.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class OpenChatRoomListAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<ChatRoomModel> chatRooms, filterList;
    UserPreference userPreference;
    String schoolCode;
    UserModel userData;
    int userId;
    List<ChildModel> children;
    OnItemClickListner listner;
    //OnItemClickListener listener;

    public OpenChatRoomListAdapter(Context context, List<ChatRoomModel> openChatRooms, List<ChildModel> childrenList, OnItemClickListner clickListner) {
        super();
        mContext = context;
        chatRooms = openChatRooms;
        filterList = openChatRooms;
        userPreference = UserPreference.getInstance(context);
        schoolCode = userPreference.getSchoolCode();
        userData = userPreference.getUserData();
        userId = userData.getUserId();
        children = childrenList;
        listner = clickListner;
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat_users, parent, false);
        return new OpenChatRoomListAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ChatRoomModel model = chatRooms.get(position);
        ((MyViewHolder) holder).bind(model, listner);
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    chatRooms = filterList;

                } else {
                    ArrayList<ChatRoomModel> temp = new ArrayList<>();
                    for (ChatRoomModel row : filterList) {

                        if (row.RoomName.toLowerCase().contains(charString.toLowerCase())) {
                            temp.add(row);
                        }
                    }

                    chatRooms = temp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chatRooms;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chatRooms = (ArrayList<ChatRoomModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chat_group_name, chat_last_message, chat_last_date, firstChar;
        Button chat_group_unread_message;
        ConstraintLayout rowChatUsers;
        ImageView circleImageView;

        MyViewHolder(View view) {
            super(view);
            rowChatUsers = view.findViewById(R.id.rowChatUsers);
            chat_group_name = view.findViewById(R.id.chat_group_name);
            chat_last_message = view.findViewById(R.id.chat_last_message);
            chat_last_date = view.findViewById(R.id.chat_last_date);
            chat_group_unread_message = view.findViewById(R.id.chat_group_unread_message);
            firstChar = view.findViewById(R.id.firstLetter);
            circleImageView = view.findViewById(R.id.circleImageView);

        }


        void bind(final ChatRoomModel chatRoom, final OpenChatRoomListAdapter.OnItemClickListner listner) {
            if (chatRoom.RoomName != null || !chatRoom.RoomName.equals("")) {
                SetImg(chatRoom.RoomName.charAt(0));
            } else {
                Log.i("", "");
            }
            // SetImg(chatRoom.RoomName.charAt(0));

            if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {

                if (chatRoom.Type.equals(Constants.ChatRoomType.INDIVIDUAL)) {
                    chatRoom.RoomName = (chatRoom.CreatedBy != userId ? chatRoom.OwnerName : chatRoom.RoomName);
                } else {
                    for (ChildModel child : children) {
                        if (child.ClassId == chatRoom.ClassId) {
                            chatRoom.RoomName = chatRoom.RoomName + "-" + child.Name;
                            break;
                        }
                    }
                }
            } else {
                chatRoom.RoomName = chatRoom.Type.equals(Constants.ChatRoomType.INDIVIDUAL) ?
                        (chatRoom.CreatedBy != userId ? chatRoom.OwnerName : chatRoom.RoomName) : chatRoom.RoomName;
            }

            chat_group_name.setText(chatRoom.RoomName != null ? chatRoom.RoomName.substring(0, 1).toUpperCase() + chatRoom.RoomName.substring(1).toLowerCase() : "");
            if (!chatRoom.getLastUserName().equals("")) {
                chat_last_message.setText(chatRoom.getLastUserName().concat(": ").concat(chatRoom.getLastMessage()));
            }
            chat_last_date.setText(Utility.GetChatTime(chatRoom.LastMessageTimeStamp));

            rowChatUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.OnRoomClick(chatRoom);
                }
            });
        }

        private void SetImg(char firstLetter) {
            firstChar.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = circleImageView.getBackground();
            int colorId = Utility.GetRandomMaterialColor("materialColor", mContext);

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(colorId);

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(colorId);

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(colorId);
            }
        }
    }

    public interface OnItemClickListner {
        void OnRoomClick(ChatRoomModel model);
    }

}



