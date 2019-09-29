package unit.rest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import unit.api.ConversionSearchResult;

public class ConversionModule extends Module {

	@Override
	public String getModuleName() {
		return "ConversionModule";
	}

	@Override
	public Version version() {
		return Version.unknownVersion();
	}

	private final SimpleSerializers serializers = new SimpleSerializers();

	public ConversionModule() {
		serializers.addSerializer(ConversionSearchResult.class, new JsonSerializer<ConversionSearchResult>() {
			@Override
			public void serialize(ConversionSearchResult conversionSearchResult, JsonGenerator jsonGen,
					SerializerProvider serializerProvider) throws IOException {
				jsonGen.writeStartObject(conversionSearchResult);
				jsonGen.writeBooleanField("success", conversionSearchResult.isSuccess());
				jsonGen.writeStringField("message", conversionSearchResult.getMessage());

			}
		});
	}

	@Override
	public void setupModule(final SetupContext context) {
		context.addSerializers(serializers);
	}
}
