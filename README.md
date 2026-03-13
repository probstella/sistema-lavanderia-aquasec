# Sistema de Gestão de Lavanderia – Aquasec

Sistema desktop desenvolvido em **Java** para gerenciamento de clientes e pedidos de uma lavanderia.
O sistema foi projetado para ser **simples, intuitivo e eficiente**, facilitando o controle dos serviços e clientes no dia a dia.

## Funcionalidades

* Cadastro e gerenciamento de **clientes**
* Registro e acompanhamento de **pedidos de lavanderia**
* Interface gráfica **simples e intuitiva**
* **Salvamento automático dos dados**, evitando perda de informações
* Armazenamento dos dados **localmente no próprio computador**
* **Destaque visual para pedidos atrasados**, facilitando o controle de prazos
* **Remoção automática do histórico de pedidos finalizados após 7 dias**, evitando acúmulo de registros antigos

## Tecnologias Utilizadas

* **Java**
* **Java Swing** para interface gráfica
* **Programação Orientada a Objetos**
* **Git e GitHub** para controle de versão

## Estrutura do Projeto

```
src/
 ├ dao       → acesso e persistência de dados
 ├ model     → entidades do sistema
 ├ service   → regras de negócio
 └ ui        → interface gráfica
```

## Execução do Sistema

Para executar o sistema:

1. Baixe o arquivo **LavanderiaSistema.jar** e a pasta **data**
2. Execute com Java instalado:

```
java -jar LavanderiaSistema.jar
```

## Capturas de Tela

As imagens do sistema podem ser encontradas na pasta **screenshots**.

## Melhorias Futuras

* Implementação de **banco de dados** para armazenamento das informações
* Sistema de **login para funcionários**
* **Relatórios de serviços e faturamento**
* Busca e filtros avançados de pedidos
* Versão **web ou mobile** do sistema
* Integração com **notificações para clientes sobre status do pedido**

## Objetivo do Projeto

Este projeto foi desenvolvido como PEX do curso de **Análise e Desenvolvimento de Sistemas**, com o objetivo de aplicar conceitos de **programação orientada a objetos**, organização em camadas e desenvolvimento de **aplicações desktop em Java**.
