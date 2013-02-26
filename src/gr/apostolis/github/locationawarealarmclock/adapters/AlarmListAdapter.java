package gr.apostolis.github.locationawarealarmclock.adapters;

import gr.apostolis.github.locationawarealarmclock.AlarmClockApplication;
import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import gr.apostolis.github.locationawarealarmclock.layouts.AlarmListItem;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AlarmListAdapter extends ArrayAdapter<Alarm> implements
		OnCheckedChangeListener {

	private Context context;
	private AlarmClockApplication application;

	public AlarmListAdapter(Application application, Context context) {
		super(context, android.R.layout.simple_list_item_1,
				((AlarmClockApplication) application).getAlarms());
		this.application = (AlarmClockApplication) application;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlarmListItem alarmView;
		if (null == convertView) {
			alarmView = (AlarmListItem) View.inflate(context,
					R.layout.alarm_list_item, null);
		} else {
			alarmView = (AlarmListItem) convertView;
		}
		alarmView.setAlarm(application.get(position));
		alarmView.setOnCheckedChangeListener(this);
		return alarmView;
	}

	public void addAlarm(Alarm alarm) {
		application.addAlarm(alarm);
		notifyDataSetChanged();
	}

	public void removeAlarm(int id) {
		application.deleteAlarm(getItem(id).getId());
		notifyDataSetChanged();
	}

	public void undoDeleteAlarm() {
		application.undoDeleteLastAlarm();
		notifyDataSetChanged();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		AlarmListItem li = (AlarmListItem) buttonView.getParent();
		Alarm alarm = li.getAlarm();
		alarm.setActive(isChecked);
		application.saveAlarm(alarm);
	}

	public void saveAlarm(Alarm alarm) {
		application.saveAlarm(alarm);
		notifyDataSetChanged();
	}
}
