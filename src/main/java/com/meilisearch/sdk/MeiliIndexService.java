package com.meilisearch.sdk;

import java.util.HashMap;
import java.util.Map;

import com.meilisearch.sdk.annotations.AnnotationsHelper;
import com.meilisearch.sdk.annotations.MeiliEntityConfig;

public class MeiliIndexService {

	private Map<String, String> uid_key = null;
	private Map<String, MeiliEntityConfig> uid_Entity = new HashMap<String, MeiliEntityConfig>();

	private Client client;

	public void index(Object object) throws Exception {

		String uid = AnnotationsHelper.getEntityName(object.getClass());

		if (uid != null) {

			MeiliEntityConfig entity = getCacheUidEntity().get(uid);

			if (entity == null) {
				entity = AnnotationsHelper.buildMeiliEntity(uid, object.getClass());
			}

			if (!getCacheUidKey().containsKey(uid)) {
				createNewIndex(entity);
			}

			updatePrimaryKey(entity.getUid(), entity.getPrimaryKey());

			getCacheUidEntity().put(entity.getUid(), entity);

			beginIndex(object, entity);

		}

	}

	public void setClient(Client client) {
		this.client = client;
	}

	private void beginIndex(Object object, MeiliEntityConfig entityConfig) {

		System.out.println("cache_config :: " + uid_Entity);

		String document = AnnotationsHelper.buildDocuments(object, entityConfig, uid_Entity);

		try {
			getClient().getIndex(entityConfig.getUid()).addDocuments(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private MeiliEntityConfig createNewIndex(MeiliEntityConfig conf) {
		try {

			Index index = getClient().createIndex(conf.getUid(), conf.getPrimaryKey());
			getCacheUidKey().put(index.getUid(), index.getPrimaryKey());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conf;

	}

	private Map<String, MeiliEntityConfig> getCacheUidEntity() {
		return uid_Entity;
	}

	private Map<String, String> getCacheUidKey() {

		if (uid_key == null) {
			uid_key = new HashMap<String, String>();
			initCache();
		}

		return uid_key;
	}

	private Client getClient() {
		return client;
	}

	private void initCache() {
		try {
			Index[] indexes = getClient().getIndexList();

			for (Index index : indexes) {
				uid_key.put(index.getUid(), index.getPrimaryKey());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updatePrimaryKey(String uid, String primaryKey) {

		String cachedPrimaryKey = getCacheUidKey().get(uid);

		if (cachedPrimaryKey == null && primaryKey != null || !cachedPrimaryKey.equals(primaryKey)) {
			try {
				client.updateIndex(uid, primaryKey);
				getCacheUidKey().put(uid, primaryKey);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
