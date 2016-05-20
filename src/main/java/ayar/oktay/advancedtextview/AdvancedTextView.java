package ayar.oktay.advancedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class AdvancedTextView extends TextView {
  private static final String ROBOTO_REGULAR = "fonts/Roboto-Medium.ttf";
  private static final String ROBOTO_MEDIUM = "fonts/Roboto-Regular.ttf";
  private static final String[] FONT_FILES = new String[] { ROBOTO_REGULAR, ROBOTO_MEDIUM };

  private boolean justifyText;
  private String fontFile;
  private String text;

  private int mViewWidth;
  private int mLineY;

  public AdvancedTextView(Context context) {
    super(context);

    // Set defaults
    justifyText = false;
  }

  public AdvancedTextView(Context context, AttributeSet attrs) {
    super(context, attrs);

    obtainAttributes(attrs, 0, 0);
  }

  public AdvancedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    obtainAttributes(attrs, defStyleAttr, 0);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AdvancedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    obtainAttributes(attrs, defStyleAttr, defStyleRes);
  }

  @Override public void setText(CharSequence text, BufferType type) {
    super.setText(text, type);

    this.text = text.toString();
  }

  @Override protected void onDraw(Canvas canvas) {
    if (!justifyText) {
      super.onDraw(canvas);
    } else {
      drawJustified(canvas);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    if (this.fontFile != null && fontFile.length() > 0) {
      // Load font
      loadFont(this.fontFile);
    }
  }

  private void obtainAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    TypedArray arr = getContext().getTheme()
        .obtainStyledAttributes(attrs, R.styleable.AdvancedTextView, defStyleAttr, defStyleRes);

    this.justifyText = arr.getBoolean(R.styleable.AdvancedTextView_justifyText, false);

    // Obtain font file
    int font = arr.getInt(R.styleable.AdvancedTextView_font, 0);
    if (font > 0) {
      this.fontFile = FONT_FILES[font - 1];
    } else {
      fontFile = arr.getString(R.styleable.AdvancedTextView_fontFile);
    }
  }

  private void loadFont(String fontFile) {
    Typeface font = Typeface.createFromAsset(getContext().getAssets(), fontFile);

    setTypeface(font);
  }

  private void drawJustified(Canvas canvas) {
    TextPaint paint = this.getPaint();
    paint.setColor(this.getCurrentTextColor());
    paint.drawableState = this.getDrawableState();
    this.mViewWidth = this.getMeasuredWidth();
    String text = (String) this.getText();
    this.mLineY = 0;
    this.mLineY = (int) ((float) this.mLineY + this.getTextSize());
    Layout layout = this.getLayout();

    for (int i = 0; i < layout.getLineCount(); ++i) {
      int lineStart = layout.getLineStart(i);
      int lineEnd = layout.getLineEnd(i);
      String line = text.substring(lineStart, lineEnd);

      if (this.needScale(line) && i < layout.getLineCount() - 1) {
        float width = StaticLayout.getDesiredWidth(line, this.getPaint());

        this.drawScaledText(canvas, lineStart, line, width);
      } else {
        canvas.drawText(line, 0.0F, (float) this.mLineY, paint);
      }

      this.mLineY += this.getLineHeight();
    }
  }

  private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
    float x = 0.0F;
    if (this.isFirstLineOfParagraph(lineStart, line)) {
      String d = "  ";
      canvas.drawText(d, x, (float) this.mLineY, this.getPaint());
      float i = StaticLayout.getDesiredWidth(d, this.getPaint());
      x += i;
      line = line.substring(3);
    }

    float var10 = ((float) this.mViewWidth - lineWidth) / (float) line.length();

    for (int var11 = 0; var11 < line.length(); ++var11) {
      String c = String.valueOf(line.charAt(var11));
      float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
      canvas.drawText(c, x, (float) this.mLineY, this.getPaint());
      x += cw + var10;
    }
  }

  private boolean isFirstLineOfParagraph(int lineStart, String line) {
    return line.length() > 3 && line.charAt(0) == 32 && line.charAt(1) == 32;
  }

  private boolean needScale(String line) {
    return line.length() == 0 ? false : line.charAt(line.length() - 1) != 10;
  }
}
