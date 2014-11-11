package com.cnbpi.widget;



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiongloo.publib.R;

public class LoadingDialog extends Dialog {
	private int Id;
	private Context context;
	private TextView message;

	public LoadingDialog(Context paramContext, int them, int layout) {
		super(paramContext, them);
		this.context = paramContext;
		this.Id = layout;
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(this.Id);
		this.message = ((TextView) findViewById(R.id.message_loading));

	}

	public void setText(String paramString) {
		this.message.setText(paramString);
	}
	
	
	
}
