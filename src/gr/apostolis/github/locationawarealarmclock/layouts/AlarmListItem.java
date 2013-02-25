package gr.apostolis.github.locationawarealarmclock.layouts;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListItem extends RelativeLayout {

	private ToggleButton toggleButton;
	private TextView repeatText;
	private TextView alarmTime;
	private Alarm alarm;

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
		this.alarm = alarm;
		alarmTime.setText(alarm.getTime());
		repeatText.setText(alarm.getRepeatString());
		toggleButton.setChecked(alarm.isActive());
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener toggleListener) {
		toggleButton.setOnCheckedChangeListener(toggleListener);
	}
}
