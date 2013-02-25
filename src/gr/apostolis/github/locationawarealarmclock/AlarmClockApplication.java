package gr.apostolis.github.locationawarealarmclock;

import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARMS_TABLE;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_EF_RADIUS;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_ENABLED;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_ID;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_LATITUDE;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_LONGTITUDE;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_MESSAGE;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_RECURRING;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_RINGTONE;
import static gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper.ALARM_TIME;
import gr.apostolis.github.locationawarealarmclock.alarms.Alarm;
import gr.apostolis.github.locationawarealarmclock.alarms.AlarmsSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmClockApplication extends Application {

	private List<Alarm> alarms = null;
	private SQLiteDatabase db;
	private Alarm lastRemovedAlarm;

	@Override
	public void onCreate() {
		super.onCreate();
		AlarmsSQLiteOpenHelper helper = new AlarmsSQLiteOpenHelper(this);
		db = helper.getWritableDatabase();
		if (null == alarms) {
			loadAlarms();
		}
	}

	private void loadAlarms() {
		alarms = Collections.synchronizedList(new ArrayList<Alarm>());
		Cursor alarmsCursor = db.query(ALARMS_TABLE, new String[] { ALARM_ID,
				ALARM_ENABLED, ALARM_TIME, ALARM_MESSAGE, ALARM_RECURRING,
				ALARM_RINGTONE, ALARM_LONGTITUDE, ALARM_LATITUDE,
				ALARM_EF_RADIUS }, null, null, null, null, ALARM_TIME);

		alarmsCursor.moveToFirst();
		Alarm alarm = null;

		if (!alarmsCursor.isAfterLast()) {
			do {
				long id = alarmsCursor.getLong(0);
				boolean enabled = Boolean.parseBoolean(alarmsCursor
						.getString(1));
				String time = alarmsCursor.getString(2);
				String message = alarmsCursor.getString(3);
				long recurring = alarmsCursor.getLong(4);
				String ringtone = alarmsCursor.getString(5);
				double longtitude = alarmsCursor.getDouble(6);
				double latitude = alarmsCursor.getDouble(7);
				double effectiveRadius = alarmsCursor.getDouble(8);
				boolean[] repeatOn = getArrayRecurringDaysFromLong(recurring);

				alarm = new Alarm();
				alarm.setId(id);
				alarm.setActive(enabled);
				alarm.setMessage(message);
				alarm.setRingtone(ringtone);
				alarm.setTime(time);
				alarm.setRepeatOn(repeatOn);
				alarm.setLatitude(latitude);
				alarm.setLongtitude(longtitude);
				alarm.setEffectiveRadius(effectiveRadius);
				synchronized (alarms) {
					alarms.add(alarm);
				}

			} while (alarmsCursor.moveToNext());
		}
		alarmsCursor.close();
	}

	public void addAlarm(Alarm alarm) {
		assert (null != alarm);

		ContentValues values = new ContentValues();

		values.put(ALARM_ENABLED, alarm.isActive() ? 1 : 0);
		values.put(ALARM_TIME, alarm.getTime());
		values.put(ALARM_MESSAGE, alarm.getMessage());
		values.put(ALARM_RECURRING,
				getLongRecurringDaysFromArray(alarm.getRepeatOn()));
		values.put(ALARM_RINGTONE, alarm.getRingtone());
		values.put(ALARM_LONGTITUDE, alarm.getLongtitude());
		values.put(ALARM_LATITUDE, alarm.getLatitude());
		values.put(ALARM_EF_RADIUS, alarm.getEffectiveRadius());

		alarm.setId(db.insert(ALARMS_TABLE, null, values));

		synchronized (alarms) {
			alarms.add(alarm);
		}
	}

	public void saveAlarm(Alarm alarm) {
		assert (null != alarm);

		ContentValues values = new ContentValues();

		values.put(ALARM_ENABLED, alarm.isActive() ? 1 : 0);
		values.put(ALARM_TIME, alarm.getTime());
		values.put(ALARM_MESSAGE, alarm.getMessage());
		values.put(ALARM_RECURRING,
				getLongRecurringDaysFromArray(alarm.getRepeatOn()));
		values.put(ALARM_RINGTONE, alarm.getRingtone());
		values.put(ALARM_LONGTITUDE, alarm.getLongtitude());
		values.put(ALARM_LATITUDE, alarm.getLatitude());
		values.put(ALARM_EF_RADIUS, alarm.getEffectiveRadius());

		long id = alarm.getId();
		String where = String.format("%s = ?", ALARM_ID);
		db.update(ALARMS_TABLE, values, where, new String[] { id + "" });
	}

	public void deleteAlarms(long[] ids) {

		StringBuffer idList = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			idList.append(ids[i]);
			if (i < ids.length - 1) {
				idList.append(",");
			}
		}

		String where = String.format("%s in (%s)", ALARM_ID, idList);
		db.delete(ALARMS_TABLE, where, null);

		// Also remove selected alarms from ArrayList
		List<Long> idArrayList = new ArrayList<Long>(ids.length);
		for (long id : ids) {
			idArrayList.add(id);
		}

		synchronized (alarms) {
			Iterator<Alarm> i = alarms.iterator();
			while (i.hasNext()) {
				lastRemovedAlarm = i.next();
				if (idArrayList.contains(lastRemovedAlarm.getId())) {
					alarms.remove(lastRemovedAlarm);
					break;
				}
			}
		}

	}

	public void undoDeleteLastAlarm() {
		if (null == lastRemovedAlarm) {
			return;
		}

		addAlarm(lastRemovedAlarm);
		lastRemovedAlarm = null;
	}

	public Alarm get(int position) {
		Alarm ret;
		synchronized (alarms) {
			ret = alarms.get(position);
		}
		return ret;
	}

	public void deleteAlarm(long id) {
		deleteAlarms(new long[] { id });
	}

	private long getLongRecurringDaysFromArray(boolean[] recurringArray) {
		long recurring = 0;
		if (null == recurringArray) {
			return recurring;
		}
		for (int day = 0; day < 7; day++) {
			int val = recurringArray[day] ? 1 : 0;
			recurring = recurring | (val << day);
		}
		return recurring;
	}

	private boolean[] getArrayRecurringDaysFromLong(long recurring) {
		boolean[] recurringArray = new boolean[7];

		for (int day = 0; day < 7; day++) {
			recurringArray[day] = (((recurring & (1 << day)) > 0) ? true
					: false);
		}
		return recurringArray;
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

}
