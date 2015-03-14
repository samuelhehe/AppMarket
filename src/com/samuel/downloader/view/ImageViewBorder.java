package com.samuel.downloader.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewBorder extends ImageView {

	 private int color;
	 
	 private String namespace="http://mdmss.foxconn.com/appmarket/const/colors";
	 public ImageViewBorder(Context context, AttributeSet attrs) {
		 super(context, attrs);
	     color=Color.parseColor(attrs.getAttributeValue(namespace, "BorderColor"));
	}
	  
    
      @Override
      protected void onDraw(Canvas canvas) {
          
          super.onDraw(canvas);    
          //画边框  暂时去除小边框
          Rect rec=canvas.getClipBounds();
          rec.bottom--;
          rec.right--;
          Paint paint=new Paint();
          paint.setColor(color);
          paint.setStyle(Paint.Style.STROKE);
          canvas.drawRect(rec, paint);
      }
}
