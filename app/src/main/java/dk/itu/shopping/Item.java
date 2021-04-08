package dk.itu.shopping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public class Item {
  private String mWhat = null;
  private String mWhere = null;
  private byte[] mPict = null;

  public Item(String what, String where) {
    mWhat= what;
    mWhere= where;
  }

  public Item(String what, String where, byte[] pict) {
    mWhat= what;
    mWhere= where;
    mPict= pict;
  }

  @Override
  public String toString() { return oneLine(""," in: "); }
  public String getWhat() { return mWhat; }
  public void setWhat(String what) { mWhat = what; }
  public String getWhere() { return mWhere; }
  public void setWhere(String where) { mWhere = where; }
  public String oneLine(String pre, String post) {
    return pre+mWhat + post + mWhere;
  }
  public byte[] getPict() {
    return mPict;
  }
  public void setPict(byte[] pict) { this.mPict= pict;  }

  // convert and scale from/to bitmap to byte array
  public static byte[] scaleAndConvert(Bitmap bitmap) {
    Bitmap resized= Bitmap.createScaledBitmap(bitmap, 100, 100, false);
    ByteArrayOutputStream stream= new ByteArrayOutputStream();
    resized.compress(Bitmap.CompressFormat.PNG, 0, stream);
    return stream.toByteArray();
  }

  public static Bitmap ConvertByteArrayToBitmap(byte[] blop) {
    return BitmapFactory.decodeByteArray(blop, 0, blop.length);
  }
}
