package gr.apostolis.github.locationawarealarmclock;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class Alarm {
	private boolean active;
	private boolean[] repeatOn;
	private String ringtone;
	private String time;
	private static String[] weekDays = DateFormatSymbols.getInstance(
			Locale.getDefault()).getShortWeekdays();

	public Alarm() {
		active = true;
		repeatOn = new boolean[7];
		for (int i = 0; i < repeatOn.length; i++) {
			repeatOn[i] = false;
		}
		ringtone = new String("");
		time = new String("");
	}

	public String getRepeatString() {
		if (null == repeatOn) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < repeatOn.length; i++) {
			if (repeatOn[i]) {
				// for some reason, the weekDays array returned by
				// getShortWeekDays contains an extra element at the beginning
				// with an empty string
				sb.append(weekDays[i + 1] + " ");
			}
		}
		return sb.toString().trim();
	}

	public long getNextTriggerInMillis() {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		Calendar now = Calendar.getInstance(Locale.getDefault());
		int hour = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);

		if (null == repeatOn) {
			if (cal.compareTo(now) > 0) {
				// alarm time is in the future, return offset
				return cal.getTimeInMillis();
			}
			// alarm time is in the past. Since we're not setting a
			// repeatable alarm, add one day and return offset
			cal.add(Calendar.DATE, 1);
			return cal.getTimeInMillis();
		}
		int day = now.get(Calendar.DAY_OF_WEEK);
		// loop in array with active alarm days for the next seven days and
		// locate the next active day
		for (int i = day; i < repeatOn.length + day; i++) {
			if (repeatOn[i % 7]) {
				break;
			}
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTimeInMillis();
	}

	public long[] getAlarmManagerEvents() {
		long[] alarms = new long[2];

		if (!active) {
			return null;
		}

		alarms[0] = getNextTriggerInMillis();
		alarms[1] = ((null == repeatOn) ? (long) 0.0
				: android.app.AlarmManager.INTERVAL_DAY);

		return alarms;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isRepeating() {
		return (null != repeatOn);
	}

	public boolean[] getRepeatOn() {
		return repeatOn;
	}

	public void setRepeatOn(boolean[] repeatOn) {
		this.repeatOn = repeatOn;
	}

	public String getRingtone() {
		return ringtone;
	}

	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Alarm [active=" + active + ", repeat=" + (null != repeatOn)
				+ ", ringtone=" + ringtone + ", time=" + time + ", repeat on="
				+ getRepeatString() + "]";
	}


}
