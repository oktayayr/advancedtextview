package ayar.oktay.advancedtextview;

/**
 * Created by Oktay AYAR on 5/20/16.
 */
public class IlleagalUsageException extends RuntimeException {
  public IlleagalUsageException() {
  }

  public IlleagalUsageException(String detailMessage) {
    super(detailMessage);
  }

  public IlleagalUsageException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public IlleagalUsageException(Throwable throwable) {
    super(throwable);
  }
}
