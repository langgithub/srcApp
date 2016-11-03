package com.lang.acrapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


 public class TopBar extends ViewGroup{

	private OnTopBarItemClickListener mOnItemClickListener;
	
	public void setmOnItemClickListener(OnTopBarItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}
	public TopBar(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int count=getChildCount();
        for(int i=0;i<count;i++){
        	//²âÁ¿child
        	measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int count=getChildCount();
			
		//	Log.e("outprint","TAG"+ String.valueOf(count));
			int startX=5;
			int startY=5;
			for(int i=0;i<count;i++){
				final View childView=getChildAt(i);
				childView.setVisibility(View.VISIBLE);
				int cWidth=childView.getMeasuredWidth();
				int cHeight=childView.getMeasuredHeight();
			//	Log.e("outprint","TAG"+startX+" : "+startY+" : "+cWidth+" : "+cHeight);
				childView.layout(startX,startY, startX+cWidth, startY+cHeight);
				startX+=cWidth;
				final int pos=i;
                childView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(mOnItemClickListener!=null){
							mOnItemClickListener.onClick(childView,pos);
						}
					}
				});
			}
		}
	}

	
	public interface OnTopBarItemClickListener{
		void onClick(View v,int pos);
	}
}
