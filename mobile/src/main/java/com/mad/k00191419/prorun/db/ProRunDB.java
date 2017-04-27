package com.mad.k00191419.prorun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ProRunDB {

    private static final String APP_NAME = "__PRO_RUN";
    // database constants
    public static final String DB_NAME = "prorun.db";
    public static final int DB_VERSION = 1;

    // locations table constants
    public static final String LOCATIONS_TBL = "locations";

    public static final String LOCATION_ID = "_id";
    public static final int LOCATIN_ID_COL = 0;

    public static final String LOCATION_RUN_NO = "run_no";
    public static final int LOCATION_RUN_NO_COL = 1;

    public static final String LOCATION_LATITUDE = "latitude";
    public static final int LOCATION_LATITUDE_COL = 2;

    public static final String LOCATION_LONGITUDE = "longitude";
    public static final int LOCATION_LONGITUDE_COL = 3;

    public static final String LOCATION_TIME = "time";
    public static final int LOCATION_TIME_COL = 4;

    // runs table constants
    public static final String RUNS_TBL = "runs";

    public static final String RUN_NO = "_run_no";
    public static final int RUN_NO_COL = 0;

    public static final String RUN_START_DATE = "start_date";
    public static final int RUN_START_DATE_COL = 1;

    public static final String RUN_TOTAL_TIME = "time";
    public static final int RUN_TOTAL_TIME_COL = 2;

    public static final String RUN_TOTAL_DISTANCE = "distance";
    public static final int RUN_TOTAL_DISTANCE_COL = 3;

    public static final String RUN_TOTAL_CALORIES = "calories";
    public static final int RUN_TOTAL_CALORIES_COL = 4;

    public static final String RUN_AVG_SPEED = "average_speed";
    public static final int RUN_AVG_SPEED_COL = 5;

    // daily goals table constants
    public static final String DAILY_GOALS_TBL = "daily_goals";

    public static final String DAILY_GOAL_ID = "_id";
    public static final int DAILY_GOAL_ID_COL = 0;

    public static final String DAILY_DUE_DATE = "due_date";
    public static final int DAILY_DUE_DATE_COL = 1;

    public static final String DAILY_DISTANCE_GOAL = "distance_goal";
    public static final int DAILY_DISTANCE_GOAL_COL = 2;

    public static final String DAILY_DISTANCE_CURRENT = "distance_current";
    public static final int DAILY_DISTANCE_CURRENT_COL = 3;

    public static final String DAILY_CALORIES_GOAL = "calories_goal";
    public static final int DAILY_CALORIES_GOAL_COL = 4;

    public static final String DAILY_CALORIES_CURRENT = "calories_current";
    public static final int DAILY_CALORIES_CURRENT_COL = 5;

    // weekly goals table constants
    public static final String WEEKLY_GOALS_TBL = "weekly_goals";

    public static final String WEEKLY_GOAL_ID = "_id";
    public static final int WEEKLY_GOAL_ID_COL = 0;

    public static final String WEEKLY_DUE_DATE = "due_date";
    public static final int WEEKLY_DUE_DATE_COL = 1;

    public static final String WEEKLY_DISTANCE_GOAL = "distance_goal";
    public static final int WEEKLY_DISTANCE_GOAL_COL = 2;

    public static final String WEEKLY_DISTANCE_CURRENT = "distance_current";
    public static final int WEEKLY_DISTANCE_CURRENT_COL = 3;

    public static final String WEEKLY_CALORIES_GOAL = "calories_goal";
    public static final int WEEKLY_CALORIES_GOAL_COL = 4;

    public static final String WEEKLY_CALORIES_CURRENT = "calories_current";
    public static final int WEEKLY_CALORIES_CURRENT_COL = 5;

    // monthly goals table constants
    public static final String MONTHLY_GOALS_TBL = "monthly_goals";

    public static final String MONTHLY_GOAL_ID = "_id";
    public static final int MONTHLY_GOAL_ID_COL = 0;

    public static final String MONTHLY_DUE_DATE = "due_date";
    public static final int MONTHLY_DUE_DATE_COL = 1;

    public static final String MONTHLY_DISTANCE_GOAL = "distance_goal";
    public static final int MONTHLY_DISTANCE_GOAL_COL = 2;

    public static final String MONTHLY_DISTANCE_CURRENT = "distance_current";
    public static final int MONTHLY_DISTANCE_CURRENT_COL = 3;

    public static final String MONTHLY_CALORIES_GOAL = "calories_goal";
    public static final int MONTHLY_CALORIES_GOAL_COL = 4;

    public static final String MONTHLY_CALORIES_CURRENT = "calories_current";
    public static final int MONTHLY_CALORIES_CURRENT_COL = 5;

    // CREATE TABLE statements
    public static final String CREATE_LOCATIONS_TABLE =
            "CREATE TABLE " + LOCATIONS_TBL + " (" +
                    LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCATION_RUN_NO + " TEXT, " +
                    LOCATION_LATITUDE + " REAL, " +
                    LOCATION_LONGITUDE + " REAL, " +
                    LOCATION_TIME + " INTEGER)";

    public static final String CREATE_RUNS_TABLE =
            "CREATE TABLE " + RUNS_TBL + " (" +
                    RUN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RUN_START_DATE + " INTEGER, " +
                    RUN_TOTAL_TIME + " INTEGER, " +
                    RUN_TOTAL_DISTANCE + " REAL, " +
                    RUN_TOTAL_CALORIES + " INTEGER, " +
                    RUN_AVG_SPEED + " REAL)";

    public static final String CREATE_DAILY_GOALS_TABLE =
            "CREATE TABLE " + DAILY_GOALS_TBL + " (" +
                    DAILY_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DAILY_DUE_DATE + " INTEGER, " +
                    DAILY_DISTANCE_GOAL + " REAL, " +
                    DAILY_DISTANCE_CURRENT + " REAL, " +
                    DAILY_CALORIES_GOAL + " INTEGER, " +
                    DAILY_CALORIES_CURRENT + " INTEGER)";


    public static final String CREATE_WEEKLY_GOALS_TABLE =
            "CREATE TABLE " + WEEKLY_GOALS_TBL + " (" +
                    WEEKLY_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WEEKLY_DUE_DATE + " INTEGER, " +
                    WEEKLY_DISTANCE_GOAL + " REAL, " +
                    WEEKLY_DISTANCE_CURRENT + " REAL, " +
                    WEEKLY_CALORIES_GOAL + " INTEGER, " +
                    WEEKLY_CALORIES_CURRENT + " INTEGER)";

    public static final String CREATE_MONTHLY_GOALS_TABLE =
            "CREATE TABLE " + MONTHLY_GOALS_TBL + " (" +
                    MONTHLY_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MONTHLY_DUE_DATE + " INTEGER, " +
                    MONTHLY_DISTANCE_GOAL + " REAL, " +
                    MONTHLY_DISTANCE_CURRENT + " REAL, " +
                    MONTHLY_CALORIES_GOAL + " INTEGER, " +
                    MONTHLY_CALORIES_CURRENT + " INTEGER)";


    public static final String DROP_LOCATIONS_TABLE =
            "DROP TABLE IF EXISTS " + LOCATIONS_TBL;

    public static final String DROP_RUNS_TABLE =
            "DROP TABLE IF EXISTS " + RUNS_TBL;

    public static final String DROP_DAILY_GOALS_TABLE =
            "DROP TABLE IF EXISTS " + DAILY_GOALS_TBL;

    public static final String DROP_WEEKLY_GOALS_TABLE =
            "DROP TABLE IF EXISTS " + WEEKLY_GOALS_TBL;

    public static final String DROP_MONTHLY_GOALS_TABLE =
            "DROP TABLE IF EXISTS " + MONTHLY_GOALS_TBL;

    // Clear table SQL
    public static final String DELETE_FROM_RUNS =
            "DELETE FROM " + RUNS_TBL;

    public void clearRuns() {
        openWriteableDB();
        db.execSQL(DELETE_FROM_RUNS);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_RUNS_TABLE);
            db.execSQL(CREATE_LOCATIONS_TABLE);
            db.execSQL(CREATE_DAILY_GOALS_TABLE);
            db.execSQL(CREATE_WEEKLY_GOALS_TABLE);
            db.execSQL(CREATE_MONTHLY_GOALS_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("__PLAYER_APP", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            Log.d("__PLAYER_APP", "Deleting all data!");
            db.execSQL(ProRunDB.DROP_DAILY_GOALS_TABLE);
            db.execSQL(ProRunDB.DROP_LOCATIONS_TABLE);
            db.execSQL(ProRunDB.DROP_MONTHLY_GOALS_TABLE);
            db.execSQL(ProRunDB.DROP_RUNS_TABLE);
            db.execSQL(ProRunDB.DROP_WEEKLY_GOALS_TABLE);
            onCreate(db);
        }
    }

    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public ProRunDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<Run> getRuns() {

        this.openReadableDB();
        Cursor cursor = db.query(RUNS_TBL, null,
                null, null,
                null, null, null);
        // create empty runs list
        ArrayList<Run> runs = new ArrayList<Run>();

        // populate runs list with data from runs table
        while (cursor.moveToNext()) {
            runs.add(getRunFromCursor(cursor));
        }

        // populate each run's locations
        for (Run run : runs) {
            // move cursor to locations table and get those for that run
            long runNo = run.getNo();
            cursor = db.query(LOCATIONS_TBL,
                    null,
                    LOCATION_RUN_NO + "=?",   // where RUN is run.getNo()
                    new String[]{runNo + ""},
                    null, null, null, null);

            for (Location loc : getLocationsForRun(runNo, cursor)) {
                run.addLocation(loc);
            }
        }
        if (cursor != null)
            cursor.close();

        this.closeDB();
        Log.d(APP_NAME, "Retrieved " + runs.size() + " runs from " + RUNS_TBL);

        Collections.reverse(runs);
        return runs;
    }

    public Run getRun(long runNo){

        ArrayList<Run> ls = getRuns();
        for(Run r : ls){
            if(r.getNo() == runNo){
                return r;
            }
        }
        return null;
    }

    private ArrayList<Location> getLocationsForRun(long runNo, Cursor cursor) {

        ArrayList<Location> list = new ArrayList<>();

        // populate loactions list with data from locations table
        while (cursor.moveToNext()) {
            list.add(getLocationFromCursor(cursor));
        }
        return list;
    }

    private Location getLocationFromCursor(Cursor cursor) {

        Log.d(APP_NAME, "getting LOCATION from cursor..");

        if (cursor == null || cursor.getCount() == 0) {
            Log.d(APP_NAME, "cursor EMPTY!");
            return null;
        } else {
            try {
                Location loc = new Location("GPS");
                loc.setLatitude(cursor.getDouble(LOCATION_LATITUDE_COL));
                loc.setLongitude(cursor.getDouble(LOCATION_LONGITUDE_COL));
                loc.setTime(cursor.getInt(LOCATION_TIME_COL));
                return loc;
            } catch (Exception e) {
                Log.d(APP_NAME, "exception when creating LOCATION!:" + e.getMessage());
                return null;
            }
        }
    }

    private static Run getRunFromCursor(Cursor cursor) {

        Log.d(APP_NAME, "getting RUN from cursor..");

        if (cursor == null || cursor.getCount() == 0) {
            Log.d(APP_NAME, "cursor EMPTY!");
            return null;
        } else {
            try {
                Run r = new Run(
                        cursor.getLong(RUN_NO_COL),
                        cursor.getLong(RUN_START_DATE_COL),
                        cursor.getLong(RUN_TOTAL_TIME_COL),
                        cursor.getFloat(RUN_TOTAL_DISTANCE_COL),
                        cursor.getLong(RUN_TOTAL_CALORIES_COL),
                        cursor.getDouble(RUN_AVG_SPEED_COL));
                return r;
            } catch (Exception e) {
                Log.d(APP_NAME, "exception when creating RUN!:" + e.getMessage());
                return null;
            }
        }
    }

    private long writeRun(Run r) {
        ContentValues cv = new ContentValues();
        cv.put(RUN_NO, r.getNo());
        cv.put(RUN_START_DATE, r.getStartDate());
        cv.put(RUN_AVG_SPEED, r.getAvgSpeed());
        cv.put(RUN_TOTAL_CALORIES, r.getTotalCalories());
        cv.put(RUN_TOTAL_DISTANCE, r.getTotalDistance());
        cv.put(RUN_TOTAL_TIME, r.getTotalTime());

        Log.d(APP_NAME, "Inserting RUN to DB: \n" + r.toString());

        this.openWriteableDB();
        long rowID = db.insert(RUNS_TBL, null, cv);
        this.closeDB();

        return rowID;
    }

    private long insertLocation(Location l, long runNo) {
        ContentValues cv = new ContentValues();
        cv.put(LOCATION_LATITUDE, l.getLatitude());
        cv.put(LOCATION_LONGITUDE, l.getLongitude());
        cv.put(LOCATION_TIME, l.getTime());
        cv.put(LOCATION_RUN_NO, runNo);

        Log.d(APP_NAME, "Inserting Location to DB: \n" + l.toString());

        this.openWriteableDB();
        long rowID = db.insert(LOCATIONS_TBL, null, cv);
        this.closeDB();

        return rowID;
    }

    public long insertRun(Run r) {
        long rowId = writeRun(r);

        for (Location l : r.getLocations()) {
            insertLocation(l, r.getNo());
        }

        return rowId;
    }

    public long getNextRunNo() {
        this.openReadableDB();
        Cursor cursor = db.query(RUNS_TBL, new String[]{RUN_NO},
                null, null,
                null, null, RUN_NO + " DESC", "1");
        cursor.moveToFirst();
        long no = 1;
        if (cursor.getCount() > 0) {
            no = cursor.getLong(0) + 1;
        }
        if (cursor != null)
            cursor.close();

        this.closeDB();
        return no;
    }

    public Goal getWeeklyGoal() {

        Log.d(APP_NAME, "getting Weekly goal..");

        this.openReadableDB();
        Cursor cursor = db.query(WEEKLY_GOALS_TBL, new String[]{
                        WEEKLY_GOAL_ID,
                        WEEKLY_DUE_DATE,
                        WEEKLY_DISTANCE_GOAL,
                        WEEKLY_DISTANCE_CURRENT,
                        WEEKLY_CALORIES_GOAL,
                        WEEKLY_CALORIES_CURRENT
                },
                null, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Goal g = new Goal(Goal.Type.WEEKLY,
                    1,
                    Calendar.getInstance().getTimeInMillis(),
                    0, 0, 0, 0);
            writeWeeklyGoal(g);
            return g;
        }
        try {
            Goal g = new Goal(
                    Goal.Type.WEEKLY,
                    cursor.getInt(WEEKLY_GOAL_ID_COL),
                    cursor.getLong(WEEKLY_DUE_DATE_COL),
                    cursor.getDouble(WEEKLY_DISTANCE_GOAL_COL),
                    cursor.getDouble(WEEKLY_DISTANCE_CURRENT_COL),
                    cursor.getLong(WEEKLY_CALORIES_GOAL_COL),
                    cursor.getLong(WEEKLY_CALORIES_CURRENT_COL));
            return g;
        } catch (Exception e) {
            Log.d(APP_NAME, "exception when creating weekly GOAL!:" + e.getMessage());
            return null;
        }

    }

    public Goal getMonthlyGoal() {

        Log.d(APP_NAME, "getting Monthly goal..");

        this.openReadableDB();
        Cursor cursor = db.query(MONTHLY_GOALS_TBL, new String[]{
                        MONTHLY_GOAL_ID,
                        MONTHLY_DUE_DATE,
                        MONTHLY_DISTANCE_GOAL,
                        MONTHLY_DISTANCE_CURRENT,
                        MONTHLY_CALORIES_GOAL,
                        MONTHLY_CALORIES_CURRENT
                },
                null, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Goal g = new Goal(Goal.Type.MONTHLY,
                    1,
                    Calendar.getInstance().getTimeInMillis(),
                    0, 0, 0, 0);
            writeMonthlyGoal(g);
            return g;
        }
        try {
            Goal g = new Goal(
                    Goal.Type.MONTHLY,
                    cursor.getInt(MONTHLY_GOAL_ID_COL),
                    cursor.getLong(MONTHLY_DUE_DATE_COL),
                    cursor.getDouble(MONTHLY_DISTANCE_GOAL_COL),
                    cursor.getDouble(MONTHLY_DISTANCE_CURRENT_COL),
                    cursor.getLong(MONTHLY_CALORIES_GOAL_COL),
                    cursor.getLong(MONTHLY_CALORIES_CURRENT_COL));
            return g;
        } catch (Exception e) {
            Log.d(APP_NAME, "exception when creating monthly GOAL!:" + e.getMessage());
            return null;
        }

    }

    public Goal getDailyGoal() {

        Log.d(APP_NAME, "getting Daily goal..");

        this.openReadableDB();
        Cursor cursor = db.query(DAILY_GOALS_TBL, new String[]{
                        DAILY_GOAL_ID,
                        DAILY_DUE_DATE,
                        DAILY_DISTANCE_GOAL,
                        DAILY_DISTANCE_CURRENT,
                        DAILY_CALORIES_GOAL,
                        DAILY_CALORIES_CURRENT
                },
                null, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Goal g = new Goal(Goal.Type.DAILY,
                    1,
                    Calendar.getInstance().getTimeInMillis(),
                    0, 0, 0, 0);
            writeDailyGoal(g);
            return g;
        }
        try {
            Goal g = new Goal(
                    Goal.Type.DAILY,
                    cursor.getInt(DAILY_GOAL_ID_COL),
                    cursor.getLong(DAILY_DUE_DATE_COL),
                    cursor.getDouble(DAILY_DISTANCE_GOAL_COL),
                    cursor.getDouble(DAILY_DISTANCE_CURRENT_COL),
                    cursor.getLong(DAILY_CALORIES_GOAL_COL),
                    cursor.getLong(DAILY_CALORIES_CURRENT_COL));
            return g;
        } catch (Exception e) {
            Log.d(APP_NAME, "exception when creating daily GOAL!:" + e.getMessage());
            return null;
        }

    }

    public long updateWeeklyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(WEEKLY_DUE_DATE, g.getDate());
        cv.put(WEEKLY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(WEEKLY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(WEEKLY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(WEEKLY_CALORIES_CURRENT, g.getCurrentCalories());

        String where = WEEKLY_GOAL_ID + "= ?";
        String[] whereArgs = {String.valueOf(g.getId())};

        Log.d(APP_NAME, "Updating player in DB: \n" + g.toString());

        this.openWriteableDB();
        int rowCount = db.update(WEEKLY_GOALS_TBL, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public long updateMonthlyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(MONTHLY_DUE_DATE, g.getDate());
        cv.put(MONTHLY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(MONTHLY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(MONTHLY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(MONTHLY_CALORIES_CURRENT, g.getCurrentCalories());

        String where = MONTHLY_GOAL_ID + "= ?";
        String[] whereArgs = {String.valueOf(g.getId())};

        Log.d(APP_NAME, "Updating player in DB: \n" + g.toString());

        this.openWriteableDB();
        int rowCount = db.update(MONTHLY_GOALS_TBL, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public long updateDailyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(DAILY_DUE_DATE, g.getDate());
        cv.put(DAILY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(DAILY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(DAILY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(DAILY_CALORIES_CURRENT, g.getCurrentCalories());

        String where = DAILY_GOAL_ID + "= ?";
        String[] whereArgs = {String.valueOf(g.getId())};

        Log.d(APP_NAME, "Updating player in DB: \n" + g.toString());

        this.openWriteableDB();
        int rowCount = db.update(DAILY_GOALS_TBL, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public long updateGoal(Goal g){
        switch(g.getType()){
            case DAILY:
                return updateDailyGoal(g);
            case WEEKLY:
                return updateWeeklyGoal(g);
            case MONTHLY:
                return updateMonthlyGoal(g);
            default:
                return -1;
        }
    }

    private long writeWeeklyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(WEEKLY_DUE_DATE, g.getDate());
        cv.put(WEEKLY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(WEEKLY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(WEEKLY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(WEEKLY_CALORIES_CURRENT, g.getCurrentCalories());

        Log.d(APP_NAME, "Inserting Weekly Goal to DB: \n" + g.toString());

        this.openWriteableDB();
        long rowID = db.insert(WEEKLY_GOALS_TBL, null, cv);
        this.closeDB();

        return rowID;
    }
    private long writeMonthlyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(MONTHLY_DUE_DATE, g.getDate());
        cv.put(MONTHLY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(MONTHLY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(MONTHLY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(MONTHLY_CALORIES_CURRENT, g.getCurrentCalories());

        Log.d(APP_NAME, "Inserting Monthly Goal to DB: \n" + g.toString());

        this.openWriteableDB();
        long rowID = db.insert(MONTHLY_GOALS_TBL, null, cv);
        this.closeDB();

        return rowID;
    }
    private long writeDailyGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(DAILY_DUE_DATE, g.getDate());
        cv.put(DAILY_DISTANCE_GOAL, g.getTargetDistance());
        cv.put(DAILY_DISTANCE_CURRENT, g.getCurrentDistance());
        cv.put(DAILY_CALORIES_GOAL, g.getTargetCalories());
        cv.put(DAILY_CALORIES_CURRENT, g.getCurrentCalories());

        Log.d(APP_NAME, "Inserting Daily Goal to DB: \n" + g.toString());

        this.openWriteableDB();
        long rowID = db.insert(DAILY_GOALS_TBL, null, cv);
        this.closeDB();

        return rowID;
    }
}
//    public int updatePlayer(Player p) {
//        ContentValues cv = new ContentValues();
//        cv.put(PLAYER_NAME, p.getName());
//        cv.put(PLAYER_WINS, p.getWins());
//        cv.put(PLAYER_LOSSES, p.getLosses());
//        cv.put(PLAYER_TIES, p.getTies());
//
//        String where = PLAYER_ID + "= ?";
//        String[] whereArgs = { String.valueOf(p.getId()) };
//
//Log.d(APP_NAME, "Updating player in DB: \n"+p.toString());
//
//        this.openWriteableDB();
//        int rowCount = db.update(PLAYER_TABLE, cv, where, whereArgs);
//        this.closeDB();
//
//        return rowCount;
//    }
//
//    public int deletePLayer(long id) {
//        String where = PLAYER_ID + "= ?";
//        String[] whereArgs = { String.valueOf(id) };
//
//Log.d(APP_NAME, "Deleting player with ID: "+id);
//
//        this.openWriteableDB();
//        int rowCount = db.delete(PLAYER_TABLE, where, whereArgs);
//        this.closeDB();
//
//        return rowCount;
//    }

