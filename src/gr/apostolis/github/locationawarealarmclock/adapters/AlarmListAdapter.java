package gr.apostolis.github.locationawarealarmclock.adapters;

import gr.apostolis.github.locationawarealarmclock.Alarm;
import gr.apostolis.github.locationawarealarmclock.R;
import gr.apostolis.github.locationawarealarmclock.layouts.AlarmListItem;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AlarmListAdapter extends ArrayAdapter<Alarm> {

	private Context context;
	private List<Alarm> alarms;

	public AlarmListAdapter(Context context, List<Alarm> alarms) {
		super(context, android.R.layout.simple_list_item_1, alarms);
		this.context = context;
		this.alarms = alarms;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlarmListItem alarmView;
		if (null == convertView) {
			alarmView = (AlarmListItem) View.inflate(context, R.layout.alarm_list_item, null);
		} else {
			alarmView = (AlarmListItem) convertView;
		}
		alarmView.setAlarm(alarms.get(position));
		return alarmView;
	}
}
