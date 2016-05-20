package ayar.oktay.advancedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class AdvancedTextView extends TextView {
  public AdvancedTextView(Context context) {
    super(context);
  }

  public AdvancedTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AdvancedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AdvancedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
}
