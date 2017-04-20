package com.jpreddy.urlpinger

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object URLPingerApp extends App {
  
  val logger = LoggerFactory.getLogger("com.jpreddy.urlpinger")

	//  Q2.2
	val urlList = List[String](
			"http://www.newscorpaustralia.com/case-studies",
			"https://www.cloudera.com/documentation/enterprise/latest/topics/admin_howto_multitenancy.html",
			"http://spark.apache.org/docs/latest/graphx-programming-guide.html",
			"https://www.test.com/"
			)
	logger.debug("*** Q2.2 : Printing the list of URLs ***")
  urlList.foreach(logger.debug(_))
  
  logger.debug("")
  logger.debug("*** Printing the responses for URLs(actually saving them to db with db.save) ***")
	val urlResponses = urlList.map( sendGet(_) )  
	urlResponses.foreach(db.save)
	
	//Q2.3
	logger.debug("")
	logger.debug("*** Q2.3: Printing the responses to console  for URLs in test.com domain ***")
	urlResponses.filter(x => x.requestStartLine.contains("test.com")).foreach(println)
	
	//Q2.4
	
	def findURLsWithCookie() :List[Any]= {
    return db.find("cookie","\\S") // Just checking the non empty 'cookie' attribute
}
			
			
			// HTTP GET request
			def sendGet(requestUrl: String): HttpResponseModel = {

					val client = new DefaultHttpClient();
					val request = new HttpGet(requestUrl);
          val USER_AGENT = "Mozilla/5.0";

          request.addHeader("User-Agent", USER_AGENT);
					val response = client.execute(request);
					val result = new StringBuffer();
					
					val httpResponseModel = new HttpResponseModel(
							request.getRequestLine.toString,
							response.getStatusLine.toString,
							if(response.getHeaders("Set-Cookie").length !=0) response.getHeaders("Set-Cookie")(0).getValue else "",
							if(response.getHeaders("Date").length !=0) response.getHeaders("Date")(0).getValue else "",
							result.toString()
							)

					logger.debug(httpResponseModel.toString)
					
					httpResponseModel
	}

	//Q2.3
	//  urlResponses.filter(
}


object db{

	/*
	 * Saves an arbitrary object in a NoSQL database
	 */
	def save(obj: Any)={}
	/*
	 * Find all objects whose attibute @attr matches the regular
	 * expression @what
	 */
	def find(attr: String, whatRegExp: String):List[Any] ={List()}
}