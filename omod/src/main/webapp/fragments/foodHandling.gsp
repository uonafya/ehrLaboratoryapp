<script type="text/javascript">
    jq(document).ready(function () {
     var jq = jQuery;
     jq('#foodHandlingTable').DataTable();
     });
</script>
<table id="foodHandlingTable">
    <thead>
    <tr>
        <th>Name</th>
        <th>Concept Reference</th>
        <th>Description</th>
        <th>Date created</th>
        <th>Created By</th>
    </tr>
    </thead>
    <tbody>
        <% list.each { %>
            <tr>
                <td>${it.testName}</td>
                <td>${it.conceptReference}</td>
                <td>${it.description}</td>
                <td>${it.dateCreated}</td>
                <td>${it.creator}</td>
            </tr>
        <%}%>
    </tbody>
</table>