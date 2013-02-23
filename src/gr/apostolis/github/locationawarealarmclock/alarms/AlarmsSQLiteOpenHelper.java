package gr.apostolis.github.locationawarealarmclock.alarms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmsSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "alarms_db.sqlite";
	public static final int VERSION = 1;
    public static final String ALARMS_TABLE = "alarms";
    public static final String ALARM_ID = "id";
    public static final String ALARM_ENABLED = "enabled";
    public static final String ALARM_MESSAGE = "message";
    public static final String ALARM_TIME = "time";
    public static final String ALARM_RECURRING= "recurring";
    public static final String ALARM_RINGTONE = "ringtone";
    public static final String ALARM_LONGTITUDE = "longtitude";
    public static final String ALARM_LATITUDE = "latitude";
    public static final String ALARM_EF_RADIUS = "effective_radius";


	public AlarmsSQLiteOpenHelper(Context context) {
		super(context,DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing for now

	}

	private void createTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + ALARMS_TABLE + " ( " + 
				ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				ALARM_ENABLED + " INTEGER, " +
				ALARM_MESSAGE + " TEXT, " + 
				ALARM_TIME + " TEXT, " +
				ALARM_RECURRING + " INTEGER, " +
				ALARM_RINGTONE + " TEXT, " + 
				ALARM_LONGTITUDE + " REAL, " +
				ALARM_LATITUDE + " REAL, " +
				ALARM_EF_RADIUS + " REAL" +
				" );";
		Log.d(getClass().toString(), "Running sql: " + sql);
		db.execSQL(sql);
	}

}
