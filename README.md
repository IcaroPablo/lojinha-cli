# lojinha-cli

Um mini projeto para praticar conceitos básicos de programação orientada a objetos

## Requisitos
- Gerenciamento de usuários
    - Deve haver um sistema de autenticação para os usuários
    - Cada cliente deve ser capaz de
        - Criar um novo cadastro
            - O sistema deve validar dados de "nome", "telefone" e "cpf"/"cnpj"
            - Não devem existir dois usuários com o mesmo CPF
        - Visualizar os dados do próprio cadastro
        - Atualizar seus dados
        - Remover o seu cadastro
    - O administrador do sistema deve ser capaz de realizar CRUD em todos os cadastros de clientes
- Gerenciamento de estoque
    - Apenas o administrador do sistema deve ser capaz de gerenciar o estoque por meio de operações de CRUD
- Clientes podem adicionar e remover itens no carrinho de compras
    - Cada usuário deve ter apenas um carrinho
    - Guardar os estados dos carrinhos não finalizados para cada usuário
- Ao fim da compra
    - Exibir uma nota fiscal (e gerar um arquivo) com os itens escolhidos, as quantidades de cada item e o valor a ser pago
    - Itens comprados devem ser removidos do estoque

## V1.0.0

A aplicação deve ter uma interface em linha de comando apenas, não devem ser usados frameworks ou bibliotecas para a implementação de quaisquer funcionalidades

## Material de referência

- [Orientação a objetos com Java](https://www.alura.com.br/apostila-java-orientacao-objetos)
- [SOLID](https://www.alura.com.br/artigos/solid)
- [MVC](https://www.alura.com.br/apostila-java-web/mvc-model-view-controller)

## Bônus
- Modelagem com UML e diagrama de casos de uso
