package com.mycellspeed.main.aws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.mycellspeed.main.data.DataUnit;
import com.mycellspeed.main.singleton.DataUnitHandler;

public class DynamoDBHandler {
	
	private static final String DDB_ENDPOINT = "dynamodb.us-west-2.amazonaws.com";
	private static final String ACCESS_KEY_ID = "AKIAJBT3LEN33YZIUR4A";
	private static final String SECRET_KEY = "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";
	
	private static AmazonDynamoDBClient client;
	
	public DynamoDBHandler() {}
	
	//TODO load properties
	public static QueryResult propertyLoader() {
		// Set up properties client
		AmazonDynamoDBClient tempClient = new AmazonDynamoDBClient(new BasicAWSCredentials("id","key"));
		
		// Create Get-Request and pull information
		QueryRequest request = new QueryRequest().withTableName("name of property table");
		
		return tempClient.query(request);
	}
	//TODO setup connection client
	private static boolean setClient() {
		try {
			//TODO replace with properties
			client = new AmazonDynamoDBClient(new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY));
			client.setEndpoint(DDB_ENDPOINT);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}
	
	private static void clearClient() {
		client = null;
	}
	
	//TODO operations to perform
	public static boolean loadData(String tableName) {
		try {
			setClient();
			
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			for (DataUnit unit: DataUnitHandler.getInstance().retrieveData()) {
				if (unit.getValue() != null) {
					Log.i("DDB Handler - Unit Item", "K=" + unit.getKey() + ",V=" + unit.getValue() + ",T=" + unit.getType());
					if (unit.getType().equals("N"))
						item.put(unit.getKey(), new AttributeValue().withN(unit.getValue()));
					else if (unit.getType().equals("S"))
						item.put(unit.getKey(), new AttributeValue().withS(unit.getValue()));
					else 
						Log.i("DynamoDBHandler", "Error loading data into map: K=" + unit.getKey() + ",V=" + unit.getValue() + ",T=" + unit.getType());
				}
			}
			item.put("TS", new AttributeValue().withN("" + new Date().getTime()));
			
			/*
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			
			item.put("TS", new AttributeValue().withN("" + new Date().getTime()));
			item.put("IMSI", new AttributeValue().withN("310009193958167"));
			item.put("IMEI", new AttributeValue().withS("A000003905456F"));
			item.put("PhnNbr", new AttributeValue().withN("9192477513"));
			item.put("NetOper", new AttributeValue().withS("Sprint"));
			item.put("SimOper", new AttributeValue().withS("Sprint"));
			item.put("SimSrlNbr", new AttributeValue().withN("89011200000190717297"));
			item.put("SimState", new AttributeValue().withN("5"));
			item.put("SimCtryCode", new AttributeValue().withS("us"));
			item.put("PhnType", new AttributeValue().withS("CDMA"));
			item.put("NetType", new AttributeValue().withS("EVDO A"));
			item.put("MNC", new AttributeValue().withN("120"));
			item.put("MCC", new AttributeValue().withN("310"));
			item.put("GrpId", new AttributeValue().withN("310120"));
			item.put("DvcLat", new AttributeValue().withN("39.5446"));
			item.put("DvcLon", new AttributeValue().withN("-104.8562"));
			item.put("Mnfctr", new AttributeValue().withS("Samsung"));
			item.put("Brand", new AttributeValue().withS("Samsung"));
			item.put("Device", new AttributeValue().withS("SPH-D710"));
			item.put("Model", new AttributeValue().withS("SPH-D710"));
			item.put("OS", new AttributeValue().withS("Jelly Bean - V 4.1.2"));
			item.put("DLSTR", new AttributeValue().withN("12.85"));
			item.put("ULSTR", new AttributeValue().withN("12.85"));
			*/
			
			PutItemRequest request = new PutItemRequest().withTableName(tableName).withItem(item);
			client.putItem(request);
			
			return true;
		} catch (Throwable e) {
			Log.e("DynamoDBHandler", "MAJOR ERROR: " + e.getMessage());
			clearClient();
			return false;
		}
	}
}
