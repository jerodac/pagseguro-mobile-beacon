package br.com.uol.ps.beacon.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.uol.ps.beacon.R;
import br.com.uol.ps.beacon.others.OffersModel;
import br.com.uol.ps.beacon.utils.Utils;

/**
 * Adapter do CardView
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public class RecyclerViewAdapter extends RecyclerView
        .Adapter<RecyclerViewAdapter
        .DataObjectHolder> {

    private List<OffersModel> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;

    public RecyclerViewAdapter(List<OffersModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        TextView price;
        ImageView img;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.tv_describe);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            dateTime = (TextView) itemView.findViewById(R.id.tv_date);
            img = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), mDataset.get(getAdapterPosition()), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getTitle());
        holder.price.setText(Utils.bigDecimalToMonetary(mDataset.get(position).getValue()));
        holder.dateTime.setText(mDataset.get(position).getTime());
        Picasso.with(mContext)
                .load(mDataset.get(position).getImageResource())
                .placeholder(R.drawable.ic_loading_image_card_view)
                .into(holder.img);

    }

    public void addItem(OffersModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, OffersModel offersModel, View v);
    }
}