package unit.gogo;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import unit.api.Conversion;
import unit.util.UnitConversion;


@Component(
		service = ConversionCommands.class,
		property = {
			"osgi.command.scope=conversion",
			"osgi.command.function=list",
			"osgi.command.function=add",
			"osgi.command.function=remove"
		}
	)
public class ConversionCommands {

	private Configuration getConfig(String convName) {
		try {
			Configuration[] configs = cm.listConfigurations("(&(" + UnitConversion.CONVERSION_NAME_PROP + "=" + convName + ")(service.factoryPid=" + UnitConversion.FACTORY_PID + "))");
			if (configs != null && configs.length >= 1) {
				return configs[0];
			}
		} catch (IOException | InvalidSyntaxException e) {
		}
		return null;
	}

	@Descriptor("list conversions")
	public void list() {
		System.out.print("Conversions: ");
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			for (ServiceReference<Conversion> serviceReference : bc.getServiceReferences(Conversion.class, null)) {
				Conversion conv = bc.getService(serviceReference);
				try {
					if (conv != null) {
						System.out.print(conv.getConversionName());
						if (getConfig(conv.getConversionName()) != null) {
							System.out.print("*");						
						}
					}
				} finally {
					bc.ungetService(serviceReference);
				}
				System.out.print(" ");
			}
		} catch (InvalidSyntaxException e) {
		}
		System.out.println();
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private ConfigurationAdmin cm;

	@Descriptor("add a conversion with name and formula")
	public void add(
			@Descriptor("the name of the conversion")
			String name,
			@Descriptor("the formula for the conversion")
			String formula
			
			) throws IOException, InvalidSyntaxException {

		String actionName = "updated";
		// lookup existing configuration
		Configuration config = getConfig(name);
		if (config == null) {
			// create a new one
			config = cm.createFactoryConfiguration(UnitConversion.FACTORY_PID, "?");
			actionName = "added";
		}
		Dictionary<String, String> props = new Hashtable<>();
		props.put(UnitConversion.CONVERSION_NAME_PROP, name);
		if (formula != "" || formula != null) {
			props.put(UnitConversion.CONVERSION_FORMULA_PROP, formula);
		}
		config.update(props);
		System.out.println("\"" + name + "\" conversion " + actionName);
	}
	
	@Descriptor("remove a conversion")
	public void remove(
			@Descriptor("the name of the conversion")
			String name
			) throws IOException, InvalidSyntaxException {
		Configuration config = getConfig(name);
		boolean removed = false;
		if (config != null) {
			config.delete();
			removed = true;
		}
		System.out.println("\"" + name + "\" conversion " + (removed ? "removed" : "was not added manually"));
	}
}