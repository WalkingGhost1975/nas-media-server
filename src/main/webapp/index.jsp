<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administration Page</title>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
        <script type="text/javascript">
            function postTags()  {
                var form = document.forms['tagsForm'];
                var resource = form['resource'].value;
                var tags = form['tags'].value;
                $.ajax({
                    url : resource,
                    contentType : 'application/json',
                    dataType : 'json',
                    processData : false,
                    type : 'POST',
                    data : tags
                });
            }
        </script>
    </head>
    <body>
        <h1>Media Repository Administration</h1>
        <form action="<%= request.getContextPath() %>/repo/images/scan" method="post">
            <input type="submit" name="submit" value="ImageRepository scannen" />
        </form>
        <form action="<%= request.getContextPath() %>/repo/audio/scan" method="post">
            <input type="submit" name="submit" value="AudioRepository scannen" />
        </form>
        <form name="tagsForm">
            <fieldset>
                <div><input type="text" id="resource" name="resource" /></div>
                <div><input type="text" id="tags" name="tags" /></div>
                <div><input type="submit" name="submit" value="Tags aktualisieren" onclick="postTags(); return false;" /></div>
            </fieldset>
        </form>
    </body>
</html>
