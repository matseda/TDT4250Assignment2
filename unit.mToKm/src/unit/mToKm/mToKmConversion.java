package unit.mToKm;

import org.osgi.service.component.annotations.*;
import unit.util.UnitConversion;
import unit.api.Conversion;

@Component(
		property = {
				UnitConversion.CONVERSION_NAME_PROP + "=mToKm",
				UnitConversion.CONVERSION_FORMULA_PROP + "=*1000"}
		)
public class mToKmConversion extends UnitConversion implements Conversion {
}
