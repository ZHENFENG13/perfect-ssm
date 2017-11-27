<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>articleManger</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/kindeditor-4.1.10/themes/default/default.css"/>
    <script charset="utf-8" src="${pageContext.request.contextPath}/kindeditor-4.1.10/kindeditor-all.js"></script>
    <script charset="utf-8" src="${pageContext.request.contextPath}/kindeditor-4.1.10/lang/zh_CN.js"></script>

</head>
<body style="margin:1px;" id="ff">
<table id="dg" title="文本信息管理" class="easyui-datagrid" pagination="true"
       rownumbers="true" fit="true"
       url="${pageContext.request.contextPath}/articles/datagrid" toolbar="#tb">
    <thead data-options="frozen:true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="10%" align="center" hidden="true">编号</th>
        <th field="articleTitle" width="200" align="center">标题</th>
        <th field="articleCreateDate" width="150" align="center">创建时间</th>
        <th field="addName" width="150" align="center">添加人</th>
        <th field="content" width="70" align="center"
            formatter="formatHref">操作
        </th>
    </tr>
    </thead>
</table>

<div id="tb">
    <div>
        <a href="javascript:openArticleAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加</a> <a
            href="javascript:openArticleModifyDialog()"
            class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> <a
            href="javascript:deleteArticle()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;标题：&nbsp;<input type="text" id="articleTitle" size="20"
                              onkeydown="if(event.keyCode==13) searchArticle()"/>&nbsp; <a
            href="javascript:searchArticle()" class="easyui-linkbutton"
            iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 850px;height:555px;padding: 10px 20px; position: relative; z-index:1000;"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>标题：</td>
                <td><input type="text" id="title" name="articleTitle"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                    <input id="articleIdfm" name="id" type="hidden" value="0">
                </td>
            </tr>
            <tr>
                <td>添加人：</td>
                <td><input type="text" id="addName" name="addName"/>
                </td>
            </tr>
            <tr>
                <td>详细内容</td>
                <td>
                    <textarea id="editor"
                              style="width:800px;height:400px;visibility:hidden;"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveArticle()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeArticleDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>


<script type="text/javascript">
    var url = "${pageContext.request.contextPath}/articles";
    var method;

    $(function () {
        //详情编辑器
        KindEditor.ready(function (K) {
            this.editor
                = K.create('textarea[id="editor"]', {
                items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
                    'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                    'anchor', 'link', 'unlink'],
                uploadJson: '/images',
                filePostName: 'file',
                fileManagerJson: '/images',
                allowFileManager: true
            });
        });
    });

    function searchArticle() {
        $("#dg").datagrid('load', {
            "articleTitle": $("#articleTitle").val(),
        });
    }

    function deleteArticle() {
        var selectedRows = $("#dg").datagrid('getSelections');
        if (selectedRows.length == 0) {
            $.messager.alert("系统提示", "请选择要删除的数据！");
            return;
        }
        var strIds = [];
        for (var i = 0; i < selectedRows.length; i++) {
            strIds.push(selectedRows[i].id);
        }
        var ids = strIds.join(",");
        $.messager
            .confirm(
                "系统提示",
                "您确认要删除这<font color=red>" + selectedRows.length
                + "</font>条数据吗？",
                function (r) {
                    if (r) {
                        $.ajax({
                            type: "DELETE",//方法类型
                            dataType: "json",//预期服务器返回的数据类型
                            url: "/articles/" + ids,//url
                            data: {},
                            success: function (result) {
                                console.log(result);//打印服务端返回的数据
                                if (result.resultCode == 200) {
                                    $.messager.alert(
                                        "系统提示",
                                        "数据已成功删除！");
                                    $("#dg").datagrid(
                                        "reload");
                                }
                                else {
                                    $.messager.alert(
                                        "系统提示",
                                        "数据删除失败！");
                                }

                                ;
                            },
                            error: function () {
                                $.messager.alert("ERROR！");
                            }
                        });
                    }
                });

    }

    function openArticleAddDialog() {
        editor.html('请输入文章内容');
        $("#dlg").dialog("open").dialog("setTitle", "添加文本信息");
        method = "POST";
    }

    function saveArticle() {
        var title = $("#title").val();
        var addName = $("#addName").val();
        var content = editor.html();
        var id = $("#articleIdfm").val();
        var data = {"id": id, "articleTitle": title, "articleContent": content, "addName": addName}
        $.ajax({
            type: method,//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: url,//url
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);//打印服务端返回的数据
                if (result.resultCode == 200) {
                    $.messager.alert("系统提示", "保存成功");
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                    resetValue();
                }
                else {
                    $.messager.alert("系统提示", "操作失败");
                    $("#dlg").dialog("close");
                    resetValue();
                }
                ;
            },
            error: function () {
                $.messager.alert("系统提示", "操作失败");
            }
        });
    }

    function openArticleModifyDialog() {
        var selectedRows = $("#dg").datagrid('getSelections');
        if (selectedRows.length != 1) {
            $.messager.alert("系统提示", "请选择一条要编辑的数据！");
            return;
        }
        var row = selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle", "修改信息");
        $('#fm').form('load', row);
        editor.html(row.articleContent);
        method = "PUT";
        $("#articleIdfm").val(row.id);
    }

    function formatHref(val, row) {
        return "<a href='${pageContext.request.contextPath}/article.html?id=" + row.id + "' target='_blank'>查看详情</a>";
    }

    function resetValue() {
        $("#title").val("");
        $("#addName").val("");
        $("#container").val("");
        editor.html();
    }

    function closeArticleDialog() {
        $("#dlg").dialog("close");
        resetValue();
    }
</script>
</body>
</html>