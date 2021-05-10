package fr.uavignon.cerimuseum.data;

import java.util.HashMap;

class CollectionResult {
    public void transferCollectionInfo(getCollection collectionresult, HashMap<String, getCollection.ItemResponse> collection){
        collection.putAll(collectionresult.collection);
    }
}
