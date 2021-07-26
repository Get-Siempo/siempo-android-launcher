package co.siempo.phone.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.focuslauncher.R;

public class AgendaItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView textView;

    public AgendaItemViewHolder(View itemView) {
        super(itemView);


        ButterKnife.bind(this, itemView);
    }

    public void render(String text) {
        textView.setText(text);
    }
}
