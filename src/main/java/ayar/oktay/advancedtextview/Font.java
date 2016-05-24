package ayar.oktay.advancedtextview;

/**
 * Created by Oktay AYAR on 5/24/16.
 */
public enum Font {

  ;

  private static final String FOLDER = "/assets/";
  private static final String EXT = ".ttf";

  private int value;

  Font(int value) {
    this.value = value;
  }

  public static Font parse(int value) {
    for (Font font : Font.values()) {
      if (font.getValue() == value) {
        return font;
      }
    }

    return null;
  }

  public int getValue() {
    return value;
  }

  public String getFileName() {
    return FOLDER + this.toString().replace("_", "") + EXT;
  }
}
