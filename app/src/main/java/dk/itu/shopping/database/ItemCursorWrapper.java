package dk.itu.shopping.database;
import android.database.Cursor;
import android.database.CursorWrapper;
import dk.itu.shopping.Item;
import dk.itu.shopping.database.ItemsDbSchema.ItemTable;

public class ItemCursorWrapper extends CursorWrapper {
  public ItemCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Item getItem() {
    String what = getString(getColumnIndex(ItemTable.Cols.WHAT));
    String where = getString(getColumnIndex(ItemTable.Cols.WHERE));
    byte[] pict= getBlob(getColumnIndex(ItemTable.Cols.PICT));
    return new Item(what, where, pict);
  }
}