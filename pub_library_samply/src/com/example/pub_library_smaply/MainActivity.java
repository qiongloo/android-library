package com.example.pub_library_smaply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qiongloo.view.annotation.xtuils.ViewInject;

public class MainActivity extends Activity {
	
	@ViewInject(R.id.swiptList_click)
	private Button swiptList_click;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initlistiner();
	}
	
	/**
	 * 点击事件
	 */
	private void initlistiner(){
	
		/**
		 * swiptList展示
		 * @param v
		 */
		swiptList_click.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.this.startActivity(new Intent(MainActivity.this, SwiptListviewActivity.class));
			}
		});
			

	
	
	}
	
	



	
	
	
	
	
	
}
