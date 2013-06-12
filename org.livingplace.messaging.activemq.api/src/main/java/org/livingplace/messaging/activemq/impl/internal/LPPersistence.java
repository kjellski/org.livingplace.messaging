/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.livingplace.messaging.internal;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iFlat
 */
public class LPPersistence {

    private static Mongo mongo = null;
    private static DB db =null;

    private LPPersistence() {
        // Exists only to defeat instantiation.
    }


    public synchronized static Mongo getMongoInstance(String ip, int port) throws MongoException, UnknownHostException {
        
        if(mongo == null){
            try {
                mongo = new Mongo(ip, port);
            } catch (UnknownHostException ex) {
                Logger.getLogger(LPPersistence.class.getName()).log(Level.SEVERE, null, ex);
                throw new UnknownHostException();
            } catch (MongoException ex) {
                Logger.getLogger(LPPersistence.class.getName()).log(Level.SEVERE, null, ex);

                MongoException m = new MongoException("We can't connect to the MongoDB");
                m.setStackTrace(ex.getStackTrace());
                throw m;
            }
        }

        return mongo;
    }

    public synchronized static DB getDBInstance(){
        
        if(db == null && mongo != null){
            db = mongo.getDB("amqmessages");
        }

        return db;
    }

}
