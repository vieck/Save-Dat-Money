package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.AddItem;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.Fragments.AddFragment;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddAdapter extends BaseAdapter {

    Context context;
    List<AddTree.Node> categoryNodes;
    Bundle bundle;

    public AddAdapter(Context context, List<AddTree.Node> objects, Bundle bundle) {
        this.context = context;
        this.categoryNodes = objects;
        this.bundle = bundle;
    }

    @Override
    public int getCount() {
        return categoryNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_add, parent, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        final int i = position;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnListViewClick", "Clicked");
                if (!categoryNodes.get(i).isLeafNode()) {
                    categoryNodes = categoryNodes.get(i).getChildNodes();
                    notifyDataSetChanged();
                } else {
                    AddItem item = categoryNodes.get(i).getItem();

                    FragmentActivity fragmentActivity = ((FragmentActivity) context);
                    bundle.putString("Category", item.getType());
                    bundle.putString("Subcategory", item.getSubType());
                    AddFragment addFragment = new AddFragment();
                    addFragment.setArguments(bundle);
                    fragmentActivity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, addFragment).commit();
                }
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        final AddItem addItem = categoryNodes.get(position).getItem();
        textView.setText(addItem.getType());
        imageView.setImageDrawable(ContextCompat.getDrawable(context, addItem.getIcon()));
        return view;
    }

}
