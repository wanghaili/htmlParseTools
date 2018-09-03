/**
 * Created by renshuoxin on 16/8/27.
 */
$(function () {
    
    $(".data-upload-btn").on("click",function(){
        console.log("123");
       $(".data-upload").trigger("click");
    });

    $(".data-upload").on("change",function(){
        $("#myupload").ajaxSubmit({
            dataType:"json",
            success:function(responseData){
                $(".data-input").val(responseData.result);
            },
            error:function(xhr){
                console.log("error");
                console.log(xhr.responseText); //返回失败信息
            }
        })
    });
});