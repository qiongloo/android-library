package com.example.pub_library_smaply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/**
	 * swiptList点击事件
	 * @param v
	 */
	public void handleSample1(View v) {
		startActivity(new Intent(this, SwiptListviewActivity.class));
	}


	
	
	
	
	
	
}
