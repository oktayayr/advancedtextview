package ayar.oktay.advancedtextview;

/**
 * Created by Oktay AYAR on 5/24/16.
 */
public enum Font {
  Aller_Bold(0),
  Aller_Bold_Italic(1),
  Aller_Display(2),
  Aller_Light(3),
  Aller_Light_Italic(4),
  Aller_Regular(5),
  Amatic_Regular(6),
  Amatic_Bold(7),
  Bebas(8),
  Capture(10),
  Caviar_Dreams(11),
  Caviar_Dreams_Bold(12),
  Caviar_Dreams_Bold_Italic(13),
  Caviar_Dreams_Italic(14),
  DroidSans(15),
  DroidSans_Bold(16),
  Journal(17),
  OpenSans_Bold(18),
  OpenSans_Bold_Italic(19),
  OpenSans_Italic(20),
  OpenSans_Light(21),
  OpenSans_Light_Italic(22),
  OpenSans_Regular(23),
  Pacifico(24),
  Roboto_Regular(25),
  Roboto_Bold(26),
  Roboto_Bold_Italic(27),
  Roboto_Italic(28),
  Roboto_Light(29),
  Roboto_Light_Italic(30),
  Roboto_Medium(31),
  Roboto_Medium_Italic(32),
  Windsong(33)
  ;

  private static final String FOLDER = "fonts/";
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
