package helperClass;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fishfarm.gotech.R;

/**
 * Created by aruna.ramakrishnan on 3/04/2018.
 */
public class SearchResultsAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<SpinnerObject> searchResult;
    private List<SpinnerObject> mDisplayedValues;
    private List<SpinnerObject> mOriginalValues;

    public SearchResultsAdapter(Context context, List<SpinnerObject> searchResults) {
        this.context = context;
        searchResult = searchResults;
        mOriginalValues = (List<SpinnerObject>) searchResults;
    }

    public SearchResultsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return searchResult.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.search_results_row, null);
        }
//        RelativeLayout relSearch = (RelativeLayout) convertView.findViewById(R.id.relSearch);
//        TextView txtValue = (TextView) convertView.findViewById(R.id.txtValue);
//        txtValue.setText(searchResult.get(position).getValue());

        return convertView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                SearchResultsAdapter.this.searchResult = (List<SpinnerObject>) results.values;
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<SpinnerObject> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {

                    constraint = constraint.toString().toLowerCase();

                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String title = mOriginalValues.get(i).getValue();
                        if (title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }

                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
  }
