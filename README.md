<html>
   <head></head>
   <body >
<h1 align="center"> <strong> Tech Challenge -  Streaming de Vídeos - FIAP Arquitetura de Sistemas em Java - 4º Fase</strong> </h1>
<p></p>
<p></p>

<h2  align="center"><strong>Descrição do Projeto </strong></h2>
<p></p>

<p>Projeto para controle de um Streaming de Videos com Spring Boot, Spring Web Flux para programação reativa com todas as vantagens de um processo não bloqueante, Conceito de Arquitetura Limpa. TDD com Testes Unitários e Integrados com no minimo 80% de cobertura do código. Além de aplicarmos as melhores práticas de desenvolvimento e neste caso usamos SonarLint e CheckStyle para nos apoiar.</p>
<p></p>
<p>No parágrafo acima falamos mais dos requisitos técnicos, sobre o negócio devemos ter um CRUD completo para Vídeos e algumas listagens utilizando filtros especificos e um endpoint de estatisticas de favoritos e visualizados.</p>
<h2 align="center"><strong>Configuração para uso.</strong></h2>
<p></p>
<p> 1) Baixar via GIT : git clone https://github.com/ArleiPacanaro/TechChallenge.git</p> 
<p></p>
<p> 2) Ir pela linha de comando até o diretório da Aplicação. </p> 
<p></p>
<p> 3) executar mvn spring-boot:run </p> 
<p></p>
<p> 4) Caso queira executar os testes integrados, como optamos em não usar um banco em memória, na mesma pasta do item 3, executar o comando: docker compose up -d </p> 

<p><b>Premissas de softwares na máquina client: </b></p> 
<p></p>
<p>1) Java 17</p>
<p>2) Maven</p>
<p>3) Docker Client</p>

<p>Após os passos acima, poderá acessar o nosso swagger para testes e ver nossa documentação.</p>
<p>URL principal do nosso swagger: http://localhost:8080/swagger-ui-custom.html</p>
<p>Ou poderá acessar http://localhost:8080/videos</p>

<h2 align="center"><strong>Principais pontos Técnicos:</strong></h2>
<p></p>
<p><b>Spring Boot:</b> Optamos pelo Spring Boot com Java para facilitar e agilizar nosso desenvolvimento com servidor ja embarcado da aplicação e estruturas\bibliotecas já prontas em especial para o desenvolvimento WEB com injeção e inversão de dependências.</p>
<p><b>Spring WebFlux</b>: Foi um requisito técnico definido no escopo do projeto, porém é fantástico desenvolver aplicação fora do padrão convencional onde temos um fluxo bloqueante que espera as ações acontecerem para liberar aquele recurso da thread, ao passo que no fluxo não bloqueante após a requisição a thread já é liberada e  o Web Flux faz todo o controle para a resposta, quebrando paradigma da programação convencional, existe um manifesto para ser assinado, sugerimos que todos pesquisem mais sobre o tema assinem.</p>
<p><b>MongoDB</b>: É um dos bancos que suportam a programação reativa, como Cassandra e Redis. Porém além disso já usamos em projetos anteriores e se trata de um banco NOSQL , ou seja, que gerencia Documentos e não tabelas relacionais como as utilizadas nos conceitos mais difundidos, em nosso projeto anterior, expusemos diversas vantagens e desvantagens como o conceito de ACID para banco de dados, mas  as vantagens em nosso modo de ver possuem um peso maior na nossa decisão, entre elas destacamos a escalabilidade horizontal com clusters que nos deixou verdadeiramente encantados. Na nossa aplicação usamos o Atlas MongoDB que é uma Cloud e podemos optar por diversas formas de utilização dos recursos computacionais como ServerLess, backups e até a escalabilidade de forma automática, nos testes integrados que implementamos poderiamos ter usado um banco em memória, mas optamos pelo MongoDB para uma garantia mais efetiva dos testes e neste caso usamos uma imagem docker, sem a necessidade de instalação client.</p>
<p><b>JUnit</b>: É um framework open-source para construção de testes automatizados em Java, essencial para o desenvolvimento utilizando os conceitos de TDD, em conjuto com o Mockito que cria Fakes para nossos testes unitários, utilizamos em todas nossas classes de testes e com isto atingimos mais que o minimo de 80% de cobertura de testes em nosso código.</p>
<p><b>WebTestClient</b>: é uma aplicação cliente que opera em um navegador da web. Ele permite que os usuários acessem serviços e recursos baseados na web por meio de uma interface de navegador. No nosso caso iriamos usar o MockMvc porém para aplicações reativas ele não funciona e optamos por esta, pois além da facilidade de uso , ela não necessita que nossa aplicação para os testes unitários esteja em execução e utilizamos em conjunto com o Mockito e AssertJ para os testes unitários.</p>
<p><b>SonarLint</b>: é um software detector, em tempo real, de códigos fedidos – que geram dificuldades de manutenção , bugs  e vulnerabilidades. Foi muito importante a sua utilização para termos uma escrita correta e segura, aplicamos em todas as classes do projeto com exceção as de testes. </p>
<p><b>CheckStyle</b>: é um analisador estático de código para checar se o código fonte está de acordo com as regras de codificação, este software nos ajudou a atendermos os requisitos da utilização de boas práticas em nosso código.</p>
<p><b>Rest Assured</b>: que nos permite testar serviços RESTful em Java de um jeito muito mais prático, basicamente, ele nos provê uma maneira de criar chamadas HTTP, como se fôssemos um cliente acessando a API. Suporta os métodos POST, GET, PUT, DELETE, OPTIONS, PATCH e HEAD e pode ser usado para validar e verificar a resposta dessas solicitações.</p>



<h2 align="center"><strong>Pessoas Desenvolvedoras do Projeto</strong></h2>
<p> Grupo 39 : RM350113 , RM349894 , RM350459 e 350127</p>
<p></p>

<h2 align="center"><strong>Conclusão</strong></h2>
 </body>
</html>
