package com.jpreddy.urlpinger

//Q.2.1
case class HttpResponseModel(
		val requestStartLine:String, 
		val responseStatusLine:String, 
		val cookie:String, 
		val date:String, 
		val messageBody:String
		
		) 
		{
  override def toString() ={
    val respString=new String(s"[requestStartLine=${this.requestStartLine}], [responseStatusLine=$responseStatusLine], [cookie=$cookie], [date=$date], [messageBody=$messageBody]")
    respString
  }
		}

