package shy.car.sdk.travel.location;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.base.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import shy.car.sdk.R;
import shy.car.sdk.travel.location.data.City;


/**
 * Demo: 搜索结果显示Fragment
 * Created by YoKey on 16/10/9.
 */
public class SearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mTvNoResult;
    private SearchAdapter mAdapter;
    private List<City> mDatas;
    
    private String mQueryText;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_city, container, false);
        mTvNoResult = (TextView) view.findViewById(R.id.tv_no_result);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recy);
        return view;
    }
    
    public void bindDatas(List<City> datas) {
        this.mDatas = datas;
        mAdapter = new SearchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        if (mQueryText != null) {
            mAdapter.getFilter().filter(mQueryText);
        }
    }
    
    /**
     * 根据newText 进行查找, 显示
     */
    public void bindQueryText(String newText) {
        if (mDatas == null) {
            mQueryText = newText.toLowerCase();
        } else if (!TextUtils.isEmpty(newText)) {
            mAdapter.getFilter().filter(newText.toLowerCase());
        }
    }
    
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> implements Filterable {
        private List<City> items = new ArrayList<>();
        
        public SearchAdapter() {
            items.clear();
            items.addAll(mDatas);
        }
        
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            final VH holder = new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_city, parent, false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    ToastManager.showShortToast(getActivity(), "选择了" + items.get(position).getCityName());
                }
            });
            return holder;
        }
        
        @Override
        public int getItemCount() {
            return items.size();
        }
        
        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.tvName.setText(items.get(position).getCityName());
        }
        
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    ArrayList<City> list = new ArrayList<>();
                    for(City item : mDatas) {
                        if (item.getPinyin().startsWith(constraint.toString()) || item.getCityName().contains(constraint)) {
                            list.add(item);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.count = list.size();
                    results.values = list;
                    return results;
                }
                
                @Override
                @SuppressWarnings("unchecked")
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    ArrayList<City> list = (ArrayList<City>) results.values;
                    items.clear();
                    items.addAll(list);
                    if (results.count == 0) {
                        mTvNoResult.setVisibility(View.VISIBLE);
                    } else {
                        mTvNoResult.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            };
        }
        
        class VH extends RecyclerView.ViewHolder {
            private TextView tvName;
            
            public VH(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }
}
