package br.com.geladaonline;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
		try {
			Marshaller marshaller = context.createMarshaller();
			response.setContentType("application/xml;charset=UTF-8");
			PrintWriter out = response.getWriter();
			Cervejas cervejas = new Cervejas();
			cervejas.setCervejas(new ArrayList<>(estoque.listarCervejas()));
			marshaller.marshal(cervejas, out);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}
	}

}
