package br.com.geladaonline;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import br.com.geladaonline.model.Estoque;
import br.com.geladaonline.model.rest.Cervejas;

@WebServlet("/cervejas/*")
public class CervejaServlet extends HttpServlet {

	private static JAXBContext context;
	
	static {
		try {
			context = JAXBContext.newInstance(Cervejas.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Estoque estoque = new Estoque();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * Negociação de conteúdo pelo 
		 * Header accept: applicaton/xml ou .../json e .../outros
		 */
		//
		String accept = request.getHeader("Accept");
		
		Cervejas cervejas = new Cervejas();
		cervejas.setCervejas(new ArrayList<>(estoque.listarCervejas()));
		
		try {
			if ("application/xml".equals(accept)){
				geraXML(response, cervejas);
			} else if("application/json".equals(accept)){
				geraJSON(response, cervejas);				
			} else {
				// 415 Formato não suportado
				response.sendError(415);
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}
	}

	private void geraJSON(HttpServletResponse response, Cervejas cervejas) throws JAXBException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		MappedNamespaceConvention con = new MappedNamespaceConvention();
		XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con, response.getWriter());
		
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(cervejas, xmlStreamWriter);
	}

	private void geraXML(HttpServletResponse response, Cervejas cervejas) throws JAXBException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(cervejas, response.getWriter());
	}

}
