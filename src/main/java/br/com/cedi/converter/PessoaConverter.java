package br.com.cedi.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.cedi.dao.PessoaDAO;
import br.com.cedi.model.Pessoa;

//@FacesConverter(forClass = Pessoa.class)
@FacesConverter("teste")
public class PessoaConverter implements Converter {

	@Inject
	private PessoaDAO pessoaDAO;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pessoa retorno = null;
		if (value.equalsIgnoreCase("Selecione")) {
			value = "";
		}
		if (value != null && value.trim().length() > 0) {
			if (StringUtils.isNotEmpty(value)) {
				retorno = this.pessoaDAO.buscarPeloCodigo(new Long(value));
			}
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return ((Pessoa) value).getId().toString();
		}
		return "";
	}

}