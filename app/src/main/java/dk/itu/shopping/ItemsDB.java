package dk.itu.shopping;
import android.content.ContentValues;
import android.content.Context;
import java.util.ArrayList;
import java.util.Observable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.itu.shopping.database.ItemBaseHelper;
import dk.itu.shopping.database.ItemCursorWrapper;
import dk.itu.shopping.database.ItemsDbSchema;

public class ItemsDB extends Observable {
  private static ItemsDB sItemsDB;
  private static SQLiteDatabase mDatabase;

  private ItemsDB(Context context) {
    if (getAll().size() == 0)  fillItemsDB();
  }

  public static ItemsDB get(Context context) {
    if (sItemsDB == null)  {
      mDatabase= new ItemBaseHelper(context.getApplicationContext()).getWritableDatabase();
      sItemsDB= new ItemsDB(context);
    }
    return sItemsDB;
  }

  public void addItem(Item newItem) {
    ContentValues values= getContentValues(newItem);
    mDatabase.insert(ItemsDbSchema.ItemTable.NAME, null, values);
    this.setChanged(); notifyObservers();
  }

  public void initItem(String what, String where){
    Item newItem= new Item(what, where);
    ContentValues values= getContentValues(newItem);
    mDatabase.insert(ItemsDbSchema.ItemTable.NAME, null, values);
    this.setChanged(); notifyObservers();
  }

  public void removeItem(String what){
    Item newItem= new Item(what, "");
    String selection= ItemsDbSchema.ItemTable.Cols.WHAT + " LIKE ?";
    int changed= mDatabase.delete(ItemsDbSchema.ItemTable.NAME, selection, new String[]{newItem.getWhat()});
    if (changed > 0) { this.setChanged(); notifyObservers();  }
  }

  public void fillItemsDB() {
    initItem("coffee", "Irma");
    initItem("carrots", "Netto");
    initItem("milk", "Netto");
    initItem("bread", "bakery");
    initItem("butter", "Irma");
    initItem("oranges", "Irma");
    initItem("cheese", "Netto");
    initItem("bread", "bakery");
    initItem("apples", "Menu");
  }

  public ArrayList<Item> getAll() {
    ArrayList<Item> items= new ArrayList<Item>();
    ItemCursorWrapper cursor= queryItems(null, null);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      items.add(cursor.getItem());
      cursor.moveToNext();
    }
    cursor.close();
    return items;
  }

  // Database helper methods to convert between Items and database rows
  private static ContentValues getContentValues(Item item) {
    ContentValues values=  new ContentValues();
    values.put(ItemsDbSchema.ItemTable.Cols.WHAT, item.getWhat());
    values.put(ItemsDbSchema.ItemTable.Cols.WHERE, item.getWhere());
    if (item.getWhere() != null) values.put(ItemsDbSchema.ItemTable.Cols.PICT, item.getPict());
    return values;
  }

  static private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
    Cursor cursor= mDatabase.query(
        ItemsDbSchema.ItemTable.NAME,
        null, // Columns - null selects all columns
        whereClause, whereArgs,
        null, // groupBy
        null, // having
        null  // orderBy
    );
    return new ItemCursorWrapper(cursor);
  }
}