var hiddenCLS="hidden";
var keywords=[];

$(function(){
	//文本文件上传
	$(".upload-btn").on("click",function(){
		$(".file-upload").trigger("click");
	})
	
	$(".file-upload").on("change",function(){
		$("#myupload").ajaxSubmit({
			dataType:"json",
			success:function(responseData){
				$(".file-input").val(responseData.fileName);
				$(".input-text").text(responseData.content);
			},
			error:function(xhr){
				console.log("error");
	            console.log(xhr.responseText); //返回失败信息
			}
		})
	});
	
	//分析文本
	$(".analysis-btn").on("click",function(){
		var datatosend={};
		
		datatosend.splitContent=$(".input-text").val();
		
		var requestURL="parser/splitWords";
		$.myAJAX(requestURL,datatosend,function(data){
			console.log(data);
			
			//加载分词结果
			var wordmeanList=makeWordmean(data.result.wordMeans);
			var wordList=wordsCom(data.result.words);
			
			$("#splitwords").empty().append(wordList);
			$("#wordmean").empty().append(wordmeanList);
			
			//加载停用词结果
			var stopwordmean=makeWordmean(data.result.stopWordMeans);
			var stopwords=wordsCom(data.result.stopWords);
			
			$("#stopwords").empty().append(stopwords);
			$("#stopwordmean").empty().append(stopwordmean);
			
			
			//加载关键词提取的结果
			keywords=data.result.keyWords;
			var keywordsNum=keywords.length,tempNum=keywords.length;
			var end=Math.ceil(keywordsNum/2);
			if(keywordsNum>10){
				end=5;
				tempNum=10;
			}

			var preKeywords=keywordsCom(data.result.keyWords,0,end);
			var nextKeywords=keywordsCom(data.result.keyWords,end,tempNum);
			
			$(".pre-keywords table tbody").empty().append(preKeywords);			
			$(".next-keywords table tbody").empty().append(nextKeywords);
			
			//可视化展示
			$("#visual-display-result").empty();
			visualDis(data.result.keyWords.slice(0,tempNum));
		});
	});
	
	//分词结果和停用词结果切换
	$(".text-deal-title .menu-ul a").on("click",function(e){
		var $target=$(e.target),
			curValue=$target.data("value");
		
		$(".text-deal-title .menu-ul a").parent().removeClass("text-deal-active");
		$target.parent().addClass("text-deal-active");
		
		$(".result-splitwords-wrap").addClass(hiddenCLS);
		$(".result-stopwords-wrap").addClass(hiddenCLS);
		
		$(".result-"+curValue+"-wrap").removeClass(hiddenCLS);
	});
	
	//关键词个数自主输入
	$(".keywords-btn").on("click",function(){
		var keywordsNum=$(".keywords-title input").val();
		
		var reg=new RegExp('^[1-9][0-9]*$');
		if(reg.test(keywordsNum)){
			if(keywordsNum>=5 && keywordsNum<=20){
				
				if(keywordsNum<=keywords.length){
					var end=Math.ceil(keywordsNum/2);
					//获取关键词提取结果，并加载
					var preKeywords=keywordsCom(keywords,0,end);
					var nextKeywords=keywordsCom(keywords,end,keywordsNum);
							
					$(".pre-keywords table tbody").empty().append(preKeywords);						
					$(".next-keywords table tbody").empty().append(nextKeywords);

					//可视化展示
					$("#visual-display-result").empty();
					visualDis(keywords.slice(0,keywordsNum));
				}else{
					alert("超出关键词总数，请重新输入");
					return;
				}			
			}else{
				alert("请输入正确的关键词个数：5-20");
				return;
			}
		}else{
			alert("您输入的关键词个数不合法，请重新输入");
			return;
		}
	});
});

//生成词性类别图示
function makeWordmean(mean) {
	var wordmeanList = "<dt>词性类别图示：</dt>";
	var meanArr=[];
	for (var i = 0; i < mean.length; i++) {
		var curMean = mean[i];
		var dd = '<dd class="CLASSNAME">WORDNAME</dd>';
		if (wordmean[mean[i]] == null) {
			curMean = mean[i].charAt(0);
		}
		if ($.inArray(curMean, meanArr)>-1) {
			continue;
		}
		meanArr.push(curMean);
		dd = dd.replace("CLASSNAME", curMean).replace("WORDNAME", wordmean[curMean]);
		wordmeanList += dd;
	}
	return wordmeanList;
}
//生成词性分析中words
function wordsCom(data) {
	var words = "";
	for (var i = 0; i < data.length; i++) {
		var nominal = data[i].wordMean;
		if (nominal == "line") {
			words += "<br/>";
			continue;
		}
		if (wordmean[nominal] == null) {
			nominal = nominal.charAt(0);
		}
		var dd = '<dd class="CLASSNAME" title="TITLE">WORDNAME</dd>';
		dd = dd.replace("CLASSNAME", nominal).replace("TITLE", wordmean[nominal]).replace("WORDNAME", data[i].word);
		words += dd;
	}
	return words;
}
//关键词提取
function keywordsCom(keywords,start,end){
	var keywordsList="";
	
	for(var i=start;i<end;i++){
		var tr='<tr><td>KEYWORD</td><td>WEIGHT</td>';
		tr=tr.replace("KEYWORD",keywords[i].keyword).replace("WEIGHT",keywords[i].weight);
		keywordsList+=tr;
	}
	return keywordsList;
}

//可视化展示
function createRandomItemStyle() {
	return {
		normal: {
			color: 'rgb(' + [
				Math.round(Math.random() * 160),
				Math.round(Math.random() * 160),
				Math.round(Math.random() * 160)
			].join(',') + ')'
		}
	};
}

function visualDis(data){
	var keywordList=[];
	
	for(var i=0;i<data.length;i++){
		var obj={};
		obj.name=data[i].keyword;
		obj.value=data[i].weight;
		obj.itemStyle=createRandomItemStyle();
		keywordList.push(obj);
	};

	require.config({
        paths: {
            echarts:"scripts/dist"
        }
    });
    require(
		[
			'echarts',
			// 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
			'echarts/chart/wordCloud'
		],

		function(ec) {
			var myChart = ec.init(document.getElementById("visual-display-result"));
		
			var option = {
				title: {
					text: '网页标签',
					link: ''
				},
				tooltip: {
					show: true
				},
				series: [{
					name: 'page tag',
					type: 'wordCloud',
					size: ['80%', '80%'],
					textRotation: [0, 45, 90, -45],
					textPadding: 0,
					autoSize: {
						enable: true,
						minSize: 14
					},
					data: keywordList
				}]
			};
			myChart.setOption(option);
		}
	);
}