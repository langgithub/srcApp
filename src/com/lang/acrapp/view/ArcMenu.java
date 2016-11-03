package com.lang.acrapp.view;

import com.lang.scrapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class ArcMenu extends ViewGroup implements OnClickListener{

	/**
	 * 菜单的位置
	 */
	private Position mPostion=Position.RIGHT_BOTTOM;
	/**
	 * 圆弧半径
	 */
	private int mRadius;
	
	/**
	 * 菜单的状态
	 */
	private State state=State.OPEN;
	/**
	 * 主菜单按钮
	 */
	private View mCButton;
	/**
	 * 菜单项监听
	 */
	public OnMenuItemClickListener mOnClickItemListener;
	
	public enum State{
		OPEN,CLOSE
	}
	public enum Position{
		LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	}
	

	private static final int POS_LEFT_TOP=0;
	private static final int POS_LEFT_BOTTOM=1;
	private static final int POS_RIGHT_TOP=2;
	private static final int POS_RIGHT_BOTTOM=3;
	private State mCurrentState=State.CLOSE;

	
	public ArcMenu(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}	
	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}

	
	public void setmOnClickItemListener(OnMenuItemClickListener mOnClickItemListener) {
		this.mOnClickItemListener = mOnClickItemListener;
	}
	
	public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mRadius=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, 
				getResources().getDisplayMetrics());
		//获取自定义属性的值
		
		TypedArray a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.ArcMenu,defStyleAttr,0);
	    
		int pos=a.getInt(R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);
		
		switch (pos) {
		case POS_LEFT_TOP:
			mPostion=Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPostion=Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPostion=Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPostion=Position.RIGHT_BOTTOM;
			break;
		default:
			break;
		}
		mRadius=(int) a.getDimension(R.styleable.ArcMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, 
				getResources().getDisplayMetrics()));
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count=getChildCount();
        for(int i=0;i<count;i++){
        	//测量child
        	measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			layoutCButton();
			
			int count=getChildCount();
			for (int i = 0; i < count-1; i++) {
				View child=getChildAt(1+i);
				
				child.setVisibility(View.GONE);
				//mRadius=300;
				int cl=(int) (mRadius*Math.sin((Math.PI)/2/(count-2)*i));
				int ct=(int) (mRadius*Math.cos((Math.PI)/2/(count-2)*i));
				int cWidth=child.getMeasuredWidth();
				int cHeight=child.getMeasuredHeight();
				if(mPostion==Position.LEFT_BOTTOM||mPostion==Position.RIGHT_BOTTOM){
					ct=getMeasuredHeight()-cHeight-ct-80;
				}
				
				if(mPostion==Position.RIGHT_BOTTOM||mPostion==Position.RIGHT_TOP){
					cl=getMeasuredWidth()-cWidth-cl;
				}
				 child.layout(cl, ct, cl+cWidth, ct+cHeight);
			}
		}
		
	}
	/**
	 * 定位主菜单按钮
	 */
	private void layoutCButton() {
		mCButton=getChildAt(0);
		mCButton.setOnClickListener(this);
		
		int l=0;
		int t=0;
		
		int width=mCButton.getMeasuredWidth();
		int height=mCButton.getMeasuredHeight();
		
		
		switch (mPostion) {
			case LEFT_TOP:
				l=0;
				t=0;
				break;
	        case LEFT_BOTTOM:
				l=0;
				t=getMeasuredHeight()-height-80;
				break;
	        case RIGHT_TOP:
		        l=getMeasuredWidth()-width;
		        t=0;
		        break;
	        case RIGHT_BOTTOM:
	        	 l=getMeasuredWidth()-width;
	        	 t=getMeasuredHeight()-height;
		        break;
			default:
				break;
		}
		mCButton.layout(l, t, l+width, t+height);
	}

    /**
     * 点击子菜单项的回调接口
     * @author lang
     *
     */
	public interface OnMenuItemClickListener{
		void onClick(View v,int pos);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		rotateCButton(v,0f,360f,300);
		toggleMenu(300);
	}
	/**
	 * 切换菜单
	 */
	private void toggleMenu(int duration) {
		// TODO Auto-generated method stub
		int count=getChildCount();
		for(int i=0;i<count-1;i++){
			final View child=getChildAt(i+1);
			child.setVisibility(View.VISIBLE);
			int cl=(int) (mRadius*Math.sin((Math.PI)/2/(count-2)*i));
			int ct=(int) (mRadius*Math.cos((Math.PI)/2/(count-2)*i));
			
			int xfalg=1;
			int yfalg=1;
			if(mPostion==Position.LEFT_TOP||mPostion==Position.LEFT_BOTTOM){
				xfalg=-1;
			}
			if(mPostion==Position.LEFT_TOP||mPostion==Position.RIGHT_TOP){
				yfalg=-1;
			}
			
			AnimationSet animationset=new AnimationSet(true);
			Animation animation=null;
			if(mCurrentState==state.CLOSE){
				animation=new TranslateAnimation(xfalg*cl, 0,yfalg*ct,0);
				child.setClickable(true);
				child.setFocusable(true);
			}else{
				animation=new TranslateAnimation(0,xfalg*cl,0,yfalg*ct);
				child.setClickable(false);
				child.setFocusable(false);
			}
			animation.setFillAfter(true);
			animation.setDuration(duration);
			animation.setStartOffset((i*100)/count);
			animation.setAnimationListener(new AnimationListener() {

				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					if(mCurrentState==State.CLOSE){
						child.setVisibility(View.GONE);
					}
				}
			});
			
			//旋转动画
			RotateAnimation rotateAnimation=new RotateAnimation(0, 720,Animation.RELATIVE_TO_SELF,
					0.5f,Animation.RELATIVE_TO_SELF,0.5f);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);
			
			animationset.addAnimation(rotateAnimation);
			animationset.addAnimation(animation);
			child.startAnimation(animationset);
			
			final int pos=i+1;
			child.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mOnClickItemListener!=null){
						mOnClickItemListener.onClick(child, pos);
						System.out.println("click");
						}
						menuItemAnim(pos-1);
						ChangeStatus();
				}

			});
		}
		
		ChangeStatus();
	}
	

	private void menuItemAnim(int pos) {
		// TODO Auto-generated method stub
		for(int i=0;i<getChildCount()-1;i++){
			View childView=getChildAt(i+1);
			if(i==pos){
				childView.startAnimation(scaleBigAnim(300));
			}else{
				childView.startAnimation(scaleSmallAnim(300));
			}
			childView.setClickable(false);
			childView.setFocusable(false);
		}
	}
	/**
	 * 为当前点击的item设置变大，透明的降低的动画
	 * @return
	 */
	private Animation scaleBigAnim(int duration) {
		AnimationSet set=new AnimationSet(true);
		ScaleAnimation scaleAnim=new ScaleAnimation(1.0f, 4.0f,1.0f,4.0f,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		AlphaAnimation alpaAnim=new AlphaAnimation(1f, 0.0f);
		set.addAnimation(scaleAnim);
		set.addAnimation(alpaAnim);
		
		set.setDuration(duration);
		set.setFillAfter(true);
		return set;
	}
	private Animation scaleSmallAnim(int duration) {
		AnimationSet set=new AnimationSet(true);
		ScaleAnimation scaleAnim=new ScaleAnimation(1.0f, 0.0f,1.0f,0.0f,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		AlphaAnimation alpaAnim=new AlphaAnimation(1f, 0.0f);
		set.addAnimation(scaleAnim);
		set.addAnimation(alpaAnim);
		
		set.setDuration(duration);
		set.setFillAfter(true);
		return set;
	}
	private void ChangeStatus() {
		// TODO Auto-generated method stub
		mCurrentState=(mCurrentState==State.CLOSE?State.OPEN:state.CLOSE);
	}
	private void rotateCButton(View v, float start, float end, int duration) {
		// TODO Auto-generated method stub
		RotateAnimation animation=new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF,
				0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

	
}
