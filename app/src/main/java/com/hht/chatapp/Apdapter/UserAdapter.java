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
import com.hht.chatapp.MessengeActivity;
import com.hht.chatapp.Model.Users;
import com.hht.chatapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<Users> mUsers;

    public UserAdapter(Context context, List<Users> users) {
        this.context = context;
        this.mUsers = users;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());

        if(users.getImageUrl().equals("default")){
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context)
                    .load(users.getImageUrl())
                    .into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessengeActivity.class);
                i.putExtra("userid",users.getId());
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usnTw);
            imageView = itemView.findViewById(R.id.avatar);
        }
    }
}
