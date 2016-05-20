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
  private TextView expandTv;
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

    expandTv = null;
    expandTarget = null;
    expanded = false;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableTextLayout(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    setOrientation(VERTICAL);

    expandTv = null;
    expandTarget = null;
    expanded = false;
  }

  public void initExpandText(String expandText, int expandTextColor, float textSize,
      AdvancedTextView atv) {
    float density = getContext().getResources().getDisplayMetrics().density;

    expandTv = (TextView) LayoutInflater.from(getContext())
        .inflate(R.layout.layout_expand_text, this, false);

    expandTv.setText(expandText);
    expandTv.setTextColor(expandTextColor);
    expandTv.setTextSize(textSize);
    expandTv.setPadding(0, (int) ((float) atv.getLineHeight() / density), 0, 0);
    expandTv.setClickable(true);
    expandTv.setOnClickListener(this);

    addView(expandTv);

    this.expandTarget = atv;
  }

  @Override public void onClick(View v) {
    if (!expanded) {
      expandTarget.setMaxLines(Integer.MAX_VALUE);
      expandTarget.requestLayout();

      expanded = true;

      expandTv.setVisibility(GONE);
    }
  }
}
