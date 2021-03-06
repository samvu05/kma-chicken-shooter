package com.example.chickenshooter;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.example.chickenshooter.databinding.ActivityGameOverBindingImpl;
import com.example.chickenshooter.databinding.ActivityHelpBindingImpl;
import com.example.chickenshooter.databinding.ActivityHighscoreBindingImpl;
import com.example.chickenshooter.databinding.ActivityMainBindingImpl;
import com.example.chickenshooter.databinding.ActivitySettingBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYGAMEOVER = 1;

  private static final int LAYOUT_ACTIVITYHELP = 2;

  private static final int LAYOUT_ACTIVITYHIGHSCORE = 3;

  private static final int LAYOUT_ACTIVITYMAIN = 4;

  private static final int LAYOUT_ACTIVITYSETTING = 5;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(5);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.chickenshooter.R.layout.activity_game_over, LAYOUT_ACTIVITYGAMEOVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.chickenshooter.R.layout.activity_help, LAYOUT_ACTIVITYHELP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.chickenshooter.R.layout.activity_highscore, LAYOUT_ACTIVITYHIGHSCORE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.chickenshooter.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.chickenshooter.R.layout.activity_setting, LAYOUT_ACTIVITYSETTING);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYGAMEOVER: {
          if ("layout/activity_game_over_0".equals(tag)) {
            return new ActivityGameOverBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_game_over is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYHELP: {
          if ("layout/activity_help_0".equals(tag)) {
            return new ActivityHelpBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_help is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYHIGHSCORE: {
          if ("layout/activity_highscore_0".equals(tag)) {
            return new ActivityHighscoreBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_highscore is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSETTING: {
          if ("layout/activity_setting_0".equals(tag)) {
            return new ActivitySettingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_setting is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout/activity_game_over_0", com.example.chickenshooter.R.layout.activity_game_over);
      sKeys.put("layout/activity_help_0", com.example.chickenshooter.R.layout.activity_help);
      sKeys.put("layout/activity_highscore_0", com.example.chickenshooter.R.layout.activity_highscore);
      sKeys.put("layout/activity_main_0", com.example.chickenshooter.R.layout.activity_main);
      sKeys.put("layout/activity_setting_0", com.example.chickenshooter.R.layout.activity_setting);
    }
  }
}
