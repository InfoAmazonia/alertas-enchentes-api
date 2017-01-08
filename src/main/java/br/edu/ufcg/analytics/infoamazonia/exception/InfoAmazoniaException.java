/**
 * 
 */
package br.edu.ufcg.analytics.infoamazonia.exception;

/**
 * @author Ricardo Araujo Santos - ricoaraujosantos@gmail.com
 */
public class InfoAmazoniaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5274676873464840320L;

	/**
	 * 
	 */
	public InfoAmazoniaException() {
		super();
	}

	/**
	 * @param message
	 */
	public InfoAmazoniaException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InfoAmazoniaException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InfoAmazoniaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InfoAmazoniaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
