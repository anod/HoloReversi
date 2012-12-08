package com.example.holoreversi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = (Context)this;
		
		Button start = (Button)findViewById(R.id.buttonStart);
		start.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BoardActivity.class);
				startActivity(intent);
			}
		});
	}


}
