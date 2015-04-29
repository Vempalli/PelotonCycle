package com.Peleton.BackEndTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.PriorityQueue;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("/merge")
public class Sample {
	static int index;
	//priority queue by defaults keeps the elements in sorted order
	static PriorityQueue<Long> queue = new PriorityQueue<Long>();
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String callMe(
			@DefaultValue("STREAM-A-DEFAULT") @QueryParam("stream1")String StreamA, 
			@DefaultValue("STREAM-B-DEFAULT") @QueryParam("stream2")String StreamB
			) throws Exception{
		if(StreamA.equalsIgnoreCase("STREAM-A-DEFAULT") ||StreamA.equalsIgnoreCase("STREAM-B-DEFAULT")){
			return "Please supply StreamA and StreamB parameters";
		}
		//Call API end point
		connectToEndpoint(StreamA);
		connectToEndpoint(StreamB);
		Long last = queue.poll();
		Long current = queue.peek();
		return "{\n \"last\":"+last+"\n \"current\":"+current+"\n}";
	}
	//Fetch the stream from given end point
	public void connectToEndpoint(String streamName) throws IOException, ParseException{
		try {
			URL StreamURL = new URL("https://api.pelotoncycle.com/quiz/next/"+streamName);
			URLConnection connection = StreamURL.openConnection();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			readDataFromStream(bufferReader);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	//push the streamed data in to queue
	private void readDataFromStream(BufferedReader bufferReader) throws IOException, ParseException {
		String line="";
		while ((line = bufferReader.readLine()) != null) {
			//read response as JSON
			JSONParser Json = new JSONParser();
			Object obj = Json.parse(line);
			JSONObject jsonObj = (JSONObject) obj;
			long last = (long) jsonObj.get("last");
			long current = (long) jsonObj.get("current");
			if(index <= 1){
				queue.add(last);
				index++;
			}
			queue.add(current);
			System.out.println(jsonObj);
		}
		
	}
}
