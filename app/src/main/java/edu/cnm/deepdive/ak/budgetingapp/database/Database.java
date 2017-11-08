package edu.cnm.deepdive.ak.budgetingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "LIST_CONTENT";
  public static final String DATABASE_TABLE = "LIST_TABLE";

  public static final int DATABASE_VERSION = 1;

  public static final String ID_COLUMN = "_id";
  public static final String DATE_COLUMN = "date";
  public static final String AMOUNT_COLUMN = "amount";
  public static final String PLACE_COLUMN = "place";
  public static final String NOTES_COLUMN ="notes";

  //  change/add columns for date, amount, place, notes
  private static final String SCRIPT_CREATE_DATABASE =
      "CREATE TABLE " + DATABASE_TABLE + " ("
          + ID_COLUMN + " integer primary key autoincrement,"
          + DATE_COLUMN + " text not null,"
          + AMOUNT_COLUMN + " text not null,"
          + PLACE_COLUMN + " text not null,"
          + NOTES_COLUMN + " text not null)";


  public Database(Context context) {
    super(context, DATABASE_NAME+".db", null,DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }
  public long insert(SQLiteDatabase db, String date, String amount, String place, String notes) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(DATE_COLUMN, date);
    contentValues.put(AMOUNT_COLUMN, amount);
    contentValues.put(PLACE_COLUMN, place);
    contentValues.put(NOTES_COLUMN, notes);

    return db.insert(DATABASE_TABLE, null, contentValues);
  }
}
