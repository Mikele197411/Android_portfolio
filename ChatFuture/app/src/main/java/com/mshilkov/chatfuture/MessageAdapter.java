package com.mshilkov.chatfuture;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, List<Message> message) {
        super(context, resource, message);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);

        }
        ImageView photoImageView=convertView.findViewById(R.id.imagePhotoView);
        TextView textView=convertView.findViewById(R.id.textMessageView);
        TextView nameTextView=convertView.findViewById(R.id.nameTextView);
        Message message=getItem(position);
        boolean isText=message.getImageUrl()==null;
        if(isText)
        {
            textView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            textView.setText(message.getText());
        }
        else
        {
            photoImageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            Glide.with(photoImageView.getContext()).load(message.getImageUrl()).into(photoImageView);
        }
        nameTextView.setText(message.getName());
        return convertView;
    }
}
