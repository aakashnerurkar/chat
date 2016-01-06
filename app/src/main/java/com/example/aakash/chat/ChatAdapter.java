package com.example.aakash.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aakash on 12/7/2015.
 */
public class ChatAdapter extends BaseAdapter {



    protected List<ChatMessage> chatMessages;
    private Activity context;

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages) {

        this.context = context;
        this.chatMessages = chatMessages;
    }


    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ChatMessage chatMessage = (ChatMessage) getItem(position);
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView = vi.inflate(R.layout.list_chatmsg_layout, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();

        }

        holder.message.setText(chatMessage.getMessage());
        holder.username.setText(chatMessage.getUsername());


        return convertView;

    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.username = (TextView) v.findViewById(R.id.username);
        return holder;
    }

    public static class ViewHolder{
        TextView message;
        TextView username;
        public LinearLayout content;
        public LinearLayout contentWithBG;

    }
}
