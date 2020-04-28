package com.aws.lambda.online;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.lambda.online.data.RequestDetails;
import com.aws.lambda.online.data.ResponseDetails;

public class MySqlTest implements RequestHandler<RequestDetails, ResponseDetails>
{
   
	public ResponseDetails handleRequest(RequestDetails requestDetails, Context arg1) {
		
		// TODO Auto-generated method stub
		ResponseDetails responseDetails = new ResponseDetails();
				try {
					insertDetails(requestDetails, responseDetails);
				} catch (SQLException sqlException) {
					responseDetails.setMessageID("999");
					responseDetails.setMessageReason("Unable to Registor "+sqlException);
				}
				return responseDetails;
	}

	private void insertDetails(RequestDetails requestDetails, ResponseDetails responseDetails) throws SQLException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		String query = getquery(requestDetails);
		int responseCode = statement.executeUpdate(query);
		if(1 == responseCode)
		{
			responseDetails.setMessageID(String.valueOf(responseCode));
			responseDetails.setMessageReason("Successfully updated details");
		}
		
	}

	private String getquery(RequestDetails requestDetails) {
		
		String query = "INSERT INTO RAINDB.items(item_id, name) VALUES (";
		if (requestDetails != null) {
			query = query.concat("'" + requestDetails.getItem_id() + "','" 
					+ requestDetails.getName() + "')");
		}
		System.out.println("the query is "+query);
		return query;
	}

	private Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
				String evnURL= System.getenv("HOST");
				String evnUsername= System.getenv("Username");
				String evnPassword= System.getenv("Password");
				
				/*String url = "jdbc:mysql://cloud-rain-instance-1.ccefttx0ihm8.ap-south-1.rds.amazonaws.com:3306";
				String username = "rainusr";
				String password = "Rain12345";*/
				Connection conn = DriverManager.getConnection(evnURL, evnUsername, evnPassword);
				return conn;
	}

}
