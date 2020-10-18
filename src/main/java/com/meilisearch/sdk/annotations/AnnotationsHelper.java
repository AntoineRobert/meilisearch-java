package com.meilisearch.sdk.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnnotationsHelper {

	public static JSONObject buildDocument(Object object, MeiliEntityConfig entityConfig,
			Map<String, MeiliEntityConfig> uid_entity)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException, MeiliException {

		JSONObject root = new JSONObject();
		Class<?> clazz = object.getClass();

		for (Entry<String, MeiliPropertyConfig> identifier_object : entityConfig.getProperties().entrySet()) {

			if (identifier_object.getValue().getFieldName() != null) {
				Field field = clazz.getField(identifier_object.getValue().getFieldName());

				Object fieldValue = getValue(field.get(object), uid_entity);

				root.append(identifier_object.getKey(), fieldValue);

			}

			if (identifier_object.getValue().getMethodName() != null) {

				Method method = clazz.getMethod(identifier_object.getValue().getMethodName(), null);

				Object methodValue = getValue(method.invoke(object, null), uid_entity);

				root.put(identifier_object.getKey(), methodValue);
			}

		}

		return root;
	}

	public static String buildDocuments(Object object, MeiliEntityConfig entityConfig,
			Map<String, MeiliEntityConfig> uid_entity) {

		JSONArray array = new JSONArray();

		try {
			array.put(buildDocument(object, entityConfig, uid_entity));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException | MeiliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Document array : " + array);

		return array.toString();
	}

	public static MeiliEntityConfig buildMeiliEntity(String uid, Class<?> clazz) throws MeiliException {

		MeiliEntityConfig conf = new MeiliEntityConfig(uid);

		Field[] fields = clazz.getFields();
		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			for (Annotation annotation : method.getAnnotations()) {
				buildPropertyConfig(conf, annotation, method.getName(), null);
			}
		}

		for (Field field : fields) {
			for (Annotation annotation : field.getAnnotations()) {
				buildPropertyConfig(conf, annotation, null, field.getName());
			}
		}

		return conf;

	}

	public static void buildPropertyConfig(MeiliEntityConfig entity, Annotation annotation, String methodName,
			String fieldName) throws MeiliException {
		if (annotation instanceof MeiliProperty) {
			MeiliProperty myAnnotation = (MeiliProperty) annotation;

			MeiliPropertyConfig property = new MeiliPropertyConfig(myAnnotation.isKey(), myAnnotation.identifier());

			property.setFieldName(fieldName);
			property.setMethodName(methodName);

			if (property.isKey()) {
				if (entity.getPrimaryKey() != null) {
					throw new MeiliException(" entity has 2 primary keys : " + entity.getUid() + " :: "
							+ property.getIdentifier() + " :: " + entity.getPrimaryKey());
				}
			}

			if (entity.getProperties().get(property.getIdentifier()) != null) {
				throw new MeiliException(
						" property as same identifier : " + entity.getUid() + " :: " + property.getIdentifier());
			} else {
				entity.getProperties().put(property.getIdentifier(), property);
			}

		}
	}

	public static String getEntityName(Class<?> clazz) {

		Annotation[] annotations = clazz.getAnnotations();

		String entityIdentifier = null;

		for (Annotation annotation : annotations) {
			if (annotation instanceof MeiliIndex) {
				MeiliIndex myAnnotation = (MeiliIndex) annotation;
				entityIdentifier = myAnnotation.name();
			}
		}

		return entityIdentifier;
	}

	public static Object getValue(Object object, Map<String, MeiliEntityConfig> uid_entity)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchFieldException, MeiliException {

		if (object == null) {
			return null;
		}

		if (object instanceof Collection) {

			System.out.println("is colection ");

			Collection col = (Collection) object;

			if (!col.isEmpty()) {

				Class<?> clazz = col.iterator().next().getClass();

				String meiliEntityName = getEntityName(clazz);
				System.out.println("is colection of " + meiliEntityName);

				JSONArray array = new JSONArray();

				addEntityToCache(uid_entity, clazz, meiliEntityName);

				for (Object subObject : col) {
					array.put(buildDocument(subObject, uid_entity.get(meiliEntityName), uid_entity));
				}

				return array;

			}

		} else if (object instanceof Map) {

			System.out.println("is Map ");

			Map map = (Map) object;

			if (!map.isEmpty()) {

				Object[] keys = map.keySet().toArray();
				Object[] values = map.values().toArray();
				Class<?> clazz = values[0].getClass();
				String meiliEntityName = getEntityName(clazz);

				System.out.println("is map of " + meiliEntityName);

				JSONArray array = new JSONArray();

				addEntityToCache(uid_entity, clazz, meiliEntityName);

				JSONObject jsonMap = new JSONObject();

				for (int i = 0; i < values.length; i++) {
					jsonMap
							.append(keys[i].toString(),
									buildDocument(values[i], uid_entity.get(meiliEntityName), uid_entity));
				}

				array.put(jsonMap);

				return jsonMap;

			}

		} else {
			Class<?> clazz = object.getClass();

			String meiliEntityName = getEntityName(clazz);

			if (meiliEntityName != null) {

				addEntityToCache(uid_entity, clazz, meiliEntityName);

				return buildDocument(object, uid_entity.get(meiliEntityName), uid_entity);

			}

		}

		return object;
	}

	private static void addEntityToCache(Map<String, MeiliEntityConfig> uid_entity, Class<?> clazz,
			String meiliEntityName) throws MeiliException {
		if (!uid_entity.containsKey(meiliEntityName)) {
			uid_entity.put(meiliEntityName, buildMeiliEntity(meiliEntityName, clazz));
		}
	}

}
