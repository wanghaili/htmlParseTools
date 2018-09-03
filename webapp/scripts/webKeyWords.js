/**
 * Created by renshuoxin on 16/8/31.
 */
var fileID;
$(function(){
    var dataSelect = $("#data-source");
    getDataSource();

    $(".keyWords-view-btn").on("click",function(){
        var pageType = $("#page-type").val();
        var type = "主题型";
        if(pageType == 2){
            type = "视频";
        }else if(pageType == 3){
            type = "电商";
        }
        $(".data-source-btn").trigger("click");
        var viewData = getData(fileID,type);

        visualDis(viewData, pageType);
    });

    $(".data-source-btn").on("click",function(){
        fileID = dataSelect.val();
    });

    $(".keyWords-view-btn").trigger("click");
});

function getDataSource(){
    var datatosend={
            status: 1
        },
        requestURL="userParser/selectMyFile";

    var dataSelect = $("#data-source");
    $.myAJAX(requestURL,datatosend,function(data){
        console.log(data);

        var dataSource = "";
        dataSelect.empty();
        $.each(data.result,function(idx,item){
            var temp;
            if(idx === data.result.length-1) {
                temp = '<option value="ID" selected>FILENAME</option>';
            }else{
                temp ='<option value="ID">FILENAME</option>';
            }
            temp=temp.replace("FILENAME",item.fileName).replace("ID",item.id);
            dataSource=dataSource+temp;
        });
        dataSelect.append(dataSource);
    });
}

function getData(dataSource,type){
    var datatosend = {
        fileId: dataSource,
        type: type
    };
    var requestURL = "userParser/countTop10";
    var result = [];
    $.myAJAX(requestURL, datatosend, function(data){
        console.log(data);
        result = data.result || [];
    });
    return result;
}

function visualDis(data, pageType){
    var text = "";
    if(pageType == 1){
        text = "新闻类网页主题标签TOP10";
    }else if(pageType == 2){
        text = "视频类网页主题标签TOP10";
    }else if(pageType == 3){
        text = "电商类网页主题标签TOP10";
    }
    var keywordsArr = [],weightArr = [];

    for(var i=0;i<data.length;i++){
        keywordsArr.push(data[i].keyword);
        weightArr.push(data[i].total_num);
    }
    require.config({
        paths: {
            echarts:"scripts/dist"
        }
    });
    require(
        [
            'echarts',
            // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
            'echarts/chart/bar'
        ],

        function(ec) {
            var myChart = ec.init(document.getElementById("view_result"));

            var option = {
                title : {
                    text: text,
                },
                tooltip : {
                    trigger: 'axis'
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'value',
                        boundaryGap : [0, 0.01]
                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        data : keywordsArr
                    }
                ],
                series : [
                    {
                        type:'bar',
                        data:weightArr
                    }
                ]
            };
            myChart.setOption(option);
        }
    );
}