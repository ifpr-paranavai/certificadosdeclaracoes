package br.com.cedi.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;


@Named("converterPessoa")
public class ConverterPessoa implements Converter{

	@Inject
	PessoaDAO pessoaDAO;
	
		@Override
		public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
			if(value.equalsIgnoreCase("Selecione")){
				value="";
			}
			if (value != null && value.trim().length() > 0) {
				try {
					System.out.println("Id Objeto: "+value);
					if(pessoaDAO==null){
						System.out.println("PESSOADAO N ULOOOO");
					}
					Object pessoa = pessoaDAO.buscarPeloCodigo(Long.parseLong(value));
					Pessoa pp=(Pessoa) pessoa;
					System.out.println("Pessoa: "+pp.getId());
					return pp;
				} catch (NumberFormatException e) {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no converter Pessoa",
							"N�o � uma Pessoa v�lida"));
				}
			} else {
				return null;
			}
		}

		@Override
		public String getAsString(FacesContext fc, UIComponent uic, Object object) {
			if (object != null) {
				return String.valueOf(((Pessoa) object).getId());
			} else {
				return null;
			}
		}

}
