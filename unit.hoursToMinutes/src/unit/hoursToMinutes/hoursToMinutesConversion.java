package unit.hoursToMinutes;

import org.osgi.service.component.annotations.*;
import unit.util.UnitConversion;
import unit.api.Conversion;

@Component(
		property = {
				UnitConversion.CONVERSION_NAME_PROP + "=hoursToMinutes",
				UnitConversion.CONVERSION_FORMULA_PROP + "=*60"}
		)
public class hoursToMinutesConversion extends UnitConversion implements Conversion {
}
