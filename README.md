# Pauta de Votação API

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias,
por votação.

 Objetivos
  - Cadastrar uma nova pauta;
  - Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo
determinado na chamada de abertura ou 1 minuto por default);
  - Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é
identificado por um id único e pode votar apenas uma vez por pauta);
  - Contabilizar os votos e dar o resultado da votação na pauta.
### Linguagem, Frameworks e Bibliotecas

* Java 8
* Spring Boot Web; JPA; Data; Cloud;
* Swagger 2
* MySql
* Lombok
* H2 Database
* AppEngine - Google Cloud Plataform

#### Swagger
Desenvolvimento:
```
http://localhost:8080/swagger-ui.html#/
```

### Versionamento
    - Os endpoints são versionados pelo número major da versão (v1) diretamente na URL de acesso.


