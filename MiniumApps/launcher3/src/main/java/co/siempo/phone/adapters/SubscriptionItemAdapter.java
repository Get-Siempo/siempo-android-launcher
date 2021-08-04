package co.siempo.phone.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.focuslauncher.R;
import co.siempo.phone.activities.InAppItemListActivity;
import co.siempo.phone.util.SkuDetails;

/**
 * Created by Rajesh Jadi on 2/23/2017.
 */

public class SubscriptionItemAdapter extends RecyclerView.Adapter<SubscriptionItemAdapter.ViewHolder> {

    private final Activity context;
    private List<SkuDetails> skuDetailsList;

    public SubscriptionItemAdapter(Activity context, List<SkuDetails> skuDetailsList) {
        this.context = context;
        this.skuDetailsList = skuDetailsList;
    }

    @Override
    public SubscriptionItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_subscription, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SkuDetails item = skuDetailsList.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtDescription.setText(item.getDescription());
        holder.txtPrice.setText(item.getPrice() + " " + item.getPriceCurrencyCode());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InAppItemListActivity) context).purchaseItem(item);
            }
        });
    }


    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        // each data item is just a string in this case
        TextView txtTitle;
        TextView txtDescription, txtPrice;
        CardView card;

        public ViewHolder(View v) {
            super(v);
            card = v.findViewById(R.id.card);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtPrice = v.findViewById(R.id.txtPrice);
        }
    }
}
