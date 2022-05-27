# Informações sobre o desenvolvimento

Para o desenvolvimento foi utilizada a IDE Eclipse JEE 2019-09.
Framework Java utilizado: Spring Boot.

O projeto foi gerado com apoio nesse artigo [Getting Started with Java on Heroku](https://devcenter.heroku.com/articles/getting-started-with-java), realizando o clone do github conforme citado nele, pois a aplicação foi planejada para ter o seu deploy realizado na plataforma cloud Heroku.

<b>Link do Github com o Frontend da aplicação <u>web</u>:</b> https://github.com/gabsperandeo/ProjetoLivres-frontend-entregas/

## Documentação Técnica: Configurações dos ambientes para o funcionamento da aplicação web
<h2>1 Introdução</h2>
	O presente documento aborda todas as configurações de ambientes necessárias para o funcionamento da aplicação entregue em sua primeira versão no primeiro semestre de 2022. O objetivo é auxiliar as próximas turmas envolvidas no funcionamento dos ambientes para futuras manutenções na aplicação web de entregas do projeto Livres Baixada Santista. Sendo assim, entende-se que é necessário manter o versionamento deste documento quando este escopo for afetado

<h2>Versionamento</h2>
<table>
	<thead>
	<tr>
		<th>Nome</th>
		<th>Comentário</th>
		<th>Data</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>&nbsp;Gabrielle Sperandeo</td>
		<td>Criação do Documento.</td>
		<td>23/05/2022</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tbody>
</table>

<h2>3 Aplicativos no Heroku</h2>
	Pré-requisitos:
<li>Uma conta (gratuita) no Heroku;</li>
<li>Java instalado;</li>
<li>Maven instalado;</li>
<li>Postgres instalado localmente;</li>
<li>Git CLI;</li>
<li>Heroku CLI.</li><br>

<h3>3.1 Backend</h3>
<h4>3.1.1 Criar o Aplicativo</h4>
Crie o aplicativo para o Backend no Heroku.
<h4>3.1.2 Ativar Add-ons</h4>
	Clique em “Configure Add-ons” na dashboard do seu aplicativo.
<h5>3.1.2.1 Postgres</h5>
Busque por “Heroku Postgres”, selecione o Plan name como “Hobby Dev - Free” e clique em Submit Order Form.
Heroku Postgres é o Banco de Dados da aplicação.
<h5>3.1.2.2 Papertrail</h5>
*Neste caso, para adicionar este add-on é necessário cadastrar um cartão de crédito, apenas para confirmação da conta. Desde que o plano selecionado seja o “Choklad - Free”, não haverá cobranças.	
Busque por “Papertrail”, selecione o Plan name como “Choklad - Free” e clique em Submit Order Form.
	Papertrail é responsável por prover logs em tempo real da aplicação.

<h4>3.1.3 Realizar Clone do Github</h4>
	Abra o diretório conforme local desejado e digite:
git clone https://github.com/gabsperandeo/ProjetoLivres-backend-entregas

<h4>3.1.4 Realizar Deploy no Heroku</h4>
<li>Abra o Prompt de Comando (CMD)</li>
<li>Digite heroku login e faça o seu login (caso não esteja logado)</li>
<li>Digite cd ProjetoLivres-backend-entregas</li>
<li>Digite heroku git:remote -a seu-heroku-app</li>
<li>Digite git add .</li>
<li>Digite git commit -am "init"</li>
<li>Digite git push heroku main </li>


<h2>3.2 Frontend</h2>
<h3>3.2.1 Criar o Aplicativo</h3>
Crie o aplicativo para o Backend no Heroku.
<h3>3.2.2 Ativar Add-ons</h3>
<h4>3.2.2.1 Papertrail</h4>
*Neste caso, para adicionar este add-on é necessário cadastrar um cartão de crédito, apenas para confirmação da conta. Desde que o plano selecionado seja o “Choklad - Free”, não haverá cobranças.	
Busque por “Papertrail”, selecione o Plan name como “Choklad - Free” e clique em Submit Order Form.
	Papertrail é responsável por prover logs em tempo real da aplicação.

<h3>3.2.3 Realizar clone do Github</h3>
	Abra o diretório conforme local desejado e digite:
git clone https://github.com/gabsperandeo/ProjetoLivres-frontend-entregas
<h3>3.2.4 Realizar Deploy no Heroku</h3>
<li>Abra o Prompt de Comando (CMD)</li>
<li>Digite heroku login e faça o seu login</li>
<li>Digite cd ProjetoLivres-frontend-entregas</li>
<li>Digite heroku git:remote -a seu-heroku-app</li>
<li>Digite git add .</li>
<li>Digite git commit -am "init"</li>
<li>Digite git push heroku main </li>

<h3>3.2.5 Atualizar Referências Frontend</h3>
	Abra o seu projeto (frontend) em seu editor favorito e procure por “herokuapp.”. Encontraremos essa referência em 6 arquivos diferentes:
<li>pages-entregas-scripts.js</li>
<li>cadastro.html</li>
<li>entregas-invalidas.html</li>
<li>entregas.html</li>
<li>login.html</li>
<li>minhas-entregas.html</li><br>

Altere a url (somente a url, não a uri toda. Exemplo: mantenha tudo que tiver após a “/” final - /login, /api/cadastroUsuario, etc) em todos os arquivos encontrados pela url do seu aplicativo heroku BACKEND, salve e dê commit + push no seu aplicativo heroku frontend.
<h2>4 Google</h2>
<h3>4.1 Google Cloud</h3>
	Acesse o Console da Google Cloud Platform.
<h4>4.1.1 Cadastrar um Cartão de Crédito</h4>
	É necessário cadastrar um cartão de crédito para que possa utilizar os serviços gratuitos da Google, com a finalidade de confirmar a sua conta e realizar cobranças caso você ultrapasse o valor mensal deles de consumo das APIs (200 dólares). Este valor é renovado mensalmente.
	Link de apoio:
https://cloud.google.com/free/docs/gcp-free-tier?hl=pt-br#how-to-upgrade

<h4>4.1.2 Ativar APIs</h4>
	Após realizar o cadastro do cartão de crédito.
<h5>4.1.2.1 Directions API</h5>
	Busque por “Directions API” e clique em “Enable”.
<h5>4.1.2.2 Maps JS API</h5>
	Busque por “Maps JavaScript API” e clique em “Enable”.

<h4>4.1.3 Chaves das APIs</h4>
	Em “APIs & Services” clique em “Credentials”.
<h5>4.1.3.1 Criação das Chaves</h5>
Clique em “+Create Credentials”, depois clique em “API key” e nomeie-a como “backend-Directions”. Repita o processo nomeando a segunda como “frontend-MapsJS”.

<h5>4.1.3.2 Restrição das APIs</h5>
	Clique na api com nome “frontend-MapsJS” e clique em “edit”.
	Restrinja-a pela opção: HTTP referrers (web sites), adicionando três itens:
https://SEU_FRONTEND_APP.herokuapp.com/
https://SEU_FRONTEND_APP.herokuapp.com/minhas-entregas
https://SEU_BACKEND_APP.herokuapp.com/

Salve.
<h3>4.2 Gmail</h3>
<h4>4.2.1 Criação da Senha de Aplicação</h4>
	Link de apoio para a criação da senha de aplicação:
https://support.google.com/accounts/answer/185833?hl=pt-BR

	É necessário criá-la para que ocorra o funcionamento dos envios de e-mail da aplicação.
<h2>5 Configurações Finais</h2>
<h3>5.1 Configurações das Variáveis de Ambiente - BACKEND</h3>
	Para configurar as variáveis de ambiente, basta acessar a pasta do seu projeto backend através do prompt de comando e começar a digitar os comandos com as configurações (copiando e colando os itens abaixo e mudando o que está em NEGRITO):


heroku config:set token=oFX1r63Az8RRyVbFBS69RKK96oIha0oj<br>
heroku config:set url_api_entregas=https://livresbs.com.br/API/entregas/<br>
heroku config:set google_api_key=<b>SUA_API_KEY_GOOGLE_DIRECTIONS</b><br>
heroku config:set link=<b>LINK_DO_SEU_HEROKUAPP_BACKEND</b> <i>(ex.: https://seuapp.herokuapp.com/)</i><br>
heroku config:set email_destinatario_confirm=<b>EMAILS_SEPARADOS_POR_VIRGULA_PARA_NOTIFICACAO_CADASTRO</b><br>
heroku config:set email_gmail=<b>SEU_ENDERECO_GMAIL</b><br>
heroku config:set app_senha_gmail=<b>SUA_SENHA_APLICACAO_GMAIL</b><br>
heroku config:set link_site=<b>LINK_DO_SEU_HEROKUAPP_FRONTEND</b> <i>(ex.: https://seuapp.herokuapp.com/)</i><br>

<h3> 5.2 Alteração API Key - FRONTEND WEB </h3>
	Abra o seu projeto (frontend) no editor e no arquivo “minhas-entregas.html” busque por: https://maps.googleapis.com/maps/api/js?key=
	Substitua o valor que está entre o = e o primeiro símbolo & pelo valor da sua chave do google maps “frontend-MapsJS” e dê um commit + push no seu app heroku (frontend).

