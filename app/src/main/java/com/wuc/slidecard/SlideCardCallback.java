package com.wuc.slidecard;

import android.graphics.Canvas;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.wuc.slidecard.adapter.UniversalAdapter;
import java.util.List;

/**
 * @author : wuchao5
 * @date : 2021/11/26 16:36
 * @desciption : 拖拽滑动
 */
public class SlideCardCallback extends ItemTouchHelper.SimpleCallback {

  private RecyclerView mRv;
  private UniversalAdapter<SlideCardBean> adapter;
  private List<SlideCardBean> mDatas;

  public SlideCardCallback(RecyclerView mRv,
      UniversalAdapter<SlideCardBean> adapter,
      List<SlideCardBean> mDatas) {
    // 参数一:dragDirs 拖拽
    // 参数二:swipeDirs 滑动
    // 不用拖拽,直接给 0，填 15 就表示可以上下左右滑动
    super(0, 15);
    this.mRv = mRv;
    this.adapter = adapter;
    this.mDatas = mDatas;
  }

  /**
   * 拖拽使用
   */
  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
    return false;
  }

  /**
   * 滑动结束后的处理
   */
  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    /// 先删除当前滑动的View,然后在添加到最后一个,造成循环滑动的效果
    // 当前滑动的View下标
    int layoutPosition = viewHolder.getLayoutPosition();
    // 删除当前滑动的元素
    SlideCardBean remove = mDatas.remove(layoutPosition);
    // 添加到集合第 0 个位置 造成循环滑动的效果
    mDatas.add(0, remove);
    adapter.notifyDataSetChanged();// onMeasure, onlayout
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    double maxDistance = recyclerView.getWidth() * 0.5f;
    // dx = 当前滑动 x 位置
    // dy = 当前滑动 y 位置
    // sqrt 开根号
    double sqrt = Math.sqrt(dX * dX + dY * dY);
    // 放大系数
    double scaleRatio = sqrt / maxDistance;
    // 系数最大为1
    if (scaleRatio > 1.0) {
      scaleRatio = 1.0;
    }
    // 显示的个数
    int childCount = recyclerView.getChildCount();
    // 循环所有数据
    for (int i = 0; i < childCount; i++) {
      View view = recyclerView.getChildAt(i);
      /*
       *   childCount - 1 =  itemView 总个数
       *   childCount - 1 - i = itemView 总个数 - i = 从最后一个开始
       *
       * 假设 childCount - 1 = 7
       *     i累加
       *     那么level = childCount - 1 - 0 = 7
       *     那么level = childCount - 1 - 1 = 6
       *     那么level = childCount - 1 - 2 = 5
       *     那么level = childCount - 1 - 3 = 4
       *     那么level = childCount - 1 - 4 = 3
       *      。。。。
       */
      int level = childCount - 1 - i;
      if (level > 0) {
        // 最大显示叠加个数:CardConfig.MAX_SHOW_COUNT = 4
        if (level < CardConfig.MAX_SHOW_COUNT - 1) {
          // 缩放比例: CardConfig.SCALE_GAP = 0.05
          float scale = CardConfig.SCALE_GAP;
          // CardConfig.TRANS_Y_GAP * level  = 原始平移距离
          // scaleRatio * CardConfig.TRANS_Y_GAP = 平移系数
          // CardConfig.TRANS_Y_GAP * level - scaleRatio * CardConfig.TRANS_Y_GAP = 手指滑动过程中的Y轴平移距离
          // 因为是Y轴,所以向上平移是 - 号
          view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * level - scaleRatio * CardConfig.TRANS_Y_GAP));
          // 1 - scale * level = 原始缩放大小
          // scaleRatio * scale = 缩放系数
          // 因为是需要放大,所以这里是 + 号
          double v = (1 - scale * level) + (scaleRatio * scale);
          view.setScaleX((float) v);
          view.setScaleY((float) v);
        }
      }
    }
  }

  /**
   * 设置回弹距离
   */
  @Override
  public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
    return 0.3f;
  }

  /**
   * 设置回弹时间
   */
  @Override
  public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
    return 1500;
  }
}