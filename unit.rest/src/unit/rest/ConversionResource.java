package unit.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import com.fasterxml.jackson.core.JsonProcessingException;

import unit.api.Conversion;
import unit.api.ConversionSearch;
import unit.api.ConversionSearchResult;

@Component(service=ConversionResource.class)
@JaxrsResource
@Path("conversion")
public class ConversionResource {
	@Reference(
			policy = ReferencePolicy.DYNAMIC
			)
	private volatile Collection<Conversion> conversions;
	
	public ConversionSearch getConversionSearch() {
		return new ConversionSearch(conversions.toArray(new Conversion[conversions.size()]));
	}

	/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConversionSearchResult search(@QueryParam("q") String q) throws JsonProcessingException {
		return getConversionSearch().search(q);
	}*/
	
	@GET
	@Path("/{lang}")
	@Produces(MediaType.APPLICATION_JSON)
	public ConversionSearchResult search(@PathParam("lang") String lang, @QueryParam("q") String q) throws JsonProcessingException {
		System.out.println("lang=" + lang + ", q=" + q);
		return getConversionSearch().search(lang, q);
	}
}
