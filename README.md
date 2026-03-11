# LojaFluxo — CRUD com JPA + Hibernate

Projeto originalmente desenvolvido durante a faculdade para estudar **Programação Orientada a Objetos**, evoluído posteriormente para incorporar **JPA + Hibernate** e boas práticas de design como **Design Patterns** e separação de camadas.

A ideia central é simular o gerenciamento de pessoas em uma loja — `Cliente` e `Funcionario` — sem framework web, focando puramente nos conceitos de persistência e orientação a objetos.

---

## Tecnologias

- Java 17+
- Maven
- JPA (Jakarta Persistence API 3.1)
- Hibernate ORM 6.4
- H2 Database (em memória)

---

## Estrutura do Projeto

```
src/main/java/com/lojafluxo/
├── domain/
│   ├── Pessoa.java           # Superclasse abstrata mapeada
│   ├── Cliente.java          # Entidade JPA
│   └── Funcionario.java      # Entidade JPA
├── infrastructure/
│   ├── JPAConnectionFactory.java   # Singleton do EntityManagerFactory
│   ├── ClienteRepository.java      # CRUD de Cliente
│   └── FuncionarioRepository.java  # CRUD de Funcionario
└── LojaFluxo.java            # Ponto de entrada

src/main/resources/META-INF/
└── persistence.xml           # Configuração do JPA/Hibernate
```

---

## Configuração — persistence.xml

O `persistence.xml` centraliza toda a configuração do JPA — conexão com banco, provider, comportamento do Hibernate e entidades mapeadas. Substitui o que em JDBC puro seria feito manualmente com `ConnectionFactory`, `DatabaseInitializer` e `schema.sql`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
             https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="lojafluxo">

        <class>com.lojafluxo.domain.Cliente</class>
        <class>com.lojafluxo.domain.Funcionario</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:lojafluxo;DB_CLOSE_DELAY=-1"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>

            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>

    </persistence-unit>

</persistence>
```

**Propriedades importantes:**

- `hbm2ddl.auto = update` — o Hibernate lê as entidades anotadas e cria/atualiza as tabelas automaticamente
- `show_sql = true` — printa o SQL gerado no console, ótimo para aprendizado
- `<class>` — necessário em Java SE puro pois não há escaneamento automático de entidades

---

## Modelagem de Domínio

### Herança — `@MappedSuperclass`

`Pessoa` é uma classe abstrata que define o contrato comum entre `Cliente` e `Funcionario`. Nunca existe uma "Pessoa" pura no sistema — sempre um subtipo.

A anotação `@MappedSuperclass` diz ao Hibernate que `Pessoa` não gera tabela própria, mas seus atributos são herdados e mapeados nas tabelas das subclasses — resultando em duas tabelas: `clientes` e `funcionarios`, cada uma com os campos comuns de `Pessoa` mais os específicos.

```java
@MappedSuperclass
public abstract class Pessoa {
    private String cpf;
    private String nome;
    private String endereco;
    private int idade;

    protected Pessoa() {} // para o Hibernate

    protected Pessoa(String nome, String cpf, String endereco, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.idade = idade;
    }
}
```

**Por que `abstract`?**
Garante que ninguém instancie `Pessoa` diretamente — ela existe apenas como molde para os subtipos.

---

### Anotações JPA nas Entidades

```java
@Entity
@Table(name = "funcionarios")
public class Funcionario extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cargo;
    private String departamento;
    private double salario;
    // ...
}
```

- `@Entity` — marca a classe como entidade gerenciada pelo JPA
- `@Table(name = "funcionarios")` — define o nome da tabela no banco
- `@Id` — define a chave primária
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` — delega a geração do ID ao banco via auto increment
- `@Column(name = "nome_coluna")` — personaliza o nome da coluna quando necessário

---

## Design Patterns Aplicados

### Singleton — `JPAConnectionFactory`

O `EntityManagerFactory` é pesado para criar — lê o `persistence.xml`, valida entidades e prepara o pool de conexões. Por isso deve existir apenas uma instância durante toda a aplicação.

```java
public class JPAConnectionFactory {
    private static EntityManagerFactory instance;

    private JPAConnectionFactory() {}

    public static EntityManagerFactory getInstance() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("lojafluxo");
        }
        return instance;
    }
}
```

O `EntityManager` é leve e criado por operação — igual ao que a `Connection` era no JDBC puro.

---

### Factory Method — Entidades

