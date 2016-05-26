package ayar.oktay.advancedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class AdvancedTextView extends TextView {
  private static final String DEFAULT_EXPAND_TEXT_COLOR = "#C5C5C5";
  private final float DEF_MIN_TEXT_SIZE =
      6 * getContext().getResources().getDisplayMetrics().density;

  // Attributes for text justification
  private boolean justifyText;

  // Attributes for expandable layout
  private boolean expandable;
  private String expandText;
  private int expandTextColor;
  private int expandTextSize;
  private int expandTextPosition;
  private int expandText_marginLeft;
  private int expandText_marginRight;
  private int expandText_marginTop;
  private int expandText_marginBottom;
  private boolean expandEllipsize;

  // Attributes for auto-fit
  private boolean autoFit;
  private int minTextSize;

  // Attributes for custom fonts
  private Font font;
  private String fontFile;

  // Local properties
  private String text;

  private boolean expandTextShown;
  private boolean heightSet;

  public AdvancedTextView(Context context) {
    super(context);

    // Set defaults
    justifyText = false;
    fontFile = null;
    expandable = false;
    expandTextColor = 0;
    expandText = null;
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

  public boolean isAutoFit() {
    return autoFit;
  }

  public void setAutoFit(boolean autoFit) {
    this.autoFit = autoFit;
  }

  public boolean isExpandable() {
    return expandable;
  }

  public void setExpandable(boolean expandable) {
    this.expandable = expandable;
  }

  public String getExpandText() {
    return expandText;
  }

  public void setExpandText(String expandText) {
    this.expandText = expandText;
  }

  public int getExpandTextColor() {
    return expandTextColor;
  }

  public void setExpandTextColor(int expandTextColor) {
    this.expandTextColor = expandTextColor;
  }

  public boolean isJustifyText() {
    return justifyText;
  }

  public void setJustifyText(boolean justifyText) {
    this.justifyText = justifyText;
  }

  public int getMinTextSize() {
    return minTextSize;
  }

  public void setMinTextSize(int minTextSize) {
    this.minTextSize = minTextSize;
  }

  public void setFontFile(String fontFile) {
    this.fontFile = fontFile;

    if (fontFile != null) {
      loadFont(fontFile);
    }
  }

  public Font getFont() {
    return font;
  }

  public void setFont(Font font) {
    this.font = font;

    if (font != null) {
      this.fontFile = font.getFileName();

      loadFont(this.fontFile);
    }
  }

  public boolean isExpandEllipsizeEnabled() {
    return expandEllipsize;
  }

  public void setExpandEllipsize(boolean expandEllipsize) {
    this.expandEllipsize = expandEllipsize;
  }

  public int getExpandTextPosition() {
    return expandTextPosition;
  }

  public void setExpandTextPosition(int expandTextPosition) {
    this.expandTextPosition = expandTextPosition;
  }

  public int getExpandTextSize() {
    return expandTextSize;
  }

  public void setExpandTextSize(int expandTextSize) {
    this.expandTextSize = expandTextSize;
  }

  @Override public void setText(CharSequence text, BufferType type) {
    super.setText(text, type);

    this.text = text.toString();
  }

  @Override protected void onDraw(Canvas canvas) {
    if (expandable && !expandTextShown) {
      initExpandable(this.expandText, this.expandTextColor);

      expandTextShown = true;
    }

    if (!justifyText && !autoFit) {
      super.onDraw(canvas);
    } else if (autoFit && justifyText) {
      float autoFitTextSize =
          getAutoFitTextSize(this.text, this.getMeasuredWidth(), this.getMaxLines(),
              this.minTextSize);
      Layout autoFitLayout = createAutoFitLayout(this.getLayout(), this.getPaint(), autoFitTextSize,
          this.getLineSpacingMultiplier(), this.getLineSpacingExtra());

      if (!heightSet) {
        int maxLines = this.getMaxLines();

        setHeight(autoFitLayout.getHeight() + getPaddingBottom() + getPaddingTop());

        this.setMaxLines(maxLines);

        heightSet = true;
      }

      drawJustified(canvas, autoFitLayout);
    } else if (autoFit) {
      drawAutoFitText(canvas);
    } else {
      drawJustified(canvas);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();

    init();
  }

  private void obtainAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    TypedArray arr = getContext().getTheme()
        .obtainStyledAttributes(attrs, R.styleable.AdvancedTextView, defStyleAttr, defStyleRes);

    // Obtain text justification attributes
    this.justifyText = arr.getBoolean(R.styleable.AdvancedTextView_justifyText, false);

    // Obtain expandable layout attributes
    this.expandable = arr.getBoolean(R.styleable.AdvancedTextView_expandable, false);
    this.expandText = arr.getString(R.styleable.AdvancedTextView_expandText);
    this.expandTextColor = arr.getColor(R.styleable.AdvancedTextView_expandTextColor,
        Color.parseColor(DEFAULT_EXPAND_TEXT_COLOR));
    this.expandTextSize = arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandTextSize,
        (int) this.getTextSize());
    this.expandTextPosition = arr.getInt(R.styleable.AdvancedTextView_expandTextPosition, 0);
    this.expandEllipsize = arr.getBoolean(R.styleable.AdvancedTextView_expandEllipsize, true);

    int expandText_margin =
        arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandText_margin, 0);
    this.expandText_marginLeft =
        arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandText_marginLeft,
            expandText_margin);
    this.expandText_marginRight =
        arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandText_marginRight,
            expandText_margin);
    this.expandText_marginTop =
        arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandText_marginTop,
            expandText_margin);
    this.expandText_marginBottom =
        arr.getDimensionPixelSize(R.styleable.AdvancedTextView_expandText_marginBottom,
            expandText_margin);

    // Obtain autoFit attributes
    this.autoFit = arr.getBoolean(R.styleable.AdvancedTextView_autoFit, false);
    this.minTextSize = arr.getDimensionPixelSize(R.styleable.AdvancedTextView_minTextSize,
        (int) DEF_MIN_TEXT_SIZE);

    // Obtain font file
    int font = arr.getInt(R.styleable.AdvancedTextView_font, -1);
    if (font >= 0 && Font.parse(font) != null) {
      this.fontFile = Font.parse(font).getFileName();
    } else {
      this.fontFile = arr.getString(R.styleable.AdvancedTextView_fontFile);
    }

    // recycle array
    arr.recycle();
  }

  private void init() {
    if (this.fontFile != null && fontFile.length() > 0) {
      // Load font
      loadFont(this.fontFile);
    }
  }

  private void loadFont(String fontFile) {
    Typeface font = Typeface.createFromAsset(getContext().getAssets(), fontFile);

    setTypeface(font);
  }

  private void drawJustified(Canvas canvas) {
    drawJustified(canvas, this.getLayout());
  }

  private void drawJustified(Canvas canvas, Layout layout) {
    TextPaint paint = this.getPaint();
    paint.setColor(this.getCurrentTextColor());
    paint.drawableState = this.getDrawableState();
    int mViewWidth = this.getMeasuredWidth();
    String text = (String) this.getText();

    for (int i = 0; i < layout.getLineCount(); ++i) {
      int lineStart = layout.getLineStart(i);
      int lineEnd = layout.getLineEnd(i);
      String line = text.substring(lineStart, lineEnd);

      if (this.needScale(line) && i < layout.getLineCount() - 1) {
        float width = StaticLayout.getDesiredWidth(line, this.getPaint());

        this.drawScaledText(canvas, lineStart, line, width, layout.getLineBaseline(i), mViewWidth);
      } else {
        canvas.drawText(line, 0.0F, layout.getLineBaseline(i), paint);
      }
    }
  }

  private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth,
      int mLineY, int mViewWidth) {
    float x = 0.0F;
    if (this.isFirstLineOfParagraph(lineStart, line)) {
      String d = "  ";
      canvas.drawText(d, x, (float) mLineY, this.getPaint());
      float i = StaticLayout.getDesiredWidth(d, this.getPaint());
      x += i;
      line = line.substring(3);
    }

    float var10 = ((float) mViewWidth - lineWidth) / (float) line.length();

    for (int var11 = 0; var11 < line.length(); ++var11) {
      String c = String.valueOf(line.charAt(var11));
      float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
      canvas.drawText(c, x, (float) mLineY, this.getPaint());
      x += cw + var10;
    }
  }

  private boolean isFirstLineOfParagraph(int lineStart, String line) {
    return line.length() > 3 && line.charAt(0) == 32 && line.charAt(1) == 32;
  }

  private boolean needScale(String line) {
    return line.length() == 0 ? false : line.charAt(line.length() - 1) != 10;
  }

  private void initExpandable(String expandText, int expandTextColor)
      throws IllegalAttributeException, IlleagalUsageException {

    if (!(getParent() instanceof ExpandableTextLayout)) {
      throw new IlleagalUsageException("Expand feature can be only used in ExpandableTextLayout");
    }

    if (expandText == null || expandText.length() == 0) {
      throw new IllegalAttributeException("expandText must be specified!!...");
    }

    if (autoFit) {
      throw new IlleagalUsageException("Expandable feature cannot be used with auto-fit feature");
    }

    if (this.getMaxLines() < 1 || this.getMaxLines() == Integer.MAX_VALUE) {
      return;
    }

    ExpandableTextLayout expandableTextLayout = (ExpandableTextLayout) getParent();
    TextView expandView = (TextView) LayoutInflater.from(getContext())
        .inflate(R.layout.layout_expand_text, expandableTextLayout, false);

    // Initialize expand view
    expandView.setText(this.expandText);
    expandView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.expandTextSize);
    expandView.setTextColor(this.expandTextColor);

    ViewGroup.MarginLayoutParams params =
        (ViewGroup.MarginLayoutParams) expandView.getLayoutParams();

    params.leftMargin = this.expandText_marginLeft;
    params.rightMargin = this.expandText_marginRight;
    params.topMargin = this.expandText_marginTop;
    params.bottomMargin = this.expandText_marginBottom;

    if (this.expandTextPosition == 0) {
      expandView.setGravity(Gravity.LEFT);
    } else if (this.expandTextPosition == 1) {
      expandView.setGravity(Gravity.CENTER_HORIZONTAL);
    } else if (this.expandTextPosition == 2) {
      expandView.setGravity(Gravity.RIGHT);
    }

    if (this.expandEllipsize) {
      this.setEllipsize(TextUtils.TruncateAt.END);
    }

    expandableTextLayout.initExpandText(expandView, this);

  }

  private void drawAutoFitText(Canvas canvas) {
    float autoFitTextSize =
        getAutoFitTextSize(this.text, this.getMeasuredWidth(), this.getMaxLines(),
            this.minTextSize);
    Layout autoFitLayout = createAutoFitLayout(this.getLayout(), this.getPaint(), autoFitTextSize,
        this.getLineSpacingMultiplier(), this.getLineSpacingExtra());

    if (!heightSet) {
      int maxLines = this.getMaxLines();

      setHeight(autoFitLayout.getHeight() + getPaddingBottom() + getPaddingTop());

      this.setMaxLines(maxLines);

      heightSet = true;
    }

    for (int i = 0; i < autoFitLayout.getLineCount(); i++) {
      int lineStart = autoFitLayout.getLineStart(i);
      int lineEnd = autoFitLayout.getLineEnd(i);
      String line = text.substring(lineStart, lineEnd);

      canvas.drawText(line, 0.0F, autoFitLayout.getLineBaseline(i), autoFitLayout.getPaint());
    }
  }

  private float getAutoFitTextSize(String text, int viewWidth, int maxLines, int minFontSize) {

    if (maxLines < 1 || maxLines == Integer.MAX_VALUE) {
      throw new IllegalAttributeException(
          "maxLines (" + maxLines + ") must be valid to use auto-fit feature");
    }

    final float DELTA = 1.0F;

    TextPaint paint = this.getPaint();
    paint.setColor(this.getCurrentTextColor());
    paint.drawableState = this.getDrawableState();

    Layout layout = getLayout();

    int lineCount = layout.getLineCount();

    float textSize = paint.getTextSize();

    if (textSize < minFontSize) {
      throw new IllegalAttributeException("Text size cannot be smaller than minTextSize");
    }

    while (lineCount > maxLines && textSize > minFontSize) {
      textSize -= DELTA;

      paint.setTextSize(textSize);

      lineCount = createAutoFitLayout(layout, paint, textSize, this.getLineSpacingMultiplier(),
          this.getLineSpacingExtra()).getLineCount();
    }

    if (textSize == minFontSize && lineCount > maxLines) {
      setMaxLines(lineCount);
    }

    return textSize;
  }

  private StaticLayout createAutoFitLayout(Layout currentLayout, TextPaint paint,
      float autoFitTextSize, float spacingmult, float spacingadd) {

    paint.setTextSize(autoFitTextSize);

    return new StaticLayout(this.text, paint, currentLayout.getWidth(),
        currentLayout.getAlignment(), spacingmult, spacingadd, false);
  }

  private int computeHeight() {
    float autoFitTextSize =
        getAutoFitTextSize(this.text, this.getMeasuredWidth(), this.getMaxLines(),
            this.minTextSize);
    Layout autoFitLayout = createAutoFitLayout(this.getLayout(), this.getPaint(), autoFitTextSize,
        this.getLineSpacingMultiplier(), this.getLineSpacingExtra());

    return autoFitLayout.getHeight() + this.getPaddingBottom() + this.getPaddingTop();
  }
}
