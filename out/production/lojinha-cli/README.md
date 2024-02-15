# src-cli

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

## TO DO
- Modelagem com UML e diagrama de casos de uso

V1.0.0
