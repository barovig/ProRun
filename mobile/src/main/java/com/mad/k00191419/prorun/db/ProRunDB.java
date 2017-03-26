package com.mad.k00191419.prorun.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProRunDB {

    private static final String APP_NAME = "__PRO_RUN";
    // database constants
    public static final String DB_NAME = "prorun.db";
    public static final int    DB_VERSION = 1;

    // locations table constants
    public static final String LOCATIONS_TBL = "locations";

    public static final String LOCATION_ID = "_id";
    public static final int    LOCATIN_ID_COL = 0;
    
    public static final String LOCATION_RUN_NO = "run_no";
    public static final int    LOCATION_RUN_NO_COL = 1;

    public static final String LOCATION_LATITUDE = "latitude";
    public static final int    LOCATION_LATITUDE_COL = 2;

    public static final String LOCATION_LONGITUDE = "longitude";
    public static final int    LOCATION_PLAYER_WINS_COL = 3;

    public static final String LOCATION_TIME = "time";
    public static final int    LOCATION_TIME_COL = 4;
    
    // runs table constants
    public static final String RUNS_TBL = "runs";

    public static final String RUN_NO = "_run_no";
    public static final int    RUN_NO_COL = 0;

    public static final String RUN_START_DATE = "start_date";
    public static final int    RUN_START_DATE_COL = 1;

    public static final String RUN_TOTAL_TIME = "time";
    public static final int    RUN_TOTAL_TIME_COL = 2;

    public static final String RUN_TOTAL_DISTANCE = "distance";
    public static final int    RUN_TOTAL_DISTANCE_COL = 3;
    
    public static final String RUN_TOTAL_CALORIES = "calories";
    public static final int    RUN_TOTAL_CALORIES_COL = 4;
    
    public static final String RUN_AVG_SPEED = "average_speed";
    public static final int    RUN_AVG_SPEED_COL = 5;
    
    // daily goals table constants
    public static final String DAILY_GOALS_TBL = "daily_goals";

    public static final String DAILY_GOAL_ID = "_id";
    public static final int    DAILY_GOAL_ID_COL = 0;

    public static final String DAILY_DUE_DATE = "due_date";
    public static final int    DAILY_DUE_DATE_COL = 1;

    public static final String DAILY_DISTANCE_GOAL = "distance_goal";
    public static final int    DAILY_DISTANCE_GOAL_COL = 2;

    public static final String DAILY_DISTANCE_CURRENT = "distance_current";
    public static final int    DAILY_DISTANCE_CURRENT_COL = 3;
    
    public static final String DAILY_CALORIES_GOAL = "calories_goal";
    public static final int    DAILY_CALORIES_GOAL_COL = 4;
    
    public static final String DAILY_CALORIES_CURRENT = "calories_current";
    public static final int    DAILY_CALORIES_CURRENT_COL = 5;    
    
    // weekly goals table constants
    public static final String WEEKLY_GOALS_TBL = "weekly_goals";

    public static final String WEEKLY_GOAL_ID = "_id";
    public static final int    WEEKLY_GOAL_ID_COL = 0;

    public static final String WEEKLY_DUE_DATE = "due_date";
    public static final int    WEEKLY_DUE_DATE_COL = 1;

    public static final String WEEKLY_DISTANCE_GOAL = "distance_goal";
    public static final int    WEEKLY_DISTANCE_GOAL_COL = 2;

    public static final String WEEKLY_DISTANCE_CURRENT = "distance_current";
    public static final int    WEEKLY_DISTANCE_CURRENT_COL = 3;
    
    public static final String WEEKLY_CALORIES_GOAL = "calories_goal";
    public static final int    WEEKLY_CALORIES_GOAL_COL = 4;

    public static final String WEEKLY_CALORIES_CURRENT = "calories_current";
    public static final int    WEEKLY_CALORIES_CURRENT_COL = 5;    
    
    // monthly goals table constants
    public static final String MONTHLY_GOALS_TBL = "monthly_goals";

    public static final String MONTHLY_GOAL_ID = "_id";
    public static final int    MONTHLY_GOAL_ID_COL = 0;

    public static final String MONTHLY_DUE_DATE = "due_date";
    public static final int    MONTHLY_DUE_DATE_COL = 1;

    public static final String MONTHLY_DISTANCE_GOAL = "distance_goal";
    public static final int    MONTHLY_DISTANCE_GOAL_COL = 2;

    public static final String MONTHLY_DISTANCE_CURRENT = "distance_current";
    public static final int    MONTHLY_DISTANCE_CURRENT_COL = 3;
    
    public static final String MONTHLY_CALORIES_GOAL = "calories_goal";
    public static final int    MONTHLY_CALORIES_GOAL_COL = 4;
    
    public static final String MONTHLY_CALORIES_CURRENT = "calories_current";
    public static final int    MONTHLY_CALORIES_CURRENT_COL = 5;    
    
    // CREATE TABLE statements
    public static final String CREATE_LOCATIONS_TABLE =
            "CREATE TABLE " + LOCATIONS_TBL + " (" +
                    LOCATION_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCATION_RUN_NO    + " TEXT, " +
                    LOCATION_LATITUDE  + " REAL, " +
                    LOCATION_LONGITUDE + " REAL, " +
                    LOCATION_TIME      + " INTEGER)";
                    
    public static final String CREATE_RUNS_TABLE =
            "CREATE TABLE " + RUNS_TBL + " (" +
                    RUN_NO             + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RUN_START_DATE     + " INTEGER, " +
                    RUN_TOTAL_TIME     + " INTEGER, " +                    
                    RUN_TOTAL_DISTANCE + " REAL, " +
                    RUN_TOTAL_CALORIES + " INTEGER, " +
                    RUN_AVG_SPEED      + " REAL)";
                    
    public static final String CREATE_DAILY_GOALS_TABLE = 
            "CREATE TABLE " + DAILY_GOALS_TBL + " (" +
                    DAILY_GOAL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DAILY_DUE_DATE         + " INTEGER, " +
                    DAILY_DISTANCE_GOAL    + " REAL, " +
                    DAILY_DISTANCE_CURRENT + " REAL, " +
                    DAILY_CALORIES_GOAL    + " INTEGER, " +
                    DAILY_CALORIES_CURRENT + " INTEGER)";

                    
    public static final String CREATE_WEEKLY_GOALS_TABLE = 
            "CREATE TABLE " + WEEKLY_GOALS_TBL + " (" +
                    WEEKLY_GOAL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WEEKLY_DUE_DATE         + " INTEGER, " +
                    WEEKLY_DISTANCE_GOAL    + " REAL, " +
                    WEEKLY_DISTANCE_CURRENT + " REAL, " +
                    WEEKLY_CALORIES_GOAL    + " INTEGER, " +
                    WEEKLY_CALORIES_CURRENT + " INTEGER)";
                    
    public static final String CREATE_MONTHLY_GOALS_TABLE = 
            "CREATE TABLE " + MONTHLY_GOALS_TBL + " (" +
                    MONTHLY_GOAL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MONTHLY_DUE_DATE         + " INTEGER, " +
                    MONTHLY_DISTANCE_GOAL    + " REAL, " +
                    MONTHLY_DISTANCE_CURRENT + " REAL, " +
                    MONTHLY_CALORIES_GOAL    + " INTEGER, " +
                    MONTHLY_CALORIES_CURRENT + " INTEGER)";   
                    
                    
    public static final String DROP_LOCATIONS_TABLE =
            "DROP TABLE IF EXISTS " + LOCATIONS_TBL;
            
    public static final String DROP_RUNS_TABLE =
            "DROP TABLE IF EXISTS " + RUNS_TBL;
            
    public static final String DROP_DAILY_GOALS_TABLE =
            "DROP TABLE IF EXISTS " + DAILY_GOALS_TBL;
            
    public static final String DROP_WEEKLY_GOALS_TABLE =
            "DROP TABLE IF EXISTS " + WEEKLY_GOALS_TBL;
            
    public static final String DROP_MONTHLY_GOALS_TABLE=
            "DROP TABLE IF EXISTS " + MONTHLY_GOALS_TBL;            

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory , int version) {
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

//    public ArrayList<Player> getPlayers() {
//
//        this.openReadableDB();
//        Cursor cursor = db.query(PLAYER_TABLE, null,
//                null, null,
//                null, null, null);
//
//        ArrayList<Player> players = new ArrayList<Player>();
//        while (cursor.moveToNext()) {
//            players.add(getPlayerFromCursor(cursor));
//        }
//
//        if (cursor != null)
//            cursor.close();
//
//        this.closeDB();
//Log.d(APP_NAME, "Retrieved "+players.size()+" players from DB");
//
//        return players;
//    }
//
//    private static Player getPlayerFromCursor(Cursor cursor) {
//        if (cursor == null || cursor.getCount() == 0){
//Log.d(APP_NAME, "getting player from cursor.. cursor EMPTY!");
//            return null;
//        }
//        else {
//            try {
//                Player p = new Player(
//                        cursor.getInt(PLAYER_ID_COL),
//                        cursor.getString(PLAYER_NAME_COL),
//                        cursor.getInt(PLAYER_WINS_COL),
//                        cursor.getInt(PLAYER_LOSSES_COL),
//                        cursor.getInt(PLAYER_TIES_COL));
//                return p;
//            }
//            catch(Exception e) {
//Log.d(APP_NAME, "getting player from cursor.. exception when creating PLAYER!");
//
//                return null;
//            }
//        }
//    }
//
//    public long insertPlayer(Player p) {
//        ContentValues cv = new ContentValues();
//        cv.put(PLAYER_ID, p.getId());
//        cv.put(PLAYER_NAME, p.getName());
//        cv.put(PLAYER_WINS, p.getWins());
//        cv.put(PLAYER_LOSSES, p.getLosses());
//        cv.put(PLAYER_TIES, p.getTies());
//
//Log.d(APP_NAME, "Inserting player to DB: \n"+p.toString());
//
//        this.openWriteableDB();
//        long rowID = db.insert(PLAYER_TABLE, null, cv);
//        this.closeDB();
//
//        return rowID;
//    }
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
}
