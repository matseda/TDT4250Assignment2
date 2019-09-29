package unit.util;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.*;
import unit.api.Conversion;
import unit.api.ConversionSearchResult;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.ScriptEngine;

@Component(
		configurationPid = UnitConversion.FACTORY_PID,
		configurationPolicy = ConfigurationPolicy.REQUIRE
		)
public class UnitConversion implements Conversion {

	public static final String FACTORY_PID = "unit.util.UnitConversion";
	
	public static final String CONVERSION_NAME_PROP = "conversionName";
	public static final String CONVERSION_FORMULA_PROP = "conversionFormula";
	
	private String name;
	private String formula;
	
	@Override
	public String getConversionName() {
		return name;
	}

	protected void setConversionName(String name) {
		this.name = name;
	}
	
	@Override
	public String getConversionFormula() {
		return formula;
	}

	protected void setConversionFormula(String formula) {
		this.formula = formula;
	}
	
	public @interface UnitConversionConfig {
		String conversionName() default  "";
		String conversionFormula();
	}

	@Activate
	public void activate(BundleContext bc, UnitConversionConfig config) {
		update(bc, config);
	}

	@Modified
	public void modify(BundleContext bc, UnitConversionConfig config) {
		update(bc, config);		
	}

	protected void update(BundleContext bc, UnitConversionConfig config) {
		setConversionName(config.conversionName());
		setConversionFormula(config.conversionFormula());
	}
	
	protected String getSuccessMessageStringFormat() {
		return "Successfully converted using %s, the result is: %s";
	}

	protected String getFailureMessageStringFormat() {
		return "Conversion failed!";
	}
	
	public ConversionSearchResult convert(String convKey) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		String code = convKey + formula;
		Object result = null;
		try {
			result = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		if (result != null) {
			return new ConversionSearchResult(true, String.format(getSuccessMessageStringFormat(), name, result));
		} else {
			return new ConversionSearchResult(false, getFailureMessageStringFormat());
		}
	}
}
