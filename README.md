# mobile-supermarket
Mobile Supermarket

O Mobile Supermarket (http://public.mobilesupermarket.wedeploy.io/) é uma aplicação que deve rodar em Android e/ou iOS e permite realizar compras de alimentos online.
O app deve fornecer uma lista de itens que podem ser adquiridos pelo usuário e deve ser possível mostrar os detalhes disponíveis do produto, em uma tela separada. Para tornar mais fácil encontrar o que está buscando, o usuário é capaz de filtrar a lista de produtos pelo seu tipo (Bakery, Dairy, Fruit, Vegetable, Meat). O usuário também deve ser capaz de adicionar e visualizar produtos no carrinho.

Requisitos obrigatórios:
* O app deve funcionar em landscape e portrait
* Exibir a lista de produtos disponíveis
* Deve existir uma tela a parte para mostrar os detalhes do produto (descrição, rating, imagem em maior escala, valor)
* Adicionar , listar e remover produtos do carrinho. Não é necessário implementar o checkout.
* Buscar alimentos por nome

Requisitos desejáveis:
* Armazenamento offline
* Testes unitários
* O que mais achar que seria legal adicionar :)

MOBILE SUPERMARKET API

O mobile supermarket disponibiliza uma API Rest que permite que aplicativos consiga acessar todo o conteúdo disponível, além de permitir a autenticação e criação de novas contas de usuários.

##	Autenticação
A autenticação e criação de novos usuários é feita através da url auth.mobilesupermarket.wedeploy.io utilizando os endpoints a seguir:

* Criar usuário:

Nome | Descrição 
------------ | ------------ 
Endpoint | /users 
Método | POST
Parâmetros | name: String<br>email: String<br>password: String

* Login: 

Nome | Descrição 
------------ | ------------ 
Endpoint | /oauth/token
Método | POST
Parâmetros | username: String <br>password: String<br>grant_type: "password"

* Carregar usuário logado: (é necessário passar o token de autenticação, como descrito na próxima seção) 

Nome | Descrição 
------------ | ------------ 
Endpoint | /user
Método | GET

##	Acesso aos produtos e carrinho
O acesso aos dados do Mobile Supermarket é feito através da url data.mobilesupermarket.wedeploy.io. Para efetuar qualquer operação sobre os dados, é necessário estar logado. Uma vez logado, o token de acesso deve ser enviado no campo "Authorization" do header de cada request, cujo valor é "Bearer [access_token]".

* Listar todos os produtos: GET /products
* Filtrar produtos: GET /products?filter={"field":"value"}
O json {"field":"value"} deve estar codificado para que a request funcione corretamente. Por exemplo, para o filtro "{"type":"fruit"}", a request deve ser: /products?filter=%7B%22type%22%3A%22fruit%22%7D

* Listar produtos do carrinho: GET /cart
* Salvar produtos no carrinho: 

Nome | Descrição 
------------ | ------------
Endpoint | /cart
Método | POST
Parâmetros | productTitle: String<br>productPrice: double<br>productFilename: String<br>productId: String<br>userId: String

* Remover produto do carrinho: DELETE /cart/[id]
	Onde id é o id do item no carrinho.

