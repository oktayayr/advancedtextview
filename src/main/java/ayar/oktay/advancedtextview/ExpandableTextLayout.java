package ayar.oktay.advancedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class ExpandableTextLayout extends LinearLayout implements View.OnClickListener {
  private AdvancedTextView expandTarget;
  private boolean expanded;
  private OnClickListener expandTextOnClickListener;

  public ExpandableTextLayout(Context context) {
    this(context, null);
  }

  public ExpandableTextLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpandableTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    setOrientation(VERTICAL);

    expandTarget = null;
    expanded = false;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableTextLayout(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    setOrientation(VERTICAL);

    expandTarget = null;
    expanded = false;
  }

  public void setExpandTextOnClickListener(OnClickListener expandTextOnClickListener) {
    this.expandTextOnClickListener = expandTextOnClickListener;
  }

  public TextView getExpandView() {
    return (TextView) getChildAt(1);
  }

  public void hideExpandView() {
    getChildAt(1).setVisibility(GONE);
  }

  public void initExpandText(TextView expandView, AdvancedTextView atv) {
    expandView.setClickable(true);
    expandView.setOnClickListener(this);

    if (getChildAt(1) == null) {
      addView(expandView);
    } else {
      TextView orgExpandView = getExpandView();

      orgExpandView.setText(expandView.getText());
      orgExpandView.setTextSize(TypedValue.COMPLEX_UNIT_PX, expandView.getTextSize());
      orgExpandView.setTextColor(expandView.getCurrentTextColor());

      MarginLayoutParams layoutParams = (MarginLayoutParams) expandView.getLayoutParams();
      MarginLayoutParams orgLayoutParams = (MarginLayoutParams) orgExpandView.getLayoutParams();

      orgLayoutParams.leftMargin = layoutParams.leftMargin;
      orgLayoutParams.rightMargin = layoutParams.rightMargin;
      orgLayoutParams.topMargin = layoutParams.topMargin;
      orgLayoutParams.bottomMargin = layoutParams.bottomMargin;

      orgExpandView.setGravity(expandView.getGravity());
      orgExpandView.setEllipsize(expandView.getEllipsize());

      orgExpandView.setVisibility(VISIBLE);
      orgExpandView.requestLayout();

    }

    this.expandTarget = atv;
  }

  @Override public void onClick(View v) {
    if (!expanded) {
      expandTarget.setMaxLines(Integer.MAX_VALUE);
      expandTarget.requestLayout();

      expanded = true;

      getExpandView().setVisibility(GONE);

      if(expandTextOnClickListener!=null){
        expandTextOnClickListener.onClick(v);
      }
    }
  }
}
