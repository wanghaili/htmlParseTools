var hiddenCLS="hidden";
var keywords=[];

$(function(){

	//点击修正
	$(".change-type").on("click",function(){
		var wHeight=$(window).height()+"px";
		
		$(".dialog").css("height",wHeight);
		$(".dialog").removeClass("hidden");
		
		//设置网页原类别
		var type=$(".page-type-td").text();
		$(".dialog .origin-type").text(type);
		
		$(".dialog input[type=radio]").prop("checked",false);
	});
	
	//dialog确定
	$(".dialog").on("click",".okBtn",function(){		
		var type=$(".dialog input[name=page_type]").filter(":checked").val();
		if(!type){
			type=$(".dialog .origin-type").text();
		}
		$(".page-type-td").text(type);
		if(type === "电商"){
			$("#textDeal").addClass(hiddenCLS);
			$("#keywords").addClass(hiddenCLS);
			$("#entity").removeClass(hiddenCLS);

			$(".textDeal-menu").addClass(hiddenCLS);
			$(".keywords-menu").addClass(hiddenCLS);
			$(".entity-menu").removeClass(hiddenCLS);
		}else{
			$("#textDeal").removeClass(hiddenCLS);
			$("#keywords").removeClass(hiddenCLS);
			$("#entity").addClass(hiddenCLS);

			$(".textDeal-menu").removeClass(hiddenCLS);
			$(".keywords-menu").removeClass(hiddenCLS);
			$(".entity-menu").addClass(hiddenCLS);
		}
		$(".dialog").addClass("hidden");
	});
	//dialog取消
	$(".dialog").on("click",".cancleBtn",function(){
		
		$(".dialog").addClass("hidden");
	});
	
	//输入URL后，点击分析
	$(".input-btn").on("click",function(){
		var datatosend={},
			requestURL="parser/judgeType";
			
		datatosend.url=$(".main-input input").val();
		$(".page-url a").attr("href",datatosend.url);
		$(".page-url a").text(datatosend.url);
		
		$.myAJAX(requestURL,datatosend,function(data){
			console.log(data);
			
			$(".page-type-td").text(data.result || "未知");

			if(data.result === "电商"){
				$("#textDeal").addClass(hiddenCLS);
				$("#keywords").addClass(hiddenCLS);
				$("#entity").removeClass(hiddenCLS);

				$(".textDeal-menu").addClass(hiddenCLS);
				$(".keywords-menu").addClass(hiddenCLS);
				$(".entity-menu").removeClass(hiddenCLS);
			}else{
				$("#textDeal").removeClass(hiddenCLS);
				$("#keywords").removeClass(hiddenCLS);
				$("#entity").addClass(hiddenCLS);

				$(".textDeal-menu").removeClass(hiddenCLS);
				$(".keywords-menu").removeClass(hiddenCLS);
				$(".entity-menu").addClass(hiddenCLS);
			}
		});
	});
	
	//修正完之后，点击分析
	$(".analysis-link").on("click",function(){
		var datatosend={},
			requestURL="feature/single",
			type=$(".page-type-td").text().trim();

		datatosend.type = type;
		datatosend.url=$(".main-input input").val();
		
		$.myAJAX(requestURL,datatosend,function(data){
			console.log(data);

			var titleSpan='<span style="color:#0098E1;font-size:15px;">title:&nbsp;&nbsp;</span>';
			var keywordsSpan='<span style="color:#0098E1;font-size:15px;">keywords:&nbsp;&nbsp;</span>';
			var desSpan='<span style="color:#0098E1;font-size:15px;">description:&nbsp;&nbsp;</span>';
			var contentSpan='<span style="color:#0098E1;font-size:15px;">content:&nbsp;&nbsp;</span>';

			$(".page-content-result").empty();
			if(data.result.type=="电商"){
				$(".page-content-result").append(data.result.title);
			}else{
				data.result.title && $(".page-content-result").append(titleSpan+data.result.title+"<br>");
				data.result.keywords && $(".page-content-result").append(keywordsSpan+data.result.keywords+"<br>");
				data.result.description && $(".page-content-result").append(desSpan+data.result.description+"<br>");
				data.result.content && $(".page-content-result").append(contentSpan+data.result.content);
			}

			if(data.result.type==="主题型"){
				$(".page-content-tip").text("新闻类——主题型");
			}else if(data.result.type==="非主题型"){
				$(".page-content-tip").text("新闻类——非主题型");
			}else{
				$(".page-content-tip").text(data.result.type);
			}

			if(data.result.type!="电商"){   //非电商类处理方法
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
			}else{      //电商类数据加载
				//加载实体识别结果
				var entityList=entityCom(data.result);
				$(".result-entity").empty().append(entityList);

				//可视化展示,力导向图
				var entityViewData = getEntityViewData(data.result);
				$("#visual-display-result").empty();
				entityDis(entityViewData);
			}
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

	//导入实体库
	$(".export-wrap .export-btn").on("click",function(){
		$(".export-upload").trigger("click");
	});

	$(".export-upload").on("change",function(){
		$("#myexport").ajaxSubmit({
			dataType:"json",
			success:function(responseData){
				$(".export-input").val(responseData.fileName);
			},
			error:function(xhr){
				console.log("error");
				console.log(xhr.responseText); //返回失败信息
			}
		})
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

	$(".analysis-link").trigger("click");
});
function defaultFun(){
	$(".analysis-link").trigger("click");
}

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
//实体识别
function entityCom(data){
	var entityList=data.title;

	for(var i=0;i<data.entityList.length;i++){
		var span='<span class="shop_name">ENTITY_NAME</span>';
		span=span.replace("ENTITY_NAME",data.entityList[i]);
		entityList=entityList.replace(data.entityList[i],span);
	}
	for(var j=0;j<data.brandList.length;j++){
		var brandSpan='<span class="brand">BRAND_NAME</span>';
		brandSpan=brandSpan.replace("BRAND_NAME",data.brandList[j]);
		entityList=entityList.replace(data.brandList[j],brandSpan);
	}
	return entityList;
}

function getEntityViewData(data){
	var entityNodes = [], entityLinks = [], result = {};
	entityNodes.push({
		category: 0,
		name: "商品名称",
		weight: 10
	});
	entityNodes.push({
		category: 1,
		name: "商品品牌",
		weight: 10
	});
	for(var i=0;i<data.entityList.length;i++){
		var obj = {
			category: 0,
			name: data.entityList[i],
			weight: 5
		};

		var goodLink = {
			source: data.entityList[i],
			target: "商品名称",
			weight: 2
		};
		entityNodes.push(obj);
		entityLinks.push(goodLink);
	}
	for(var j=0;j<data.brandList.length;j++){
		var brandObj = {
			category: 1,
			name: data.brandList[j],
			weight: 5
		};
		var brandLink = {
			source: data.brandList[j],
			target: "商品品牌",
			weight: 2
		};
		entityNodes.push(brandObj);
		entityLinks.push(brandLink);
	}
	result ={
		entityNodes: entityNodes,
		entityLinks: entityLinks
	};
	console.log(result);
	return result;
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

function entityDis(entityViewData){
	require.config({
		paths: {
			echarts:"scripts/dist"
		}
	});
	require(
		[
			'echarts',
			// 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
			'echarts/chart/force',
			'echarts/chart/chord'
		],

		function(ec) {
			var myChart = ec.init(document.getElementById("visual-display-result"));

			var option = {
				title : {
					text: '',
				},
				tooltip : {
					trigger: 'item',
					formatter: '{a} : {b}'
				},
				toolbox: {
					show : true,
					feature : {
						restore : {show: true},
						magicType: {show: true, type: ['force', 'chord']},
						saveAsImage : {show: true}
					}
				},
				series : [
					{
						type:'force',
						name : "命名实体",
						ribbonType: false,
						categories : [
							{
								name: '商品名称'
							},
							{
								name: '商品品牌'
							}
						],
						itemStyle: {
							normal: {
								label: {
									show: true,
									textStyle: {
										color: '#333'
									}
								},
								nodeStyle : {
									brushType : 'both',
									borderColor : 'rgba(255,215,0,0.4)',
									borderWidth : 1
								},
								linkStyle: {
									type: 'curve'
								}
							},
							emphasis: {
								label: {
									show: false
									// textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
								},
								nodeStyle : {
									//r: 30
								},
								linkStyle : {}
							}
						},
						useWorker: false,
						minRadius : 15,
						maxRadius : 25,
						gravity: 1.1,
						scaling: 1.1,
						roam: 'move',
						nodes: entityViewData.entityNodes,
						links: entityViewData.entityLinks
					}
				]
			};
			myChart.setOption(option);
		}
	);
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
			'echarts/chart/wordCloud',
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
