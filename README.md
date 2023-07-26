### Trabalho 2: Programação Orientada a Objetos
Feito em dupla com colega no 3º semestre. <br/>
Um CRUD com seis classes obrigatórias: Pesquisador, Projeto, Artigo, Arquivo, CarregadorDeDados e InterfaceDeUsuario.

Foi um bom aprendizado: implementei o hashCode() no objeto Pesquisador, considerando acentos e diferenças entre letras maiúsculas e minúsculas no nome, criei o método equals(), depois com a leitura e criação de arquivos descobri como utilizar BufferedReader para ler linhas (encapsulando FileReader) para tornar a leitura do arquivo mais simples em comparação ao RandomAccessFile,  e usei expressões regulares (RegEx) para ler o Lattes.txt que os outros colegas estavam com dificuldades de fazer a leitura — já que o professor não deu nenhuma dica e deixou como "desafio" para os alunos. Aprendi muitas coisas que eu não conhecia antes.

#### Objetivo:

O Currículo Lattes possui um conjunto vasto de informações. Para esse trabalho, é necessário realizar o cadastro dos Pesquisadores, Projetos e Artigos. Além dos cadastros devem ser disponibilizadas funcionalidades para realizar as seguintes pesquisas:

    • Listar os pesquisadores de uma mesma Universidade;
    • Listar os autores de um determinado artigo;
    • Listar todos os projetos de um determinado pesquisador;
    • Listar os pesquisadores de projetos já finalizados.
#### Implementação das seguintes classes:
    • Classe Pesquisador: Cadastrar o nome, a área e a universidade do pesquisador. Aplicar o conceito de herança para separar os pequisadores que são professores dos alunos.
    • Classe Projeto: Cadastrar o título do projeto, descrição, data de início, data de final. Vincular os pesquisadores envolvidos (coleção de objetos).
    • Classe Artigo: Cadastrar o título do artigo, ano de publicação, título da revista. Vincular os pesquisadores envolvidos (coleção de objetos).
    • Classe Arquivo: Fazer a leitura e gravação do arquivo de dados e gerenciar as exceções de manipulação de arquivos (duas no mínimo).
    • Classe CarregadorDeDados: Essa classe deve fazer a carga dos valores definidos no arquivo de dados para facilitar a testagem da aplicação.
    • Classe InterfaceDeUsuario: Essa classe deve fazer o gerenciamento da aplicação. O resultado das consultas solicitadas devem ser visualizados em tela, bem como podem ser solicitados a serem gravados em arquivos textos.

    
1. Todas as classes acima devem ser desenvolvidas. Outras classes podem ser acrescentadas ao projeto.
2. Use corretamente os conceitos de orientação a objetos vistos em aula (classe, objeto, atributo, método, construtor, encapsulamento, herança e polimorfismo) e os métodos toString, equals e o gerenciamento das coleções de objetos. Uso obrigatório no Trabalho II de herança, polimorfismo, encapsulamento, coleção de objetos, arquivos e exceções.
3. Este trabalho pode ser feito em duplas.
4. Entregar o código fonte e um texto explicativo do funcionamento do mesmo.
5. Apresentar o trabalho para o professor na data estipulada no AVA 
