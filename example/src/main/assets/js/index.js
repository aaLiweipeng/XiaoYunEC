$(function () {
    $('.share').click(function () {
        var json = {
            action: "share",
            params: {
                imageUrl:"http://47.100.78.251/img/headimg/zhudi.jpg",
                url:"http://47.100.78.251/cz/",
                title:"XiaoYunEC",
                text:"来自XiaoYunEC的分享的内容"
            }
        };

        xiaoyun.event(JSON.stringify(json));

    });

    $('.comment').click(function () {

            var json = {
                action: "comment"
            };

            xiaoyun.event(JSON.stringify(json));

        });
});