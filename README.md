<html>
   <head></head>
   <body >
<h1 align="center"> <strong> Tech Challenge -  Streaming de Víedos</strong> </h1>


<h2><strong>Descrição do Projeto</strong></h2>
<p>Projeto Tech Challenge - FIAP Arquitetura de Sistema em Java - 4º Fase</p>
<p>Projeto para controle de um Streaming de Videos com Spring Boot, Spring Web Flux para programação reativa com todas as vantagens de um processo não bloqueante, Conceito de Arquitetura Limpa e Testes Unitários e Integrados com no minimo 80% de cobertura do código. Além de aplicarmos as melhores práticas de desenvolvimento e neste casos usamos SonarLint e CheckStyle para nos apoiar.</p>
<p></p>
<p>No paragrafo acima falamos mais dos requisitos técnicos, sobre o negócio devemos ter um CRUD completo para Vídeos e algumas listagens utilizando filtros especificos e um endpoint de estatisticas de favoritos e visualizados.</p>
<h2><strong>Funcionalidades e Demonstração da Aplicação</strong></h2>
<p></p>
<p> 1) Baixar via GIT : git clone https://github.com/ArleiPacanaro/TechChallenge.git</p> 
<p></p>
<p> 2) Executar em máquina local : na linha de comando ir até a pasta que clonou o projeto e no prompt da linha de comando, executar: 
<p><b> 2.1)ir até a pasta: TechChallenge e  executar o comando: docker build -t tech:2.0 . ** o detalhe do ponto muito importante, bem como manter o nome tech:2.0 para uso em nosso compose .yml</b> </p> 
<p> Poderiamos através de uma conta docker hub ja deixar a imagem pronta, porém por não ser requisito e ainda não termos esta conta cadastrada, iremos criar uma imagem localmente.</p>
<p><b> 2.2)  ir até a pasta que esta o projeto e executar o comando: docker compose up -d </b> </p> 
<p>Este commando irá executar o conteiner da imagem que criamos no item 1 da nossa aplicação , mas antes irá executar a criação do banco da nossa aplicação que chame-se <b>dbEnergia</b>  em Postgres, com usuário = user e senha = 123456, isto é importante caso for analisar o resultado dos processamentos no banco de dados.</p>
<p> ** As vezes se torna necessário executar 2 vezes o compose, colocamos a dependência para subir a  aplicação após o banco, mas as vezes acabam indo em conjunto e a aplicação fica pendente.</p>
<p><b>Premissas: </b></p> 
<p></p>
<p>Após os passos acima, poderá acessar o nosso swagger para testes e ver nossa documentação.</p>
<p>URL principal do nosso swagger: http://localhost:8080/swagger-ui-custom.html</p>

<h2><strong>Pessoas Desenvolvedoras do Projeto</strong></h2>
<p> Grupo 39 : RM350113 , RM349894 , RM350459 e 350127</p>
<p></p>

<h2><strong>Conclusão</strong></h2>
 </body>
</html>
