package unit.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConversionSearch {

	private static final String DEFAULT_MESSAGE = "Wrong format: Check that conversion name is correct and that the q value is valid";
	private Collection<Conversion> conversions = new ArrayList<Conversion>();
	
	public void addConversion(Conversion conv) {
		conversions.add(conv);
	}

	public void removeConversion(Conversion conv) {
		conversions.remove(conv);
	}
	
	public ConversionSearch(Conversion... convs) {
		conversions.addAll(Arrays.asList(convs));
	}
	
	private ConversionSearchResult search(String value, Iterable<Conversion> conversions) {
		StringBuilder messages = new StringBuilder();
		boolean success = false;
		for (Conversion conv : conversions) {
			ConversionSearchResult result = conv.convert(value);
			if (result.isSuccess()) {
				messages.append(result.getMessage());
				success = true;
			}
		}
		if (messages.length() == 0) {
			messages.append(DEFAULT_MESSAGE);
		}
		return new ConversionSearchResult(success, messages.toString());
	}

	public ConversionSearchResult search(String value, String convKey) {
		System.out.println("Search m flere");
		return search(convKey, conversions.stream().filter(convs -> convs.getConversionName().equals(value)).collect(Collectors.toList()));
	}

	public ConversionSearchResult search(String value) {
		System.out.println("Search m en");
		return search(value, conversions);
	}
}