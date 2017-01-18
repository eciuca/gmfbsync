package com.github.eciuca.gmfbsync.repo;

import com.google.inject.Inject;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by manu on 15/01/2017.
 */
public class PostedTopicsRepository {

    private final DB database;
    private HTreeMap<String, String> postedTopicsMap;

    @Inject
    public PostedTopicsRepository(DB database) {
        this.database = database;
    }

    public String find(String topicHash) {
        return (String) postedTopics().getOrDefault(topicHash, null);
    }

    public void save(String topicHash, String feedId) {
        postedTopics().put(topicHash, feedId);
        database.commit();
    }

    public void logRepoContents() {
        postedTopics().keySet().forEach(key -> {
            System.out.println("hash: " + key + " " + "fbid: " + postedTopics().get(key));
        });
    }

    public boolean exists(String topicHash) {
        return postedTopics().containsKey(topicHash);
    }

    private ConcurrentMap postedTopics() {
        if (postedTopicsMap == null) {
            postedTopicsMap = (HTreeMap<String, String>) database.hashMap("postedTopics").createOrOpen();
        }

        return postedTopicsMap;
    }
}
