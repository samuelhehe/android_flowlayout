package com.samuelnotes.widget.flowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	private static final int ALIGN_TOP = 0;

	private static final int ALIGN_CENTER = 1;

	private static final int ALIGN_BOTTOM = 2;

	private int tag_align = ALIGN_CENTER;

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.flowlayout);
		tag_align = ta.getInt(R.styleable.flowlayout_tag_align, ALIGN_CENTER);
		ta.recycle();

	}

	/**
	 * 存储所有View
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();

	/**
	 * 存储所有行的行高
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLineHeight.clear();

		// / 获取ViewGroup 的宽度
		int width = getWidth();

		int lineWidth = 0; // / 行宽
		int lineHeight = 0; // / 行高

		// / 每行的Views
		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);

			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			
			// //换行
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
					- getPaddingLeft() - getPaddingRight()) {
				mLineHeight.add(lineHeight);
				mAllViews.add(lineViews);
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				lineViews = new ArrayList<View>();
			}
			// // 不换行 ,叠加宽度
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			// // 计算出一行中最高的一个控件的高度作为行高
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}
		// the last line
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		// / left , top  计算出ViewGroup 的padding值在内
		int left = getPaddingLeft();
		int top = getPaddingTop();
		/// 对每一行的子控件进行布局
		int lineNum = mAllViews.size();
		for (int i = 0; i < lineNum; i++) {
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				// / verify child state
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				int lc = left + lp.leftMargin;

				// align top

				int tc = top + lp.topMargin	+ ((lineHeight / 2) - (child.getMeasuredHeight() / 2));
				if (tag_align == ALIGN_TOP) {
					tc = top + lp.topMargin;
				} else if (tag_align == ALIGN_CENTER) {
					// align vertical center
					tc = top+ lp.topMargin + ((lineHeight / 2) - (child.getMeasuredHeight() / 2));
				} else if (tag_align == ALIGN_BOTTOM) {
					// align bottom
					tc = top + lp.topMargin + (lineHeight - child.getMeasuredHeight());
				}
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();
				
				child.layout(lc, tc, rc, bc);
				
				left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			}
			left = getPaddingLeft();
			top += lineHeight;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// / for wrap_content width , height
		int width = 0;
		int height = 0;  

		int lineWidth = 0;

		int lineHeight = 0;

		int cCount = getChildCount();
		
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth()+ lp.leftMargin+ lp.rightMargin;
			int childHeight = child.getMeasuredHeight()+ lp.topMargin+ lp.bottomMargin;

			
			if (lineWidth + childWidth > sizeWidth - getPaddingLeft()- getPaddingRight()) {
				// 对比得到最大的宽度
				width = Math.max(width, lineWidth);
				// 重置行宽
				lineWidth = childWidth;
				// 添加新行
				height += lineHeight;
				lineHeight = childHeight;
			} else { // / 未换行
				lineWidth += childWidth;
				/// 计算子行高的最高控件的大小作为行高
				lineHeight = Math.max(lineHeight, childHeight);
			}
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}
		// Log.i("TAG", sizeWidth + " sizeHeight " + sizeHeight);
		setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth
				: width + getPaddingLeft() + getPaddingRight(),
				modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
						+ getPaddingTop() + getPaddingBottom());
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
}
