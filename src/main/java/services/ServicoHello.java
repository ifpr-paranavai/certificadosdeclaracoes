package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;



import br.com.cedi.dao.CertificadoDAO;
import br.com.cedi.dao.GenericDAO;
import br.com.cedi.model.Certificado;
import br.com.cedi.model.HistoricoBusca;
import br.com.cedi.service.HistoricoBuscaService;
import br.com.cedi.util.utilitarios.ExibirMensagem;

@Path("/service")
public class ServicoHello {

	@Inject
	private CertificadoDAO certificadoDAO; // faz as buscas

	@Inject
	private HistoricoBuscaService historicoBuscaService;

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/certificados")
	public Response processos(@QueryParam("cpf") String cpf) {
		List<Certificado> listaCertificadosConsultaAluno = new ArrayList<>();
		


		HistoricoBusca b = new HistoricoBusca();
		b.setCpf(cpf);
		b.setTipo("web-service");
		b.setDataTarefa(new Date());
		historicoBuscaService.salvar(b);

		cpf = cpf.replace("'", "").replace("=", "");

		String cpfLimpo = cpf.replace(".", "").replace("-", "");

		if (cpf.trim().length() > 0) {
			listaCertificadosConsultaAluno = certificadoDAO
					.listarCondicao("(pessoa.cpf='" + cpf + "' or pessoa.cpf='" + cpfLimpo + "') order by id desc");

			if (listaCertificadosConsultaAluno.isEmpty()) {
				System.out.println("Nada encontrado");
			}
		} else {
			System.out.println("informar o cpf");
		}

		GenericEntity<List<Certificado>> lista = new GenericEntity<List<Certificado>>(listaCertificadosConsultaAluno) {

		};
		return Response.status(200).entity(lista).build();
	}

	// http://localhost:8080/hellows/rest/service/somarInteiros?valor=1&valor2=3
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/enviar-email")
	public String helloWebService() {
		
		return "" +true;

	}

	// http://localhost:8080/hellows/rest/service/hello2/10/10
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello2/{valor}/{valor2}")
	public String helloWebService2(@PathParam("valor") Integer valor, @PathParam("valor2") Integer valor2) {
		return "Olá Mundo WebService " + (valor + valor2);
	}

	// http://localhost:8080/hellows/rest/service/hello3/10/
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello3/{valor}")
	public String helloWebService3(@PathParam("valor") Integer valor) {
		return "Olá Mundo WebService " + valor;
	}

}
