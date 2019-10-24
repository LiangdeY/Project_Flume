package comp5216.sydney.edu.au.project_flume.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import comp5216.sydney.edu.au.project_flume.Model.Chat;
import comp5216.sydney.edu.au.project_flume.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String targetUserGender, userGender;


    FirebaseUser user;

    public MessageAdapter(Context mContext, List<Chat> mChat, String targetUserGender, String userGender) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.userGender = userGender;
        this.targetUserGender = targetUserGender;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup,
                                                         int viewType) {
        if(viewType == MESSAGE_TYPE_RIGHT){
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup,
                    false);
            return new MessageAdapter.ViewHolder(v, userGender);
        }else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat__item_left, viewGroup,
                    false);
            return new MessageAdapter.ViewHolder(v, targetUserGender);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat  = mChat.get(position);
        holder.message.setText(chat.getMessage());

        if(holder.gender.equals("Male")){
            holder.user_image.setImageResource(R.drawable.avatar_male);
        }
        else{
            holder.user_image.setImageResource(R.drawable.avatar_female);
        }


        if(chat.getIsSeen()){
            holder.msgSeen.setText("Seen");
        }else{
            holder.msgSeen.setText("Sent");
        }
        Log.d("MessageAdapter", "message = "  + chat.getMessage() + " isSeen = " + chat.getIsSeen());


    }

    @Override
    public int getItemCount(){
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView message;
        public ImageView user_image;
        public TextView msgSeen;
        public String gender;


        public ViewHolder(View itemView, String gender) {
            super(itemView);
            message = itemView.findViewById(R.id.show_message);
            user_image = itemView.findViewById(R.id.user_image_chatItem);
            msgSeen = itemView.findViewById(R.id.seen_chat_item);
            this.gender = gender;
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if( mChat.get(position).getSender().equals(user.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }
        else{
            return MESSAGE_TYPE_LEFT;
        }
    }


}
