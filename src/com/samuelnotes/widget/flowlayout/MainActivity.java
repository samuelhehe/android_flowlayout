package com.samuelnotes.widget.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String vals[] = {

	"Android", "delphi", "Java", "Hello", "Hi", "Welcome", "C#", "励志",
			 "宅男", "开心果", "飞一般的大侠","奋斗少年", "观世音菩萨", "小龙女 ", "黄药师", "编程神侠",
			"黑白无常", "孙悟空 ", "镇元子", "准提道人", "女娲", "鸿钧老祖", "元始天尊", "接引道人", };

	private FlowLayout aty_mflowlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		aty_mflowlayout = (FlowLayout) this.findViewById(R.id.aty_mflowlayout);
		initAddView();
	}

	private void initAddView() {

		
		MarginLayoutParams lp = new MarginLayoutParams(
				MarginLayoutParams.WRAP_CONTENT,
				MarginLayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 5, 5);
		for (int i = 0; i < vals.length; i++) {

			TextView tv = new TextView(this);
			tv.setLayoutParams(lp);
			tv.setText(vals[i]);
			tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_background));
			tv.setTextColor(getResources().getColor(
					android.R.color.holo_green_light));
			if(i==5){
				tv.setTextSize(20f);
				tv.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
			}
			
			if(i==8){
				tv.setTextColor(getResources().getColor(android.R.color.holo_red_light));
			}
			if(i==16){
				tv.setTextSize(10f);
				tv.setTextColor(getResources().getColor(android.R.color.background_dark));
			}
			aty_mflowlayout.addView(tv);
		}
	}

}