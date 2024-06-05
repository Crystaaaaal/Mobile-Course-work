package com.example.courseworkv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.net.ContentHandler;

public class CustomToast{
    public static void showToast(Context context, String message,int iconRes,int duration){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout,null);

        ImageView imageView = layout.findViewById(R.id.imageViewToastIcon);
        TextView textView = layout.findViewById(R.id.textViewToastText);

        imageView.setImageResource(iconRes);
        textView.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
