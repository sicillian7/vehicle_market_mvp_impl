package com.nixxie.vehiclemarket.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nixxie.vehiclemarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nikolahristovski on 6/3/17.
 */

public class SelectionDetailsView extends LinearLayout {

    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_year)
    TextView tvYear;


    public SelectionDetailsView(Context context) {
        super(context);
        init();
    }

    public SelectionDetailsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectionDetailsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.selection_details_view, this);
        ButterKnife.bind(this);
    }

    public void setManufacturer(String m){
        tvManufacturer.setText(m);
        animate(tvManufacturer);
    }

    public void setModel(String m){
        tvModel.setText(m);
        animate(tvManufacturer);
    }

    public void setYear(String y){
        tvYear.setText(y);
        animate(tvYear);
    }

    private void animate(TextView tv) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.text_anim);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
