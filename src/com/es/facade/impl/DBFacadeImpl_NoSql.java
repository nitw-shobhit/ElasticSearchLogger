package com.es.facade.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.es.core.BaseBean;
import com.es.facade.DBFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBFacadeImpl_NoSql<T> implements DBFacade<T> {

	@Override
	public void persist(T obj) {
		Client client = getTransportClient();
		IndexRequest request = new IndexRequest(((BaseBean) obj).getSchemaName(), ((BaseBean) obj).getEntityName());
		Gson gson = new GsonBuilder().create();
		request.source(gson.toJson(obj));
		client.index(request);
	}

	@Override
	public List<T> find(Param param) {
		Client client = getTransportClient();
		MatchQueryBuilder query = QueryBuilders.matchQuery(param.getName(), param.getValue());
		SearchResponse response = client.prepareSearch().setQuery(query)
		        .execute()
		        .actionGet();
		SearchHit[] results = response.getHits().getHits();
		List<T> listResult = new ArrayList<T>();
		Gson gson = new GsonBuilder().create();
		for(SearchHit hit : results){
            String sourceAsString = hit.getSourceAsString();
            if (sourceAsString != null) {
                listResult.add(gson.fromJson(sourceAsString, getGenericClass()));
            }
        }
		
		client.close();
		return listResult;
	}

	@Override
	public List<T> findAll() {
		Client client = getTransportClient();
		MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
		SearchResponse response = client.prepareSearch().setQuery(query)
		        .execute()
		        .actionGet();
		SearchHit[] results = response.getHits().getHits();
		List<T> listResult = new ArrayList<T>();
		Gson gson = new GsonBuilder().create();
		for(SearchHit hit : results){
            String sourceAsString = hit.getSourceAsString();
            if (sourceAsString != null) {
                listResult.add(gson.fromJson(sourceAsString, getGenericClass()));
            }
        }
		
		client.close();
		return listResult;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getGenericClass() {
		 ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
	     return (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	protected TransportClient getTransportClient() {
		TransportClient tc = TransportClientFactory.createTransportClient();
		return tc;
	}
}
