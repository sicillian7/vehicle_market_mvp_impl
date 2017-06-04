package com.nixxie.vehiclemarket.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nixxie.vehiclemarket.AppConstants;
import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.base.BaseActivity;
import com.nixxie.vehiclemarket.customViews.SelectionDetailsView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by nikolahristovski on 6/4/17.
 */

public class SummaryActivity extends BaseActivity {

    @BindView(R.id.vehicle_image)
    ImageView img;
    @BindView(R.id.selection_details_view)
    SelectionDetailsView selectionDetailsView;
    @BindView(R.id.btn_new_choice)
    TextView btnNewChoice;

    private String manufacturer, model, date;

    @Override
    protected int getContentView() {
        return R.layout.activity_summary;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Picasso.with(this)
                .load("http://storage.autonet.ca/v1/dynamic_resize/sws_path/autonet-prod-content/1288376217440_WIDESCREEN.jpg?quality=80&size=638x&stmp=1391277161714&clip=0")
                .into(img);

        manufacturer = getIntent().getExtras().getString(AppConstants.MANUFACTURER);
        model = getIntent().getExtras().getString(AppConstants.MODEL);
        date = getIntent().getExtras().getString(AppConstants.BUILT_DATE);

        if (manufacturer != null) {
            selectionDetailsView.setManufacturer(manufacturer);
        }

        if (model != null) {
            selectionDetailsView.setModel(model);
        }

        if (date != null) {
            selectionDetailsView.setYear(date);
        }
    }

    @OnClick(R.id.btn_new_choice)
    void onNewChoiceClick(){
        Intent i = new Intent(SummaryActivity.this, MainActivity.class);
        startActivity(i);
    }
}
