package com.acmvit.acm_app.binding;

import android.content.Context;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.arch.core.util.Function;
import androidx.databinding.InverseMethod;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.acmvit.acm_app.R;
import com.acmvit.acm_app.model.ProjectStatus;

import java.util.Map;

public class BindingUtils {

    private static final Map<Integer, ProjectStatus> idProjectStatusMap = Map.of(
            R.id.ideation_chip, ProjectStatus.IDEATION,
            R.id.implementation_chip, ProjectStatus.IN_PROGRESS,
            R.id.done_chip, ProjectStatus.COMPLETED
    );

    @ColorInt
    public static int getColorFromStatus(Context context, ProjectStatus status){
        if (status == null) return 0;
        switch (status) {
            case IDEATION: return context.getColor(R.color.colorIdeation);
            case IN_PROGRESS: return context.getColor(R.color.colorImplementation);
            case COMPLETED: return context.getColor(R.color.colorCompleted);
        }
        return 0;
    }

    @InverseMethod("idToStatus")
    public static int statusToId(ProjectStatus status) {
        Log.d("gg", "statusToId: ");
        if (status == null) {
            return 0;
        }
        switch (status) {
            case IDEATION: return R.id.ideation_chip;
            case IN_PROGRESS: return R.id.implementation_chip;
            case COMPLETED: return R.id.done_chip;
        }
        return 0;
    }

    public static ProjectStatus idToStatus(int status) {
        Log.d("fg", "statusToId: ");
        return idProjectStatusMap.get(status);
    }

}
