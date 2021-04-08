package dk.itu.shopping;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ListActivity extends AppCompatActivity implements Observer {

  public void update(Observable observable, Object data) {
    localDB= itemsDB.getAll();
    mAdapter.notifyDataSetChanged();
  }

  private static ItemsDB itemsDB;
  private RecyclerView itemList;
  private ItemAdapter mAdapter;
  private ArrayList<Item> localDB;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    itemsDB= ItemsDB.get(this);
    itemsDB.addObserver(this);
    localDB= itemsDB.getAll();
    itemList= findViewById(R.id.listItems);
    itemList.setLayoutManager(new LinearLayoutManager(this));
    mAdapter= new ItemAdapter();
    itemList.setAdapter(mAdapter);
  }
  private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mWhatTextView, mWhereTextView, mNoView;

    public ItemHolder(View itemView) {
      super(itemView);
      mNoView= itemView.findViewById(R.id.item_no);
      mWhatTextView= itemView.findViewById(R.id.item_what);
      mWhereTextView= itemView.findViewById(R.id.item_where);
      itemView.setOnClickListener(this);
    }

    public void bind(Item item, int position){
      mNoView.setText(" "+position+" ");
      mWhatTextView.setText(item.getWhat());
      mWhereTextView.setText(item.getWhere());
    }
    @Override
    public void onClick(View v) {
      // Two alternative ways to get what info from v
      //1.
      // int pos=itemList.getChildLayoutPosition(v);
      //String what= localDB.get(pos).getWhat();
      //2,
      // Trick from https://stackoverflow.com/questions/5754887/accessing-view-inside-the-linearlayout-with-code
      // Further explanation in class 6
      String what= (String)((TextView)v.findViewById(R.id.item_what)).getText();

      //once we have a value for what, we can delete the item
      itemsDB.removeItem(what);
    }
  }

  private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater= LayoutInflater.from(ListActivity.this);
      View v= layoutInflater.inflate(R.layout.one_row, parent, false);
      return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
      Item item= localDB.get(position);
      holder.bind(item, position);
    }

    @Override
    public int getItemCount(){ return localDB.size(); }
  }
}