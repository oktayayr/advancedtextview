package ayar.oktay.advancedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class ExpandableTextLayout extends LinearLayout implements View.OnClickListener {
  private AdvancedTextView expandTarget;
  private boolean expanded;

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

  public void initExpandText(TextView expandView,
      AdvancedTextView atv) {

    expandView.setClickable(true);
    expandView.setOnClickListener(this);

    addView(expandView);

    this.expandTarget = atv;
  }

  @Override public void onClick(View v) {
    if (!expanded) {
      expandTarget.setMaxLines(Integer.MAX_VALUE);
      expandTarget.requestLayout();

      expanded = true;

      this.removeViewAt(1);
    }
  }
}
