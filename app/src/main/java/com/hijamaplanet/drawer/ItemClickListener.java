package com.hijamaplanet.drawer;

import android.view.View;
import android.widget.ImageView;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
    void onRecyclerItemClick(int pos, ImageView shareImageView);
}
