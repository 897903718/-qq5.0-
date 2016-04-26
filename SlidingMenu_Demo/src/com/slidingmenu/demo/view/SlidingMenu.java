package com.slidingmenu.demo.view;

import com.nineoldandroids.view.ViewHelper;
import com.sildingmenu_demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;// 屏幕的宽度
	private int mMenuWidth;// menu的宽度
	private int mMenuRightPadding = 50;// 菜单距屏幕右边的距离单位为dp
	private boolean once;
	private boolean isOpen;//判断当前菜单是否开启
	public SlidingMenu(Context context) {
		// TODO Auto-generated constructor stub
		this(context,null);
//		initView(context);
	}

	/**
	 * 未使用自定义属性时，自动调用该构造函数
	 * 
	 * */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 使用了自定义属性方法时，会调用此构造方法
	 * 
	 * */

	public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		//获取我们自定义的属性
		TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.SlidingMenu,defStyleAttr,0);
		int indexCount = array.getIndexCount();
		for (int i = 0; i <indexCount; i++) {
			int attr=array.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding=array.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics()));
				break;
			}
		}
		array.recycle();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}



	/**
	 * 决定内部view(子view)的宽和高，以及自己的宽和高
	 * */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (!once) {
			// 得到连的子元素
			mWapper = (LinearLayout) getChildAt(0);
			// 得到menu
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			// menu的宽度
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			// 内容区域的宽度
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/***
	 * 通过设置偏移量将menu隐藏
	 * */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	/*
	 * 
	 * 手指监听 *
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// scrollX是隐藏在左边的宽度
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen=false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen=true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		float scale=l*1.0f/mMenuWidth;//1-0
		//调用属性动画，设置TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth*scale);
		/**
		 * 内容区域1.0~0.7缩放的效果
		 * scale : 1.0~0.0 0.7 + 0.3 * scale
		 * 菜单的偏移量需要修改
		 * 菜单的显示时有缩放以及透明度变化缩放0.7 ~1.0 
		 * 1.0 - scale * 0.3
		 * 透明度 0.6 ~ 1.0 
		 * 0.6+ 0.4 * (1- scale) ;
		 */
		//设置content的缩放中心
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
		//缩放内容区域
		ViewHelper.setScaleX(mContent, 0.7f + 0.3f * scale);
		ViewHelper.setScaleY(mContent,0.7f + 0.3f * scale);
		//缩放菜单
		ViewHelper.setScaleX(mMenu,  1.0f - scale * 0.3f);
		ViewHelper.setScaleY(mMenu, 1.0f - scale * 0.3f);
		//菜单透明度
		ViewHelper.setAlpha(mMenu, 0.6f+ 0.4f * (1- scale));
	}
	
	public void openMenu(){
		if(isOpen)return;
		this.smoothScrollTo(0, 0);
		isOpen=true;
	}
	
	public void closeMenu(){
		if(!isOpen)return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen=false;
	}
	
	public void togle(){
		if(isOpen){
			closeMenu();
		}else {
			openMenu();
		}
	}
	
	public boolean isOpen(){
		return isOpen;
	}
}
