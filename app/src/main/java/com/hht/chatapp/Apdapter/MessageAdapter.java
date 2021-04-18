package com.hht.chatapp.Apdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hht.chatapp.MessengeActivity;
import com.hht.chatapp.Model.Chat;
import com.hht.chatapp.Model.Users;
import com.hht.chatapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final Context context;
    private final List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Chat> mChat,String imgUrl) {
        this.context = context;
        this.mChat = mChat;
        this.imageUrl = imgUrl;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {


            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
            Chat chat = mChat.get(position);
            holder.show_message.setText(chat.getMessage());
        if(imageUrl.equals("default")){
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.profile_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        return MSG_TYPE_LEFT;
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_msg);
            profile_img = itemView.findViewById(R.id.profile_image);
        }
    }
}
