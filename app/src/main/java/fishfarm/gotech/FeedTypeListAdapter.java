package fishfarm.gotech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aruna.ramakrishnan on 3/7/2018.
 */

public class FeedTypeListAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<FeedTypeModel> feedTypesList;
    String status;

    public FeedTypeListAdapter(Context context, ArrayList<FeedTypeModel> feedTypeModelArrayList, String stat) {
        feedTypesList = feedTypeModelArrayList;
        mContext = context;
        status = stat;
    }

    @Override
    public int getCount() {
        return feedTypesList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedTypesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        final LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.feed_qty_row, null);
        holder = new ViewHolder();
        holder.txtFeederType = (TextView) convertView.findViewById(R.id.txtFeederType);
        holder.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
        holder.imgDelete=(ImageView)convertView.findViewById(R.id.imgDelete);
        holder.txtFeederType.setText(feedTypesList.get(position).getName());
        holder.txtQty.setText(feedTypesList.get(position).getQty());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeederTankInspActivity) mContext).removeItem(position);
            }
        });
        holder.txtSlNo = (TextView) convertView.findViewById(R.id.txtSlNo);
        holder.txtSlNo.setText(String.valueOf(position+1));
        return convertView;
    }
    static class ViewHolder {
        TextView txtFeederType;
        TextView txtQty;
        ImageView imgDelete;
        TextView txtSlNo;

    }
}

