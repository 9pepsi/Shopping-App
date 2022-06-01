package com.example.fashion_store;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCategoryList extends ArrayAdapter<String> {
    private String[] categoryName;
    private Integer[] categoryImage;
    private Activity context;

    public CustomCategoryList(Activity context, String[] categoryName, Integer[] categoryImage) {
        super(context, R.layout.category_page_list_view, categoryName);
        this.context = context;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.category_page_list_view, null, true);
        TextView categoryTextView = row.findViewById(R.id.category_name);
        ImageView imageCategory = row.findViewById(R.id.category_image);

        categoryTextView.setText(categoryName[position]);
        imageCategory.setImageResource(categoryImage[position]);
        return  row;
    }
}