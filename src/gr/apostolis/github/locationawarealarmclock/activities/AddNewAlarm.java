package gr.apostolis.github.locationawarealarmclock.activities;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AddNewAlarm extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private static int[] daysIDList = new int[] { R.id.sun_tgl, R.id.mon_tgl,
			R.id.tue_tgl, R.id.wed_tgl, R.id.thu_tgl, R.id.fri_tgl,
			R.id.sat_tgl };
	private static String[] weekDays;
	private TimePicker timePicker;
	static {
		ArrayList<String> daysFormated = new ArrayList<String>();
		for (String shortDay : DateFormatSymbols.getInstance(
				Locale.getDefault()).getShortWeekdays()) {
			// remove first blank element
			if (0 == shortDay.length()) {
				continue;
			}
			// make sure strings are capitalized and don't contain
			// extra punctuation (see Locale.FRENCH)
			daysFormated.add(Character.toUpperCase(shortDay.charAt(0))
					+ shortDay.substring(1, shortDay.length())
							.replace('.', ' ').trim());
		}
		weekDays = daysFormated.toArray(new String[daysFormated.size()]);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_alarm);
		Button add = (Button) findViewById(R.id.add_button);
		Button cancel = (Button) findViewById(R.id.cancel_button);
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		repeat.setOnCheckedChangeListener(this);
		add.setOnClickListener(this);
		cancel.setOnClickListener(this);
		handleIntent(getIntent());
		setUpToggles();
	}

	private void setUpToggles() {
		ToggleButton toggle;
		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setText(weekDays[i]);
			toggle.setTextOn(weekDays[i]);
			toggle.setTextOff(weekDays[i]);
		}
	}

	private void handleIntent(Intent intent) {

		if (null == intent) {
			return;
		}

		Alarm alarm = (Alarm) intent.getSerializableExtra("ALARM");
		if (null == alarm) {
			return;
		}

		int hour = Integer.valueOf(alarm.getTime().split(":")[0]);
		int minutes = Integer.valueOf(alarm.getTime().split(":")[1]);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minutes);

		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		boolean[] repeatOn = alarm.getRepeatOn();

		if (null == repeatOn) {
			repeat.setChecked(false);
			return;
		}

		ToggleButton toggle;
		boolean isRepeating = false;

		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setChecked(repeatOn[i]);
			isRepeating = isRepeating | repeatOn[i];
			toggle.setText(weekDays[i]);
			toggle.setTextOn(weekDays[i]);
			toggle.setTextOff(weekDays[i]);
		}
		repeat.setChecked(isRepeating);
	}

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.add_button:
			Alarm alarm = (Alarm) intent.getSerializableExtra("ALARM");
			if (null == alarm) {
				// new alarm
				alarm = new Alarm();
			}
			alarm.setTime(getTime());
			alarm.setRepeatOn(getRepeatOn());
			intent.putExtra("ALARM", alarm);
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
