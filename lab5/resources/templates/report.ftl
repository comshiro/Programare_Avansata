<!DOCTYPE html>
<html>
<head>
    <title>Image Repository Report</title>
</head>
<body>
<h1>Image Repository Report</h1>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Path</th>
    </tr>
    <#list images as image>
        <tr>
            <td>${image.name()}</td>
            <td>${image.date()}</td>
            <td>${image.path()}</td> <!-- Correctly calling the method if path() is a method -->
        </tr>
    </#list>
</table>
</body>
</html>
