<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sample Service Client</title>
<script src="jquery-3.1.1.min.js"></script>


<script type="text/javascript">

   $( document ).ready(function() {

   clrscr = function(){
		$("#result").val('');
	};

   print = function(s){
		$("#result").val($("#result").val()+ "" + s + '\n');
	    $("#result").attr("scrollTop",$("#result").attr("scrollHeight"));
   };
   
   $( document ).ajaxError(function(event, jqxhr, settings, e ) {
			if (e.message){
			  alert(e.message);
			  print(" Exception: \n\n" + e.message);
			  $( ".log" ).text( e.message );
		    }else{
			  alert(e);
			  print(" Exception: \n\n" + e);
			  $( ".log" ).text( e.message );
			}
    }); //eof ajax error 
   
    refresh = function() {
		clrscr();
  	    $.ajax({
  	        type: "GET",
  	        //headers: {'Cookie' : document.cookie },
  	        cache: false,
  	      // NO setCookies option available, set cookie to document
  	      //setCookies: "lkfh89asdhjahska7al446dfg5kgfbfgdhfdbfgcvbcbc dfskljvdfhpl",
  	        crossDomain: true,
  	        xhrFields: {
  	         withCredentials: true
  	        },
	        url: $("#serverUrl").val()+"rest/session/sessionInfo",
	        jsonp: "callback",
	        dataType:"jsonp",
 	        success: function(response){
	           print(JSON.stringify(response.result,null,' ')); 
	        }
	    });//eof ajax
 	};
    
 	stats = function() {
		clrscr();
  	    $.ajax({
  	        type: "GET",
  	        //headers: {'Cookie' : document.cookie },
  	        cache: false,
  	      // NO setCookies option available, set cookie to document
  	      //setCookies: "lkfh89asdhjahska7al446dfg5kgfbfgdhfdbfgcvbcbc dfskljvdfhpl",
  	        crossDomain: true,
  	        xhrFields: {
  	         withCredentials: true
  	        },
	        url: $("#serverUrl").val()+"rest/cache/stats",
	        jsonp: "callback",
	        dataType:"jsonp",
 	        success: function(response){
	           print(JSON.stringify(response.result,null,' ')); 
	        }
	    });//eof ajax
 	};
 	
    save = function(){
	     var key = $("#attrKey").val();
	     var value = $("#attrValue").val();
	     $.ajax({
	        type:"GET",
	        //headers: {'Cookie' : document.cookie },
	        cache: false,
  	      // NO setCookies option available, set cookie to document
  	      //setCookies: "lkfh89asdhjahska7al446dfg5kgfbfgdhfdbfgcvbcbc dfskljvdfhpl",
  	        crossDomain: true,
  	        xhrFields: {
  	         withCredentials: true
  	        },
	        url: $("#serverUrl").val()+"rest/session/setAttribute",
	        jsonp: "callback",
	        dataType:"jsonp",
	        data: {
	          key: key,
	          value: value
	        },
	        
	        success: function(response){
	           refresh(); 
	        }
	     });//eof ajax
 	};
 	 	 
    });//eof dom ready!
        
</script>
</head>
<body>

	<h2>Http Session Value Test</h2>
	Session Id  :
	<b><%=request.getSession(true).getId()%></b></br>
	User Name :<b><%=request.getRemoteUser()%></b></br> 
	<table>
		<tr>
			<td>Server URL:</td>
			<td><input id="serverUrl" type="text" size="80"
				value='<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getRequestURI()%>' /></td>
			<td><input id="btnRefresh" type="button"
				value="Get Session Values" onclick="refresh();" /></td>
			<td><input id="btnStats" type="button"
				value="Get Cache Stats" onclick="stats();" /></td>	
		</tr>
		<tr>
			<td>Session Attribute:</td>
			<td><input id="attrKey" type="text" size="33" title="Key" /> <input
				id="attrValue" type="text" size="45" title="Value" /></td>
			<td><input id="btnSave" type="button" value="Save to Session"
				onclick="save();" /></td>
	</table>
	<br />Session Info
	<br />
	<textarea wrap="on" id="result" cols="120" rows="30"></textarea>
	<br />
</body>
</html>