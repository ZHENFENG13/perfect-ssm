function login() {
    var userName = $("#userName").val();
    var password = $("#password").val();
    var roleName = $("#roleName").val();
    if (userName == null || userName == "") {
        alert("用户名不能为空！");
        return;
    }
    if (password == null || password == "") {
        alert("密码不能为空！");
        return;
    }
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/users/cookie",
        data: $('#adminlogin').serialize(),
        success: function (result) {
            console.log(typeof(result));
            alert(1);
            if (result.resultCode == 200) {
                alert(1);
                setCookie("userName", result.data.currentUser.userName);
                setCookie("roleName", result.data.currentUser.roleName);
                window.location.href = "main.jsp";
            }
            ;

        },
        error: function () {
            alert("异常！");
        }
    });

}