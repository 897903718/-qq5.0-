package com.slidingmenu_demo;

import com.sildingmenu_demo.R;
import com.slidingmenu.demo.view.SlidingMenu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener {
private Button mButton;
private SlidingMenu mSlidingMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSlidingMenu=(SlidingMenu) findViewById(R.id.menu);
		mButton=(Button) findViewById(R.id.btn);
		mButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn:
			mSlidingMenu.togle();
			break;

		default:
			break;
		}
	}

	

}
