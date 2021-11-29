package com.wuc.slidecard;

import android.content.Context;
import android.util.TypedValue;

public class CardConfig {
  /**
   * 屏幕上最多同时显示几个Item
   */
  public static int MAX_SHOW_COUNT;

  /**
   * 缩放的大小 每一级 Scale 相差 0.05f，translationY 相差 7dp 左右
   */
  public static float SCALE_GAP;
  /**
   * item 平移Y轴距
   */
  public static int TRANS_Y_GAP;

  public static void initConfig(Context context) {
    MAX_SHOW_COUNT = 4;
    SCALE_GAP = 0.05f;
    // 把非标准尺寸转换成标准尺寸
    TRANS_Y_GAP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
  }
}
