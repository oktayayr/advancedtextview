package ayar.oktay.advancedtextview;

/**
 * Created by Oktay AYAR on 5/25/16.
 */
public class IllegalAttributeException extends RuntimeException {
  public IllegalAttributeException() {
  }

  public IllegalAttributeException(String detailMessage) {
    super(detailMessage);
  }

  public IllegalAttributeException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public IllegalAttributeException(Throwable throwable) {
    super(throwable);
  }
}
