package br.com.geladaonline;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import br.com.geladaonline.model.Cerveja;
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
		// Negociação de conteúdo pelo header accept: applicaton/xml ou .../json e .../outros
		String accept = request.getHeader("Accept");
		
		try {
			if ("application/xml".equals(accept)){
				escreveXML(request, response);
			} else if("application/json".equals(accept)){
				escreveJSON(request, response);				
			} else {
				// 415 Formato não suportado
				response.sendError(415);
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();

		String identificador = null;
		try {
			identificador = obtemIdentificador(uri);
		} catch (RecursoSemIdentificadorException e) {
			// 400 Erro no Cliente
			response.sendError(400, e.getMessage());
		}
		
		if (identificador != null && estoque.recuperarCervejaPeloNome(identificador) != null) {
			response.sendError(409, "Já existe uma cerveja com esse nome");
			return ;
		}
		
		Cerveja cerveja = descreveXML(request, response);
		cerveja.setNome(identificador);
		estoque.adicionarCervejas(cerveja);
		
		// 201 Recurso criado (responder com Location e recurso criado)
		response.setHeader("Location", uri);
		response.setStatus(201);
		escreveXML(request, response);
	}
	
	private Object localizaObjetoASerEnviado(HttpServletRequest request){
		Object objeto = null;
		
		try {
			String identificador = obtemIdentificador(request.getRequestURI());
			objeto = estoque.recuperarCervejaPeloNome(identificador);
		} catch(RecursoSemIdentificadorException e) {
			Cervejas cervejas = new Cervejas();
			cervejas.setCervejas(new ArrayList<>(estoque.listarCervejas()));
			objeto = cervejas;
		}
		
		return objeto ;
	}

	private String obtemIdentificador(String uri) throws RecursoSemIdentificadorException {
		String[] partesDaUri = uri.split("/");
		
		boolean isContexto = false;
		for (String parte : partesDaUri) {
			if (parte.equals("cervejas")) {
				isContexto = true;
				continue;
			}
			
			if (isContexto) {
				try {
					return URLDecoder.decode(parte, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					return URLDecoder.decode(parte);
				}
			}
		}
			
		throw new RecursoSemIdentificadorException("Recurso sem identificador");
	}

	private void escreveJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object objetoAEscrever = localizaObjetoASerEnviado(request);
		
		if (objetoAEscrever == null) {
			// 404 Recurso não encontrado
			response.sendError(404);
			return ;
		}

		try {
			response.setContentType("application/json;charset=UTF-8");
			MappedNamespaceConvention con = new MappedNamespaceConvention();
			XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con, response.getWriter());
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(objetoAEscrever, xmlStreamWriter);
		} catch(JAXBException e){
			// 500 Erro no servidor
			response.sendError(500);
		}
	}

	private void escreveXML(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object objetoASerEnviado = localizaObjetoASerEnviado(request);
		
		if (objetoASerEnviado == null) {
			// 404 Recurso não encontrado
			response.sendError(404);
			return ;
		}
		
		try {
			response.setContentType("application/xml;charset=UTF-8");
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(objetoASerEnviado, response.getWriter());
		} catch(JAXBException e) {
			// 500 Erro no servidor
			response.sendError(500);
		}
	}
	
	private Cerveja descreveXML(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cerveja cerveja = null;
		
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			cerveja = (Cerveja) unmarshaller.unmarshal(request.getInputStream());
			return cerveja;
		} catch (JAXBException e) {
			// 500 Erro no servidor
			response.sendError(500);
		}
		
		return cerveja;
	}

}
