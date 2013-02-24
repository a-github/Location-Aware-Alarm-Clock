package gr.apostolis.github.locationawarealarmclock.layouts;

import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListItem extends RelativeLayout implements AnimationListener {

	private ToggleButton toggleButton;
	private TextView repeatText;
	private TextView alarmTime;
	private Animation anim = null;

	public AlarmListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		anim = AnimationUtils.makeInAnimation(getContext(), true);
		anim.setFillAfter(true);
		anim.setAnimationListener(this);
		setVisibility(View.VISIBLE);
		startAnimation(anim);

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

	public void animateOut(boolean toRight) {

		anim = AnimationUtils.makeOutAnimation(getContext(), toRight);
		anim.setAnimationListener(this);
		startAnimation(anim);
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		anim = null;
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {
	}

}
