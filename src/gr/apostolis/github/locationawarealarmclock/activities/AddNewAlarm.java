package gr.apostolis.github.locationawarealarmclock.activities;

import java.util.Arrays;

import gr.apostolis.github.locationawarealarmclock.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AddNewAlarm extends Activity implements OnClickListener, OnCheckedChangeListener {

	private static int[] daysIDList = new int[] { R.id.sun_tgl, R.id.mon_tgl,
			R.id.tue_tgl, R.id.wed_tgl, R.id.thu_tgl, R.id.fri_tgl,
			R.id.sat_tgl };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_alarm);
		Button add = (Button) findViewById(R.id.add_button);
		Button cancel = (Button) findViewById(R.id.cancel_button);
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		repeat.setOnCheckedChangeListener(this);
		add.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_new_alarm, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.add_button:
			intent.putExtra("time", getTime());
			intent.putExtra("repeatOn", getRepeatOn());
			setResult(RESULT_OK, intent);
			break;
		case R.id.cancel_button:
		default:
			setResult(RESULT_CANCELED, intent);
			break;
		}
		finish();
	}

	public String getTime() {
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
		StringBuilder sb = new StringBuilder();
		if (timePicker.getCurrentHour() < 10) {
			sb.append("0");
		}
		sb.append(timePicker.getCurrentHour());
		sb.append(":");
		if (timePicker.getCurrentMinute() < 10) {
			sb.append("0");
		}
		sb.append(timePicker.getCurrentMinute());
		Log.d(getClass().toString(), sb.toString());
		return sb.toString();
	}

	public boolean[] getRepeatOn() {
		boolean[] repeatOn = null;
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		if (repeat.isChecked()) {
			repeatOn = new boolean[7];
			ToggleButton toggle;
			for (int i = 0; i < daysIDList.length; i++) {
				toggle = (ToggleButton) findViewById(daysIDList[i]);
				repeatOn[i] = toggle.isChecked();
			}
		}

		Log.d(getClass().toString(), Arrays.toString(repeatOn));

		return repeatOn;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ToggleButton toggle;
		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setEnabled(isChecked);
		}

	}

}
