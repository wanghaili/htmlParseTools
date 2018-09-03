/**
 * Created by renshuoxin on 16/8/31.
 */
var fileID;
$(function(){
    var dataSelect = $("#data-source");
    getDataSource();

    $(".data-source-btn").on("click",function(){
        fileID = dataSelect.val();
    });

    $(".data-source-btn").trigger("click");

    var pageNumData = getPageNum();
    getViewData(pageNumData);
});

function getViewData(pageNumData){
    var num = [],type = [];
    var newsPageNum = 0;
    for(var i=0;i<pageNumData.length;i++){
        if(pageNumData[i].type == "主题型" || pageNumData[i].type == "非主题型"){
            newsPageNum = newsPageNum + pageNumData[i].total_num;
        }else{
            num.push(pageNumData[i].total_num);
            type.push(pageNumData[i].type);
        }
    }
    num.push(newsPageNum);
    type.push("新闻");

    firstViewDis(num, type);
}
function getPageNum(){
    var datatosend = {
        fileId: fileID
    };
    var requestURL = "userParser/countNumByCategory";
    var result = {};
    $.myAJAX(requestURL, datatosend, function(data){
        console.log(data);

        result = data.result || {};
    });

    return result;
}

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

function firstViewDis(num, type){
    require.config({
        paths: {
            echarts:"scripts/dist"
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/bar'
        ],

        function(ec) {
            var myChart = ec.init(document.getElementById("first-view"));

            var option = {
                title : {
                    text: ''
                },
                tooltip : {
                    trigger: 'axis'
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : type
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'数量',
                        type:'bar',
                        data: num,
                        markPoint : {
                            data : [
                                {type : 'max', name: '最大值'},
                                {type : 'min', name: '最小值'}
                            ]
                        }
                    }
                ]
            };
            myChart.setOption(option);
        }
    );
}