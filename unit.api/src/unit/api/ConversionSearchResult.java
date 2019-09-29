package unit.api;


import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public class ConversionSearchResult {

	private final boolean success;
	private final String message;
	
	public ConversionSearchResult(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
}