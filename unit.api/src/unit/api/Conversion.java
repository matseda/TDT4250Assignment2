package unit.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Conversion {
	String getConversionName();
	String getConversionFormula();
	ConversionSearchResult convert(String convKey);
}