package com.gitlab.projects;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author armdev
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class MongoConnection implements Serializable {

    private static final long serialVersionUID = 8365126215315864419L;

    private final String mongohost = "localhost";
    private final int mongoport = 27017;
    private final String mongoDB = "gitlabDB";
    private final MongoCollection<Document> userCollection;
    private final Gson gson = new Gson();

    public MongoConnection() {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).minConnectionsPerHost(0).threadsAllowedToBlockForConnectionMultiplier(5)
                .connectTimeout(30000).maxWaitTime(120000).maxConnectionIdleTime(0).maxConnectionLifeTime(0).connectTimeout(10000).socketTimeout(0)
                .socketKeepAlive(false).heartbeatFrequency(10000).minHeartbeatFrequency(500).heartbeatConnectTimeout(20000).localThreshold(15)
                .build();

        MongoClient mongo = new MongoClient(Arrays.asList(
                new ServerAddress(mongohost, mongoport)),
                options);
        MongoDatabase db = mongo.getDatabase(mongoDB);
        userCollection = db.getCollection("user");
    }

    @PostConstruct
    public void init() {

    }

    public SystenUser save(SystenUser data) {
        UUID userId = UUID.randomUUID();
        data.setId(userId.toString());
        final String json = gson.toJson(data);
        final BasicDBObject document = (BasicDBObject) JSON.parse(json);
        userCollection.insertOne(new Document(document));
        return data;
    }

    public SystenUser findById(String id) {
        SystenUser entity = null;
        Document query = new Document();
        query.put("id", id);
        for (Document doc : userCollection.find(query)) {
            entity = gson.fromJson(doc.toJson(), SystenUser.class);
        }
        // System.out.println("id is " + entity.get_id());
        return entity;
    }

    public SystenUser findByUsername(String username) {
        SystenUser entity = null;
        Document query = new Document();
        query.put("username", username);
        for (Document doc : userCollection.find(query)) {
            entity = gson.fromJson(doc.toJson(), SystenUser.class);
        }
        return entity;
    }

    public List<SystenUser> findAll() {
        final List<SystenUser> userList = new ArrayList<>();
        @SuppressWarnings("UnusedAssignment")
        SystenUser entity = new SystenUser();
        String sort = "registeredDate";
        String order = "desc";
        Bson sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        Document query = new Document();
        for (Document doc : userCollection.find(query).sort(sortCriteria)) {
            entity = gson.fromJson(doc.toJson(), SystenUser.class);
            userList.add(entity);
        }
        return userList;
    }

    public boolean updateProfile(String userId, SystenUser entity) {
        try {
            //  System.out.println(entity.get_id());
            // System.out.println("userId " + userId);
            Bson id = new Document("id", userId);

            Bson username = new Document("username", entity.getUsername());
            Bson url = new Document("url", entity.getUrl());
            Bson privateToken = new Document("privateToken", entity.getPrivateToken());

            Bson usernameUp = new Document("$set", username);
            Bson privateTokenUp = new Document("$set", privateToken);
            Bson urlUp = new Document("$set", url);

            userCollection.updateOne(
                    id,
                    usernameUp
            );
            userCollection.updateOne(
                    id,
                    urlUp
            );
            userCollection.updateOne(
                    id,
                    privateTokenUp
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUsersCount() {
        Long listCount = 0L;
        try {
            listCount = userCollection.count();
        } catch (Exception e) {
        }
        return listCount;
    }

    public Optional<SystenUser> login(String username, String privateToken) {
        SystenUser entity = null;
        Document query = new Document();
        query.put("username", username.trim());
        query.put("privateToken", privateToken.trim());
        for (Document doc : userCollection.find(query)) {
            entity = gson.fromJson(doc.toJson(), SystenUser.class);
        }
        return Optional.ofNullable(entity);
    }

}