O construtor das entidades é privado. A criação é feita exclusivamente pelo factory method, garantindo objetos sempre válidos.

O Hibernate exige um construtor sem argumentos para instanciar entidades ao buscar do banco via reflection. Por isso existe o construtor `protected` vazio — com acesso mínimo necessário para o framework sem expor a criação para o resto da aplicação.

```java
public class Funcionario extends Pessoa {

    private Funcionario(String nome, String cpf, ...) {
        super(nome, cpf, ...);
        // inicialização
    }

    protected Funcionario() {} // para o Hibernate — não usar no código da aplicação

    public static Funcionario newFuncionario(String nome, String cpf, ...) {
        return new Funcionario(nome, cpf, ...);
    }
}
```

---

## EntityManager — Operações CRUD

### Ciclo de vida

```
EntityManagerFactory (Singleton — criado uma vez)
    → EntityManager (por operação — criado e fechado)
        → operações dentro de transação
        → commit ou rollback
        → close
```

### Transações

O JPA não tem `autoCommit`. Toda operação que modifica dados precisa de transação explícita com tratamento de erro:

```java
EntityManager entityManager = entityManagerFactory.createEntityManager();
try {
    entityManager.getTransaction().begin();
    // operação
    entityManager.getTransaction().commit();
} catch (RuntimeException e) {
    entityManager.getTransaction().rollback();
    throw e;
} finally {
    entityManager.close();
}
```

- `begin()` — inicia a transação, operações passam a ser rastreadas
- `commit()` — confirma tudo e envia ao banco
- `rollback()` — desfaz tudo desde o begin, banco fica intacto

### Métodos do EntityManager

| Método | Equivalente SQL | Observação |
|---|---|---|
| `persist(obj)` | INSERT | Objeto sem ID |
| `find(Classe, id)` | SELECT WHERE id | Retorna null se não encontrar |
| `merge(obj)` | UPDATE | Objeto com ID — retorna objeto gerenciado |
| `remove(obj)` | DELETE | Objeto deve estar managed |
| `createQuery(jpql)` | SELECT | Usa JPQL, não SQL |

### JPQL vs SQL

O JPA usa JPQL — linguagem de query orientada a objetos. Você referencia **classes e atributos Java**, não tabelas e colunas. O Hibernate traduz para o SQL do banco em uso, garantindo portabilidade.

```java
// SQL nativo
"SELECT * FROM funcionarios WHERE cargo = ?"

// JPQL equivalente
"SELECT f FROM Funcionario f WHERE f.cargo = :cargo"
```

O `SELECT *` não existe em JPQL — você seleciona a entidade pelo alias:

```java
// findAll
entityManager.createQuery("SELECT f FROM Funcionario f", Funcionario.class)
             .getResultList();

// com filtro e parâmetro nomeado
entityManager.createQuery("SELECT f FROM Funcionario f WHERE f.cargo = :cargo", Funcionario.class)
             .setParameter("cargo", "Gerente")
             .getResultList();
```

### Detalhe importante no DELETE

O objeto precisa estar **managed** (gerenciado pelo EntityManager dentro da transação) para o `remove` funcionar. Por isso o `find` deve ocorrer **após** o `begin`:

```java
entityManager.getTransaction().begin();
Funcionario f = entityManager.find(Funcionario.class, id); // managed
entityManager.remove(f);
entityManager.getTransaction().commit();
```

---

## Ciclo de Vida de uma Entidade JPA

```
new Funcionario()      → Transient  (não gerenciado, não persistido)
entityManager.persist  → Managed    (gerenciado, será persistido no commit)
entityManager.close    → Detached   (existe em memória, não gerenciado)
entityManager.remove   → Removed    (será deletado no commit)
```

---

## Comparativo — JDBC Puro vs JPA/Hibernate

| | JDBC Puro | JPA + Hibernate |
|---|---|---|
| Criação de tabelas | `schema.sql` manual | Automático via `hbm2ddl.auto` |
| Configuração de conexão | `ConnectionFactory` manual | `persistence.xml` |
| INSERT | `PreparedStatement` + `setX()` | `entityManager.persist(obj)` |
| SELECT por ID | `ResultSet` + montar objeto na mão | `entityManager.find()` |
| SELECT todos | `while(rs.next())` + lista manual | JPQL + `getResultList()` |
| UPDATE | SQL manual | `entityManager.merge(obj)` |
| DELETE | SQL manual | `entityManager.remove(obj)` |
| Query com filtro | SQL com `?` | JPQL com `:parametro` |
