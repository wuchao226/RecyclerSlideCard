package com.wuc.slidecard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : wuchao5
 * @date : 2021/11/26 14:48
 * @desciption : 自定义 LayoutManager
 */
public class SlideCardLayoutManager extends RecyclerView.LayoutManager {
  /**
   * 仿造着 LinearLayoutManager() 来写
   */
  @Override
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  /**
   * 必须重写 在 RecyclerView->OnLayout()时候调用,用来摆放 Item位置
   */
  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    super.onLayoutChildren(recycler, state);
    // 调用 RecyclerView 的缓存机制 缓存 ViewHolder
    detachAndScrapAttachedViews(recycler);
    // 最下面图片下标
    int bottomPosition = 0;
    // 获取所有图片
    int itemCount = getItemCount();
    if (itemCount > CardConfig.MAX_SHOW_COUNT) {
      // 获取到从第几张开始
      bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
    }
    for (int i = bottomPosition; i < itemCount; i++) {
      // 获取当前view宽高
      View view = recycler.getViewForPosition(i);
      // 把当前 View 添加到 RecyclerView 中
      addView(view);
      // 测量
      measureChildWithMargins(view, 0, 0);
      // getWidth() RecyclerView 宽
      // getDecoratedMeasuredWidth() View 的宽
      int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
      int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

      // LinearLayoutManager#layoutChunk#layoutDecoratedWithMargins
      // 绘制布局 ---draw -- onDraw ,onDrawOver, onLayout
      layoutDecoratedWithMargins(view, widthSpace / 2,
          heightSpace / 2, widthSpace / 2 + getDecoratedMeasuredWidth(view),
          heightSpace / 2 + getDecoratedMeasuredHeight(view));

      // itemCount - 1  = 最后一个元素
      // 最后一个元素 - i = 倒数的元素
      int level = itemCount - 1 - i;
      // ItemView 的叠加摆放并有缩放层级
      if (level > 0) {
        // 如果不是最后一个才缩放
        if (level < CardConfig.MAX_SHOW_COUNT - 1) {
          // 平移
          view.setTranslationY(CardConfig.TRANS_Y_GAP * level);
          // 缩放
          view.setScaleX(1 - CardConfig.SCALE_GAP * level);
          view.setScaleY(1 - CardConfig.SCALE_GAP * level);
        } else {
          // 最下面的View 和前一个View布局一样(level - 1)
          // 平移
          view.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
          // 缩放
          view.setScaleX(1 - CardConfig.SCALE_GAP * (level - 1));
          view.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
        }
      }
    }
  }
}