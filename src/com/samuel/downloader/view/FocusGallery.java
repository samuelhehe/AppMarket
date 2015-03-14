package com.samuel.downloader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class FocusGallery extends Gallery {

	public FocusGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	 private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){   
	     return e2.getX() > e1.getX(); 
	    }
	  @Override
	  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	    float velocityY) {
		   int kEvent;  
		   if(isScrollingLeft(e1, e2)){  
			   kEvent = KeyEvent.KEYCODE_DPAD_LEFT;  
		   }else{ 
		    	kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;   
		   }  
		   onKeyDown(kEvent, null);  
		   return true;  
	 }
	  

}
