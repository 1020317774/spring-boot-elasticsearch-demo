### 一、为什么使用`ES`

其实数据检索并非 `Mysql` 不能做，而是不适合做全文的检索。全文检索往往需要匹配大量的文本，如果还使用`Mysql` 的 `like` 模糊查询将导致性能直线下滑，所谓“术业有专攻”，`Mysql` 更擅长的是基于主键索引的查询，要想保证大文本的匹配查询的性能就要用到像 `ES` 这样的全文检索引擎。

### 二、`ES`与`Mysql`实时同步
可以通过阿里巴巴开源的`canal`中间件实现。

### 三、集成 `Elasticsearch`

- 引入依赖，`springboot` 有两种方式引入

  - 通过 `spring` 官方提供的集成方案
  - 通过 `elasticsearch` 原生依赖继承

  > `Spring Data`是`spring`给各种数据访问提供统一的编程接口，从而简化开发人员的代码，提高开发效率。
  > 所以为了方便，我们通过这种方式集成。
  
    ```xml
    <!--指定elasticsearch版本，防止冲突-->
  <elasticsearch.version>7.10.0</elasticsearch.version>
    
    <!--spring-data-elasticsearch-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    ```
  
- `ES` 配置
  
  - `Spring Boot 2.x-3.x`
  
    ```xml
    # 最新版已启用，使用config
    #spring.data.elasticsearch.cluster-name=my-application
    #spring.data.elasticsearch.cluster-nodes=http://127.0.0.1:9300
    ```
  
  - `Spring Boot 3.x`
  
    ```java
    @Configuration
    public class RestClientConfig extends AbstractElasticsearchConfiguration {
    
        @Override
        public RestHighLevelClient elasticsearchClient() {
            final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                    // 使用RESTFul接口：9200
                    // 弃用TransportClient接口：9300
                    .connectedTo("localhost:9200", "127.0.0.1:9200")
                    .build();
    
            return RestClients.create(clientConfiguration).rest();
        }
    }
    ```
  
  说明：
  
  > 简单操作(CRUD)，通过继承`ElasticsearchRepository<T, ID>`接口实现
  >
  > 复杂操作，通过`RestHighLevelClient`来实现



### 四、项目集成使用 

> https://docs.spring.io/spring-data/elasticsearch/docs/4.1.2/reference/html/#elasticsearch.operations.searchresulttypes

- 编写实体类

  ​	  编写实体类主要会用到如下三个注解

  - 类上注解：`@Document ` (相当于Hibernate实体的@Entity/@Table) (必写)

    | 属性        | 类型    | 说明                       | 默认值 |
    | ----------- | ------- | -------------------------- | ------ |
    | indexName   | String  | 索引库的名称，同数据库表名 | 无     |
    | ~~type~~    | String  | 7.x已移除                  | _doc   |
    | shards      | String  | 分片                       | 1      |
    | replicas    | String  | 副本                       | 1      |
    | createIndex | Boolean | 是否创建索引               | true   |

  - 主键注解：`@Id ` (相当于Hibernate实体的主键@Id注解) 

    > 只是一个标识，并没有属性，主要保持ES的 `_id` 与 实体类 ID一致
  
  - 属性注解 `@Field`  (相当于Hibernate实体的@Column注解)
  
    > @Field默认是可以不加的，默认所有属性都会添加到ES中。
    
    | 属性     | 类型    | 说明                                               | 默认值         |
    | -------- | ------- | -------------------------------------------------- | -------------- |
    | type     | String  | 属性的类型，如索引，可配置：type = FieldType.Text  | FieldType.Auto |
    | analyzer | String  | 分词器，如使用IK，可配置：analyzer = "ik_max_word" | " "            |
    | index    | Boolean | 是否索引                                           | true           |



- 编写仓库

  >写一个类继承 `ElasticsearchRepository<T, ID>`，需要写两个泛型，
  >第一个代表要存储的实体类型，
  >第二个代表主键类型，例如写一个Item类的仓储如下：

  ```java
  public interface ItemRepository extends ElasticsearchRepository<Item, Long> {
  
  }
  ```

- CRUD基础操作

  `ElasticsearchRepository`已经实现了一些方法，复杂的方法可通过自己实现，如：

  ```java
  
  /**
   * 关键字检索(匹配title和category)
   *
   * @param title    标题
   * @param category 类目
   * @param brand    品牌
   * @param pageable 分页
   * @return
   */
  Page<Item> findByTitleOrCategoryOrBrand(String title, String category, String brand, Pageable pageable);
  ```

  > 更多查询：https://docs.spring.io/spring-data/elasticsearch/docs/4.1.2/reference/html/#elasticsearch.operations.searchresulttypes