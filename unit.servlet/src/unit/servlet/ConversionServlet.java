package unit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;

import unit.api.Conversion;
import unit.api.ConversionSearch;
import unit.api.ConversionSearchResult;

/**
 *@startuml
 *ConversionServlet -right-> "*" Conversions: "conversions"
 *Conversion <|.down. meterToKmConversion
 *@enduml
 */

/**
 * @startuml
 * circle Conversion
 * component ConversionServlet
 * ConversionServlet -right-( "*" Conversions: "conversions"
 * component meterToKmConversion
 * Conversion -- meterToKmConversion
 *@enduml
 */

@Component
@HttpWhiteboardServletPattern("/conversion/*")
public class ConversionServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	private ConversionSearch conversionSearch = new ConversionSearch();

	@Reference(
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC,
			bind = "addConversion",
			unbind = "removeConversion"
	)
	public void addConversion(Conversion conversion) {
		conversionSearch.addConversion(conversion);
	}
	public void removeConversion(Conversion conversion) {
		conversionSearch.removeConversion(conversion);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> segments = new ArrayList<>();
		String path = request.getPathInfo();
		if (path != null) {
			segments.addAll(Arrays.asList(path.split("\\/")));
		}
		if (segments.size() > 0 && segments.get(0).length() == 0) {
			segments.remove(0);
		}
		if (segments.size() > 1) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Request must contain max 1 path segment");
			return;
		}
		String q = request.getParameter("q");
		ConversionSearchResult result = conversionSearch.search(segments.get(0), q);
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		writer.print(result.getMessage());
	}
}