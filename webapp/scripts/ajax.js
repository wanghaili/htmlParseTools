$.extend({
	myAJAX:function(requestURL,datatosend,callback){
		$.ajax({
			type:"post",             //http请求方式"get"or"post"
			url:requestURL,
			async:false,             //请求方式,同步or异步,false表示同步请求
			data:datatosend,
			dataType:"json",
			crossDomain:true,
			success:function(responseData){
				if(typeof callback=="function"){
					callback(responseData);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				console.log(XMLHttpRequest.status);
                console.log(errorThrown);
                console.log(XMLHttpRequest.readyState);
                console.log(textStatus);
			}
		});
	}
});