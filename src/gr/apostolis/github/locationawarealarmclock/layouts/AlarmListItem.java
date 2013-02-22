package gr.apostolis.github.locationawarealarmclock.layouts;

import gr.apostolis.github.locationawarealarmclock.Alarm;
import gr.apostolis.github.locationawarealarmclock.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListItem extends RelativeLayout {

	private ToggleButton toggleButton;
	private TextView repeatText;
	private TextView alarmTime;

	public AlarmListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		findViews();
	}

	private void findViews() {
		alarmTime = (TextView) findViewById(R.id.alarm_trigger_time);
		repeatText = (TextView) findViewById(R.id.alarm_repeat);
		toggleButton = (ToggleButton) findViewById(R.id.alarm_is_active);
	}

	public void setAlarm(Alarm alarm) {
		alarmTime.setText(alarm.getTime());
		repeatText.setText(alarm.getRepeatString());
		toggleButton.setChecked(alarm.isActive());
		}
}
