package com.philosup.demo;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.testcontainers.containers.MongoDBContainer;

// @SpringBootTest
@DataMongoTest
// @ContextConfiguration(initializers = MongoDBContainer)
class TestcontainerTests {

    private static MongoDBContainer mongoDbContainer;// = new MongoDBContainer("mongo:latest");

    // @BeforeAll
    // static void setup() {
    //     mongoDbContainer = new MongoDBContainer("mongo:latest");
    //     mongoDbContainer.addFileSystemBind("D:\\Work\\mongodb", "/data/db", BindMode.READ_WRITE);
    //     if(mongoDbContainer.isRunning() == false)
    //         mongoDbContainer.start();
    //     else
    //         assertTrue(false);
    // }

    // @AfterAll
    // static void tearDownAll(){
    //     if(!mongoDbContainer.isShouldBeReused()){
    //         mongoDbContainer.stop();
    //     }
    // }

    // @Test
    // void default_mongo_db_test() {
    //     assertTrue(mongoDbContainer.isRunning());
    //     // int port = mongoDbContainer.getMappedPort(27017);

    //     // MongoClient mongoClient = new MongoClient(mongoDbContainer.getContainerIpAddress(), port);
    //     // MongoDatabase database = mongoClient.getDatabase("test");

    //     // MongoCollection<Document> collection = database.getCollection("users");

    //     // Document document = new Document("name", "wonwoo").append("email", "test@test.com");

    //     // collection.insertOne(document);

    //     // FindIterable<Document> documents = collection.find();

    //     // assertThat(documents).hasSize(1);

    //     // documents.forEach((Consumer<? super Document>) it -> {

    //     //     assertThat(it.get("name")).isEqualTo("wonwoo");
    //     //     assertThat(it.get("email")).isEqualTo("test@test.com");

    //     // });

    // }
}
