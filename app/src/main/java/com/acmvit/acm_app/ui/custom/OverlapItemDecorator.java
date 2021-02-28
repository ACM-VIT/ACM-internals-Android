package com.acmvit.acm_app.ui.custom;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OverlapItemDecorator extends RecyclerView.ItemDecoration {

    private final int overlap;
    private final int orientation;

    public OverlapItemDecorator(int orientation, int overlap) {
        this.overlap = overlap;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(
        @NonNull Rect outRect,
        @NonNull View view,
        @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
        int pos = parent.getChildAdapterPosition(view);

        if (pos == RecyclerView.NO_POSITION || pos == 0) {
            return;
        }

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = -overlap;
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = -overlap;
        } else {
            throw new IllegalArgumentException(
                "Orientation should either be LinearLayoutManager.VERTICAL or LinearLayoutManager.Horizontal"
            );
        }
    }

    @Override
    public void onDraw(
        @NonNull Canvas c,
        @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
        super.onDraw(c, parent, state);
    }
}
