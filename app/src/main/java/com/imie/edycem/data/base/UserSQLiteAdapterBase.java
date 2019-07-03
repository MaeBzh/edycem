
/*
 * UserSQLiteAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.data.base;

import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import android.content.ContentValues;


import android.database.sqlite.SQLiteDatabase;


import com.imie.edycem.data.SQLiteAdapter;
import com.imie.edycem.data.UserSQLiteAdapter;
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.entity.User;

import com.imie.edycem.harmony.util.DateUtils;
import com.imie.edycem.EdycemApplication;



/** User adapter database abstract class. <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project<br/>
 * with Harmony.<br />
 * You should edit UserAdapter class instead of this<br/>
 * one or you will lose all your modifications.</i></b>
 */
public abstract class UserSQLiteAdapterBase
                        extends SQLiteAdapter<User> {

    /** TAG for debug purpose. */
    protected static final String TAG = "UserDBAdapter";


    /**
     * Get the table name used in DB for your User entity.
     * @return A String showing the table name
     */
    public String getTableName() {
        return UserContract.TABLE_NAME;
    }

    /**
     * Get the joined table name used in DB for your User entity
     * and its parents.
     * @return A String showing the joined table name
     */
    public String getJoinedTableName() {
        String result = UserContract.TABLE_NAME;
        return result;
    }

    /**
     * Get the column names from the User entity table.
     * @return An array of String representing the columns
     */
    public String[] getCols() {
        return UserContract.ALIASED_COLS;
    }

    /**
     * Generate Entity Table Schema.
     * @return "SQL query : CREATE TABLE..."
     */
    public static String getSchema() {
        return "CREATE TABLE "
        + UserContract.TABLE_NAME    + " ("
        
         + UserContract.COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
         + UserContract.COL_IDSMARTPHONE    + " VARCHAR NOT NULL,"
         + UserContract.COL_PASSWORD    + " VARCHAR NOT NULL,"
         + UserContract.COL_DATERGPD    + " DATETIME NOT NULL"

        
        + ");"
;
    }

    /**
     * Constructor.
     * @param ctx context
     */
    public UserSQLiteAdapterBase(final android.content.Context ctx) {
        super(ctx);
    }

    // Converters

    /**
     * Convert User entity to Content Values for database.
     * @param item User entity object
     * @return ContentValues object
     */
    public ContentValues itemToContentValues(final User item) {
        return UserContract.itemToContentValues(item);
    }

    /**
     * Convert android.database.Cursor of database to User entity.
     * @param cursor android.database.Cursor object
     * @return User entity
     */
    public User cursorToItem(final android.database.Cursor cursor) {
        return UserContract.cursorToItem(cursor);
    }

    /**
     * Convert android.database.Cursor of database to User entity.
     * @param cursor android.database.Cursor object
     * @param result User entity
     */
    public void cursorToItem(final android.database.Cursor cursor, final User result) {
        UserContract.cursorToItem(cursor, result);
    }

    //// CRUD Entity ////
    /**
     * Find & read User by id in database.
     *
     * @param id Identify of User
     * @return User entity
     */
    public User getByID(final int id) {
        final android.database.Cursor cursor = this.getSingleCursor(id);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }

        final User result = this.cursorToItem(cursor);
        cursor.close();

        return result;
    }


    /**
     * Read All Users entities.
     *
     * @return List of User entities
     */
    public ArrayList<User> getAll() {
        final android.database.Cursor cursor = this.getAllCursor();
        final ArrayList<User> result = this.cursorToItems(cursor);
        cursor.close();

        return result;
    }



    /**
     * Insert a User entity into database.
     *
     * @param item The User entity to persist
     * @return Id of the User entity
     */
    public long insert(final User item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Insert DB(" + UserContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                UserContract.itemToContentValues(item);
        values.remove(UserContract.COL_ID);
        int insertResult;
        if (values.size() != 0) {
            insertResult = (int) this.insert(
                    null,
                    values);
        } else {
            insertResult = (int) this.insert(
                    UserContract.COL_ID,
                    values);
        }
        item.setId(insertResult);
        return insertResult;
    }

    /**
     * Either insert or update a User entity into database whether.
     * it already exists or not.
     *
     * @param item The User entity to persist
     * @return 1 if everything went well, 0 otherwise
     */
    public int insertOrUpdate(final User item) {
        int result = 0;
        if (this.getByID(item.getId()) != null) {
            // Item already exists => update it
            result = this.update(item);
        } else {
            // Item doesn't exist => create it
            final long id = this.insert(item);
            if (id != 0) {
                result = 1;
            }
        }

        return result;
    }

    /**
     * Update a User entity into database.
     *
     * @param item The User entity to persist
     * @return count of updated entities
     */
    public int update(final User item) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Update DB(" + UserContract.TABLE_NAME + ")");
        }

        final ContentValues values =
                UserContract.itemToContentValues(item);
        final String whereClause =
                 UserContract.COL_ID
                 + " = ?";
        final String[] whereArgs =
                new String[] {String.valueOf(item.getId()) };

        return this.update(
                values,
                whereClause,
                whereArgs);
    }


    /**
     * Delete a User entity of database.
     *
     * @param id id
     * @return count of updated entities
     */
    public int remove(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(
                TAG,
                "Delete DB("
                    + UserContract.TABLE_NAME
                    + ")"
                    + " id : "+ id);
        }

        final String whereClause =
                UserContract.COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {
                    String.valueOf(id)};

        return this.delete(
                whereClause,
                whereArgs);
    }

    /**
     * Deletes the given entity.
     * @param user The entity to delete
     * @return count of updated entities
     */
    public int delete(final User user) {
        return this.remove(user.getId());
    }

    /**
     *  Internal android.database.Cursor.
     * @param id id
     *  @return A android.database.Cursor pointing to the User corresponding
     *        to the given id.
     */
    protected android.database.Cursor getSingleCursor(final int id) {
        if (EdycemApplication.DEBUG) {
            android.util.Log.d(TAG, "Get entities id : " + id);
        }

        final String whereClause =
                UserContract.ALIASED_COL_ID
                + " = ?";
        final String[] whereArgs = new String[] {String.valueOf(id)};

        return this.query(
                UserContract.ALIASED_COLS,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }


    /**
     * Query the DB to find a User entity.
     *
     * @param id The id of the entity to get from the DB
     *
     * @return The cursor pointing to the query's result
     */
    public android.database.Cursor query(final int id) {

        String selection = UserContract.ALIASED_COL_ID + " = ?";
        

        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        return this.query(
                UserContract.ALIASED_COLS,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }




}
