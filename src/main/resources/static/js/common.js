// show loading
function g_showLoading(){
	var idx = layer.msg('processing...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px', time:100000}) ;
	return idx;
}
//salt
var g_passsword_salt="1a2b3c4d"
// get url param
function g_getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
};
//datetime formatting new Date().format("yyyyMMddhhmmss");
Date.prototype.format = function (format) {  
    var args = {  
        "M+": this.getMonth() + 1,  
        "d+": this.getDate(),  
        "h+": this.getHours(),  
        "m+": this.getMinutes(),  
        "s+": this.getSeconds(),  
    };  
    if (/(y+)/.test(format))  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var i in args) {  
        var n = args[i];  
        if (new RegExp("(" + i + ")").test(format))  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));  
    }  
    return format;  
};

function aaa(){

    // login input
    $('.form_text_ipt input').focus(function(){
        $(this).parent().css({
            'box-shadow':'0 0 3px #bbb',
        });
    });
    $('.form_text_ipt input').blur(function(){
        $(this).parent().css({
            'box-shadow':'none',
        });
        //$(this).parent().next().hide();
    });

    // form validate
    $('.form_text_ipt input').bind('input propertychange',function(){
        if($(this).val()==""){
            $(this).css({
                'color':'red',
            });
            $(this).parent().css({
                'border':'solid 1px red',
            });
            //$(this).parent().next().find('span').html('helow');
            $(this).parent().next().show();
        }else{
            $(this).css({
                'color':'#ccc',
            });
            $(this).parent().css({
                'border':'solid 1px #ccc',
            });
            $(this).parent().next().hide();
        }
    });
};
