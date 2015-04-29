package com.Peloton.DataEngineering;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.mysql.jdbc.Connection;


public class RedisTest {
	//create cache to store data to prevent database transaction
	public static Set<String> workoutsCache = new HashSet<String>();
	public static Set<String> achievementsCache = new HashSet<String>();
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SQLException {
		Jedis jedis = null;
		Connection mySQL = null;
		//Connecting to Redis server on localhost
		try{
			jedis = new Jedis("localhost");
			jedis.select(2);
			mySQL = connectToMySQL();
			if(mySQL!=null){
				System.out.println("Connection Successful to MySQL");
			}
			//Populate cache with existing data once
			workoutsCache = checkRecordsInSQL(mySQL, "workout", workoutsCache);
	        achievementsCache = checkRecordsInSQL(mySQL, "achievement", achievementsCache);
	        HashMap<String,Map<String,String>> workOutMap = new HashMap<String,Map<String,String>>();
	        HashMap<String,Map<String,String>> achievementMap = new HashMap<String,Map<String,String>>();
	        //Insert workout data
	        insertIntoSQL(mySQL,"workout*",jedis,workOutMap);
	        System.out.println("Inserted all workout objects in to MySQL");
	        //Insert achievement data
	        insertIntoSQL(mySQL,"achievement*",jedis,achievementMap);
	        System.out.println("Inserted all achievement objects in to MySQL");
		}
		catch(Exception e){
			System.out.println("Exception in main "+e);
		}
		finally{
			try {
				mySQL.close();
			} catch (SQLException e) {
				closeResources(mySQL,null,null);
				e.printStackTrace();
			}
		}
	}
	/**
	 * Scan keys iteratively and push to MySQL. This method avoids memory consumption with increase in data
	 * @param mySQL
	 * @param pattern
	 * @param jedis
	 * @param map
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	private static void insertIntoSQL(Connection mySQL,String pattern,Jedis jedis,HashMap<String,Map<String,String>> map) throws SQLException {
		int cursor = 1;
		int tempIndex = 0;
		List<String> tempList= null;
	    ScanResult<String> scan = null;
		try{
			//To avoid memory consumption with increase in data, use scan command to make iterative calls
			//After scanning few records push them to MySQL and continue this process till all keys are iterated
			ScanParams params = new ScanParams();
		    params.count(10000);
		    params.match(pattern);
		    while(cursor != 0){
		    	  if(tempIndex==0){
		    		  scan= jedis.scan(tempIndex, params);
		    	  }else{
		    		  scan= jedis.scan(cursor, params);
		    	  }
		    	  tempList= scan.getResult();
			      cursor = scan.getCursor();
			      Map<String,String> entries = new HashMap<String, String>();
			      for(String entry: tempList){
			    	  entries = jedis.hgetAll(entry);
			    	  map.put(entry,entries);
			      }
			      tempIndex++;
			      //push content to mySQL depending on pattern
			      if(pattern.equalsIgnoreCase("workout*")){
			    	  insertWorkOutDataIntoMySQL(map,mySQL,pattern.substring(0, pattern.length()-1));
			      }
			      else{
			    	  insertAchievementDataIntoMySQL(map,mySQL,pattern.substring(0, pattern.length()-1));
			      }
			      
		      }
		}
		catch(Exception e){
			closeResources(mySQL,null,null);
			System.out.println("Exception "+e+" insertIntoSQL" );
			System.exit(0);
		}
	}
	/**
	 * Insert in to workout schema
	 * @param workOutMap
	 * @param mySQL
	 * @param tableName
	 * @throws SQLException 
	 */
	private static void insertWorkOutDataIntoMySQL(HashMap<String, Map<String, String>> workOutMap, Connection mySQL, String tableName) throws SQLException {
		//while inserting -> if key in the map is already present table do not insert
		PreparedStatement preparedStatement = null;
		boolean oldRecord = false;
		//Covert Id fields to bytes using UNHEX to avoid SQL Injection attacks
		String insertTableSQL = "INSERT INTO "
				+ tableName
				+ " (workout_id, peloton_id, user_id, ride_id, is_qualifying, total_work, created, start_time, end_time, device_id, total_zero_seconds) VALUES (UNHEX(?),UNHEX(?),UNHEX(?),UNHEX(?),?,?,?,?,?,UNHEX(?),?)";
		int index = 0;
		try {
			preparedStatement = mySQL.prepareStatement(insertTableSQL);
			for (Entry<String, Map<String, String>> entry : workOutMap.entrySet()){
				String[] parse = entry.getKey().split(":");
				//Insert only if record does not exist in database
				oldRecord = workoutsCache.contains(parse[1].toLowerCase().trim());
				if(!oldRecord){
					InputStream workout_id = new ByteArrayInputStream(parse[1].getBytes(StandardCharsets.UTF_8));
					boolean is_qualify = false;
					preparedStatement.setBinaryStream(1, workout_id);
					Map<String,String> record = entry.getValue();
					if(record.get("peloton_id")!=null){
						preparedStatement.setBinaryStream(2, new ByteArrayInputStream(record.get("peloton_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(2,Types.NULL);
					}
					if(record.get("user_id")!=null){
						preparedStatement.setBinaryStream(3, new ByteArrayInputStream(record.get("user_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(3,Types.NULL);
					}
					if(record.get("ride_id")!=null){
						preparedStatement.setBinaryStream(4, new ByteArrayInputStream(record.get("ride_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(4,Types.NULL);
					}
					if(record.get("is_qualifying")!=null){
						if(record.get("is_qualifying").equalsIgnoreCase("True")){
							is_qualify = true;
						}
						else{
							is_qualify = false;
						}
					}
					if(record.get("is_qualifying")!= null){
						preparedStatement.setBoolean(5,is_qualify);
					}
					else{
						preparedStatement.setNull(5,Types.NULL);
					}
					if(record.get("total_work")!=null){
						preparedStatement.setDouble(6, Double.parseDouble(record.get("total_work")));
					}
					else{
						preparedStatement.setNull(6,Types.NULL);
					}
					if(record.get("created")!=null){
						preparedStatement.setTimestamp(7, new Timestamp(Long.valueOf(record.get("created")) * 1000));
					}
					else{
						preparedStatement.setNull(7,Types.NULL);
					}
					if(record.get("start_time")!=null){
						preparedStatement.setTimestamp(8, new Timestamp(Long.valueOf(record.get("start_time")) * 1000));
					}
					else{
						preparedStatement.setNull(8,Types.NULL);
					}
					if(record.get("end_time")!=null){
						preparedStatement.setTimestamp(9, new Timestamp(Long.valueOf(record.get("end_time")) * 1000));
					}
					else{
						preparedStatement.setNull(9,Types.NULL);
					}
					if(record.get("device_id")!=null){
						preparedStatement.setBinaryStream(10, new ByteArrayInputStream(record.get("device_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(10,Types.NULL);
					}
					if(record.get("total_zero_seconds")!=null){
						preparedStatement.setLong(11, (long)Long.parseLong(record.get("total_zero_seconds")));
					}
					else{
						preparedStatement.setNull(11,Types.NULL);
					}
					preparedStatement.addBatch();
					//add to cache to avoid db transaction
					workoutsCache.add(parse[1].toLowerCase().trim());
					// Execute every 3000 items
					if ((index + 1) % 3000 == 0) {
						preparedStatement.executeBatch();
						System.out.println("Inserted first "+index +" records in to MySQL");
		            }
					index++;
				}
				
			}
			preparedStatement.executeBatch();
 
		} catch (SQLException e) {
			closeResources(mySQL,preparedStatement,null);
			System.out.println(e.getMessage());
			System.exit(0);
		}
		finally{
			try{
				preparedStatement.close();
			}
			catch(Exception e){
				System.out.println("Exception in finally try of insertWorkOutDataIntoMySQL");
				System.exit(0);
			}
		}
	}
	/**
	 * Insert into achievement schema
	 * @param achievementMap
	 * @param mySQL
	 * @param tableName
	 * @throws SQLException 
	 */
	private static void insertAchievementDataIntoMySQL(HashMap<String, Map<String, String>> achievementMap, Connection mySQL, String tableName) throws SQLException {
		PreparedStatement preparedStatement = null;
		boolean oldRecord = false;
		//Covert Id fields to bytes using UNHEX to avoid SQL Injection attacks
		String insertTableSQL = "INSERT INTO "+ tableName+ " (achievement_id, user_id, template_id, workout_id, instant) VALUES (UNHEX(?),UNHEX(?),UNHEX(?),UNHEX(?),?)";
		int index = 0;
		try {
			preparedStatement = mySQL.prepareStatement(insertTableSQL);
			for (Entry<String, Map<String, String>> entry : achievementMap.entrySet()){
				String[] parse = entry.getKey().split(":");
				//Insert only if record does not exist in database
				oldRecord = achievementsCache.contains(parse[1].toLowerCase().trim());
				if(!oldRecord){
					InputStream achievement_id = new ByteArrayInputStream(parse[1].getBytes(StandardCharsets.UTF_8));
					preparedStatement.setBinaryStream(1, achievement_id);
					Map<String,String> record = entry.getValue();
					//As workout_id is foreign key referencing to  workout schema make sure it actually exists in workout schema
					if(!workoutsCache.contains(record.get("workout_id").toLowerCase().trim())){
						continue;
					}
					if(record.get("user_id")!=null){
						preparedStatement.setBinaryStream(2, new ByteArrayInputStream(record.get("user_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(2,Types.NULL);
					}
					if(record.get("template_id")!=null){
						preparedStatement.setBinaryStream(3, new ByteArrayInputStream(record.get("template_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(3,Types.NULL);
					}
					if(record.get("workout_id")!=null){
						preparedStatement.setBinaryStream(4, new ByteArrayInputStream(record.get("workout_id").getBytes(StandardCharsets.UTF_8)));
					}
					else{
						preparedStatement.setNull(4,Types.NULL);
					}
					if(record.get("instant")!=null){
						preparedStatement.setTimestamp(5, new Timestamp(Long.valueOf(record.get("instant")) * 1000));
					}
					else{
						preparedStatement.setNull(5,Types.NULL);
					}
					preparedStatement.addBatch();
					//add to cache to avoid db transaction
					achievementsCache.add(parse[1].toLowerCase().trim());
					// Execute every 1000 items
					if ((index + 1) % 1000 == 0) {
						preparedStatement.executeBatch();
		            }
					index++;
				}
				
			}
			preparedStatement.executeBatch();
 
		} catch (SQLException e) {
			closeResources(mySQL,preparedStatement,null);
			System.out.println(e.getMessage());
			System.exit(0);
		}
		finally{
			try{
				preparedStatement.close();
			}
			catch(Exception e){
				System.out.println("Exception in finally try of insertWorkOutDataIntoMySQL");
				System.exit(0);
			}
		}
	}
	/**
	 * Populates cache initially if any records exist in table. This function ensures program is idempotent
	 * @param connection
	 * @param tableName
	 * @param cache
	 * @return
	 * @throws SQLException 
	 */
	private static Set<String> checkRecordsInSQL(Connection connection, String tableName, Set<String> cache) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		final String queryCheck = "SELECT HEX("+tableName+"_id) as ID from "+tableName;
		try{
			preparedStatement = connection.prepareStatement(queryCheck);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				cache.add(resultSet.getString("ID").toLowerCase());
			}
		}
		catch(Exception e){
			closeResources(connection,preparedStatement,resultSet);
			System.out.println("Exception "+e+ " in checkRecordInSQL");
		}
		finally{
			try{
				preparedStatement.close();
				resultSet.close();
			}
			catch(Exception e){
				System.out.println("Exception "+e +" in checkRecordsInSQL");
			}
		}
		return cache;
	}
	/**
	 * Make a MySQL connection
	 * @return
	 */
	private static Connection connectToMySQL() {
		Properties properties = new Properties();
		InputStream db_properties = null;
		Connection connection = null;
		try{
			db_properties = new FileInputStream("lib"+File.separator+"authentication.properties");
			properties.load(db_properties);
			Class.forName(properties.getProperty("JDBC_DRIVER"));
			connection = (Connection) DriverManager.getConnection(properties.getProperty("MySQLURL"),properties.getProperty("MySQLUSER"),properties.getProperty("MySQLPASSWORD"));
		}
		catch(Exception e){
			System.out.println("Exception "+ e +" occured while connecting to MySQL");
			System.exit(0);
		}
		return connection;
	}
	/**
	 * Make sure there are no connection leakages
	 * @param connection
	 * @param preparedStatement
	 * @param resultSet
	 * @throws SQLException 
	 */
	private static void closeResources(Connection connection,
			PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
		//Close all objects if not closed earlier
		if(connection!= null && !connection.isClosed()){
			connection.close();
		}
		if(preparedStatement != null && !preparedStatement.isClosed()){
			preparedStatement.close();
		}
		if(resultSet!=null && !resultSet.isClosed()){
			resultSet.close();
		}
		// TODO Auto-generated method stub
		
	}
}
