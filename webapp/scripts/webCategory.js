/**
 * Created by renshuoxin on 16/8/27.
 */
var fileID;
$(function () {
    var dataSelect = $("#data-source");
    getDataSource();

    $(".data-source-btn").on("click",function(){
        fileID = dataSelect.val();
        console.log(fileID);
        getUrlList(fileID);
    });

    $(".data-source-btn").trigger("click");
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

function getUrlList(id){
    var datatosend={
        fileId: id,
        pageSize: 10,
        pageNum: 0
    };
    var requestURL="parser/getByFileId";
    var tbody = $(".urlListTbody");

    var operator = '<td><a href="javascript:void(0)" title="修正网页" class="change-type">修正</a></td>';
    $.myAJAX(requestURL,datatosend,function(data){
        console.log(data);


        tbody.empty();
        for(var i=0;i<10;i++){
            var tr = '<tr><td><a href="URL" target="_blank" title="点击这里">URLCONTENT</a></td>'+
                '<td>CATEGORY</td>OPERATOR</tr>';
            var type = data.result[i].type;
            if(data.result[i].type === "主题型" || data.result[i].type === "非主题型"){
                type = "新闻类";
            }
            tr=tr.replace("URL",data.result[i].url).replace("URLCONTENT",data.result[i].url).replace("CATEGORY",type).replace("OPERATOR",operator);
            tbody.append(tr);
        }
    });
}