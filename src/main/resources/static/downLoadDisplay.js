

var $upLoad = $("#upload");
var $downLoad = $("#downLoad");
console.log($upLoad)
console.log($downLoad)
$upLoad.on('click', function () {
    // $.ajax({
    //     url:"/upLoadExcel",
    //     success:function(data){
    //         alert(1)
    //     }
    // });
    $downLoad.css("display","block");
    alert("已生成考勤文件，可以下载")

})
