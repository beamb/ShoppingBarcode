package dk.itu.shopping;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingActivity extends AppCompatActivity {
  //Shopping using SQLite database and Camera to get a Barcode

  private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
  private static final int CAMERA_REQUEST_CODE = 10;

  private final static String BarcodeIntent= "com.google.zxing.client.android.SCAN";
  private final static int BARCODE_REQUEST= 2;

  // Model: Database of items
  private static ItemsDB itemsDB;

  // GUI variables
  private Button addItem, listItems, scanBarcode;
  private TextView newWhat, newWhere;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shopping);
    itemsDB = ItemsDB.get(this);

    //Text Fields
    newWhat=  findViewById(R.id.what_text);
    newWhere= findViewById(R.id.where_text);
     scanBarcode= findViewById(R.id.barcode_button);
    listItems= findViewById(R.id.list);

    listItems.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(ShoppingActivity.this, ListActivity.class);
          startActivity(intent);
        }
      });

    addItem= findViewById(R.id.add_button);
    // adding a new thing
    addItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if ((newWhat.getText().length() > 0) && (newWhere.getText().length() > 0)) {
          Item newItem= new Item(
              newWhat.getText().toString(), newWhere.getText().toString());
          itemsDB.addItem(newItem);
          newWhat.setText("");
          newWhere.setText("");
        } else Toast.makeText(ShoppingActivity.this, R.string.empty_toast, Toast.LENGTH_LONG).show();
      }
    });

    //Handling pictures of things
    scanBarcode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
          Intent intent= new Intent(BarcodeIntent);
          intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
          startActivityForResult(intent, BARCODE_REQUEST);
        } else {
          if (hasCameraPermission()) {
            enableCamera();
          } else {
            requestPermission();
          }
        }
      }
    });
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
      super.onActivityResult(requestCode, resultCode, intent);
      if (resultCode == Activity.RESULT_OK) {
        if (resultCode == Activity.RESULT_OK) {
          String barcode = "No Barcode";
          if (requestCode == BARCODE_REQUEST) barcode = intent.getStringExtra("SCAN_RESULT");
          newWhat.setText(barcode);
        }
      }
    } else {
      if (resultCode == Activity.RESULT_OK) {
        if (requestCode == BARCODE_REQUEST) {
          String barcode = "No Barcode";
          if (intent != null) {
            barcode = BarcodeActivity.getBarcode(intent);
          }
          newWhat.setText(barcode);
        }
      }
    }
  }

  private boolean hasCameraPermission() {
    return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(
            this,
            CAMERA_PERMISSION,
            CAMERA_REQUEST_CODE
    );
  }

  private void enableCamera() {
    Intent intent = new Intent(this, BarcodeActivity.class);
    startActivityForResult(intent, BARCODE_REQUEST);
  }
}
