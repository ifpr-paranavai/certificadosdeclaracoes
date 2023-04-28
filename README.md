Como fazer contribuições: https://docs.github.com/en/get-started/quickstart/contributing-to-projects



Baixar tooncat:
	https://ava.ifpr.edu.br/mod/resource/view.php?id=339889
Baixar Banco:
	https://ava.ifpr.edu.br/mod/resource/view.php?id=339890

Config Database:
	criar base b3mc4 e rodar script do banco
	verificar se realmente criou a tabela usuario
	caso não, add "use b3mc4;" no começo do arquivo

Config Project:
	Nos caminhos:
		- /certificadosdeclaracoes/WebContent/WEB-INF/applicationContext.xml
		- /certificadosdeclaracoes/src/main/java/META-INF/persistence.xml
	Adicionar o usuario e senha da database.
		Ex: 
			<property name="javax.persistence.jdbc.user" value="root" />
			<property name="javax.persistence.jdbc.password" value="root" />

	Caso Necessario alterar a porta do banco, no meu caso está em 3307
		<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3307/b3mc4" />

Config Tooncat
	Com o tooncat, "extrai para aqui" tem que ser fora do projeto
	Em servers do eclipse -> 
		new -> 
		selecione apache 9 -> 
		Server runtime selecione add -> 
		browser -> 
			selecione a pasta extraida ex: C:\Users\santyero\Downloads\apache-tomcat-9.0.65\apache-tomcat-9.0.65\apache-tomcat-9.0.65
		next -> 
		add-all -> 
		finish -> 

Botao direito no projeto certificadosdedeclaracoes -> run As -> Run on Server


CASO ERRO:
	EM servers ->
		remova o certificadodeclaracoes
	botao direito no Tomcat -> 
		clean
	e refaça o run server do projeto.


Para Logar
	acessar o site http://localhost:8080/certificadosdeclaracoes/login.jsf
	com usuario frank.willian@ifpr.edu.br recupere a senha
	copie a senha e cole
	Caso logado e retorne para o painel principal, utilize outro navegador ksksksk
