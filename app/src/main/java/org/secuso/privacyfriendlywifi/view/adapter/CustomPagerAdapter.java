package org.secuso.privacyfriendlywifi.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
//        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
//        collection.addView(layout);
        TimePicker picker = new TimePicker(collection.getContext());
        collection.addView(picker);
        return picker;//layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 1;//CustomPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return "blabla";//mContext.getString(customPagerEnum.getTitleResId());
    }

}