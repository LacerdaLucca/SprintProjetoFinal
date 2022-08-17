# Projeto Final PB
Projeto final desenvolvido com base em todo o aprendizado em Java do Programa de bolsas da Compass.oul, para avaliação do desenvolvimento e aprimoramento das habilidade no BackEnd java. Projeto desenvolvido em AEM na instancia Author.

--------------------------
Própositos
--------------------------
	Projeto foi desenvolvido a fim de mostrar todo o desenvolvimento e aprendizado dos Bolsistas BackEnd em java. 
	O Projeto tem como objetivo o desenvolvimento de um catalogo de produtos no padrão REST utilizando a tecnologia de servlets do Sling.
	
--------------------------
Banco De Dados
--------------------------


	Utilizar o banco de dados mySQL com a database "lojafinal".
	usuario = root;
	senha = suaSenhaSql;



    CREATE TABLE PRODUTO(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NOME VARCHAR(200),
    categoria VARCHAR(100),
    PRECO DECIMAL(10,2)
    );
    
    CREATE TABLE CLIENTE(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NOME VARCHAR(200)
    );
    
    CREATE TABLE NOTAFISCAL(
    NUMERO INT PRIMARY KEY AUTO_INCREMENT,
    IDPRODUTO INT,
    IDCLIENTE INT,
    VALOR DECIMAL(15,2),
    foreign key(IDPRODUTO) REFERENCES produtos(ID),
    foreign key(IDCLIENTE) REFERENCES produtos(ID)
    );


Após a configuração das tabelas pode-se iniciar os testes nos endpoints criados

--------------------------
EndPoints
--------------------------

	#Produtos - Retorno json
	www.localhost:4502/bin/produto - POST, GET, DELETE e PUT
	
	#Clientes - Retorno json
	www.localhost:4502/bin/cliente - POST, GET, DELETE e PUT
	
	#Notas Fiscais - Retorno json
	www.localhost:4502/bin/notafiscal - POST, GET, DELETE e PUT
	
	#Relatorio de Produtos - Retorno HTML
	www.localhost:4502/bin/relatorio - GET


# Como Utilizar



## Produto


	--------------------------------------------
	localhost:4502/bin/produto
	--------------------------------------------

Payload

	[
	    {
        "id": 1,
	    "name": "Teste",
	    "categoria": "categoria teste";
	    "price":150.0
	    }
	]

POST
- Adiciona um novo Produto.

    - `Parâmetros`: Nenhum.

    - `Payload`: Todos os campos em formato Json no corpo da requisição com exceção id.

GET
- Recupera a lista de Produtos.
    - `Parâmetros`:
        - Opcionais:
            - id='id' - Recupera um único produto. (único)
    - `Payload`: Nenhum.

DELETE
- Deleta um Produto.

    - `Parâmetros`:
        - Obrigatório
            - id='id'.
              -O produto não será deletado se já estiver em uma nota fiscal.

    - `Payload`: 
      - Campos id e nome ou um array de dados.

PUT
- Atualiza um Produto escolhido.
    - `Parâmetros`: Nenhum.

    - `Payload`: Todos os campos.

##

## Cliente


	--------------------------------------------
	localhost:4502/bin/cliente
	--------------------------------------------

Payload

	{
	    "name":"Lucca Lacerda"
	}

POST
- Adiciona um novo Cliente.

    - `Parâmetros`: Nenhum.

    - `Payload`: Todos os campos em formato Json no corpo da requisição com exceção do id.

GET
- Recupera a lista de Clientes.
    - `Parâmetros`:
        - Opcional:
            - id='id' - Recupera um único Cliente.

    - `Payload`: Nenhum.

DELETE
- Deleta um Cliente.
    - `Parâmetros`:
        - Obrigatório
            - productId='id'.
              -O produto não será deletado se já estiver em uma nota fiscal.

    - `Payload`:
        - Campos id e nome ou um array de dados.

PUT
- Atualiza um Cliente escolhido.
    - `Parâmetros`: Nenhum.

    - `Payload`: Todos os campos.

##

##

## Relatorio


	------------------------------------------------
	localhost:4502/bin/relatorio
	-------------------------------------------------



GET
- Recupera o relatório de todos os produtos comprados por um determinado Cliente.
    - `Parâmetros`:
        - Obrigatório:
            - cliente='id'.

    - `Payload`: Nenhum.


#
Agradecimento aos meus iustrutores
Thauany Correa Martins,
Renan Gomes Acosta e
Andre Monteiro Fernandes Lima(Responsável por esse modelo de README.md)
