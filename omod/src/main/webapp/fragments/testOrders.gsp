<%
    def props = ["wrapperIdentifier", "names", "age", "gender", "formartedVisitDate", "action"]
%>
<script>

    // get queue
    var searchResult = [];

    function getPatientQueue(currentPage) {
        this.currentPage = currentPage;
        var phrase = jQuery("#searchPhrase").val();

        jQuery.ajax({
            type: "GET",
            url: "${ui.actionLink('ehrcashier','searchPatient','searchSystemPatient')}",
            dataType: "json",
            data: ({
                gender: "Any",
                phrase: phrase,
                currentPage: this.currentPage,
                pageSize: 1000
            }),
            success: function (data) {
                jQuery("#ajaxLoader").hide();
                pData = data;
                updateSystemQueueTable(data);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                jq().toastmessage('showNoticeToast', "No linked record found");
                jQuery("#ajaxLoader").hide();
            }
        });
    }


    //update the queue table
    function updateSystemQueueTable(data) {
        searchResult = data;
        var jq = jQuery;
        jq('#patient-search-results-table > tbody > tr').remove();
        var tbody = jq('#patient-search-results-table > tbody');
        for (index in data) {
            var item = data[index];
            var row = '<tr>';
            <% props.each {
               if(it == props.last()){
                  def pageLinkRevisit = ui.pageLink("ehrcashier", "billingQueue");
                   %>
            row += '<td> ' +
                '<a title="Previous Tests" onclick="ADVSEARCH.previousLabTests(' + item.patientId + ');"><i class="icon-arrow-right small" ></i></a>' +
                '</td>';
            <% } else {%>
            row += '<td>' + item.${ it} + '</td>';
            row = strReplace(row);
            <% }
               } %>
            row += '</tr>';
            tbody.append(row);
        }
        if (jq('#patient-search-results-table tr').length <= 1) {
            tbody.append('<tr align="center"><td colspan="6">No patients found</td></tr>');
        }
    }

    function strReplace(word) {
        var res = word.replace("[", "");
        res = res.replace("]", "");
        return res;
    }

    ADVSEARCH = {
        timeoutId: 0,
        showing: false,
        params: "",
        delayDuration: 1,
        pageSize: 10,
        beforeSearch: function () {
        },
        // search patient
        searchPatient: function (currentPage, pageSize) {
            this.beforeSearch();
            var phrase = jQuery("#searchPhrase").val();
            if (phrase.length >= 1) {
                jQuery("#ajaxLoader").show();
                getPatientQueue(1);
            } else {
                jq().toastmessage('showNoticeToast', "Specify atleast one character to Search");
            }
        },
        // start searching patient
        startSearch: function (e) {
            e = e || window.event;
            ch = e.which || e.keyCode;
            if (ch != null) {
                if ((ch >= 48 && ch <= 57) || (ch >= 96 && ch <= 105)
                    || (ch >= 65 && ch <= 90)
                    || (ch == 109 || ch == 189 || ch == 45) || (ch == 8)
                    || (ch == 46)) {
                } else if (ch == 13) {
                    clearTimeout(this.timeoutId);
                    this.timeoutId = setTimeout("ADVSEARCH.delay()",
                        this.delayDuration);
                }
            }
        },
        // delay before search
        delay: function () {
            this.searchPatient(0, this.pageSize);
        },
        previousLabTests: function (patientId) {
            window.location.href = ui.pageLink("ehrcashier", "billableServiceBillListForBD", {
                "patientId": patientId
            });
        }
    };
</script>


<div>
    <form onsubmit="return false" id="patientSystemSearchForm" method="get">
        <input autocomplete="off" placeholder="Search by Patient ID,Name or Bill Id" id="searchPhrase"
               style="float:left; width:50%; padding:6px 10px -1px;" onkeyup="ADVSEARCH.startSearch(event);">
        <img id="ajaxLoader" style="display:none; float:left; margin: 3px -4%;"
             src="${ui.resourceLink("ehrcashier", "images/ajax-loader.gif")}"/>
    </form>

    <div id="patient-search-results" style="display: block; margin-top:3px;">
        <div role="grid" class="dataTables_wrapper" id="patient-search-results-table_wrapper">
            <table id="patient-search-results-table" class="dataTable"
                   aria-describedby="patient-search-results-table_info">
                <thead>
                <tr role="row">
                    <th class="ui-state-default" role="columnheader" style="width: 220px;">
                        <div class="DataTables_sort_wrapper">Identifier<span class="DataTables_sort_icon"></span>
                        </div>
                    </th>

                    <th class="ui-state-default" role="columnheader" width="*">
                        <div class="DataTables_sort_wrapper">Name<span class="DataTables_sort_icon"></span></div>
                    </th>

                    <th class="ui-state-default" role="columnheader" style="width: 60px;">
                        <div class="DataTables_sort_wrapper">Age<span class="DataTables_sort_icon"></span></div>
                    </th>

                    <th class="ui-state-default" role="columnheader" style="width: 60px;">
                        <div class="DataTables_sort_wrapper">Gender<span class="DataTables_sort_icon"></span></div>
                    </th>

                    <th class="ui-state-default" role="columnheader" style="width: 100px;">
                        <div class="DataTables_sort_wrapper">Last Visit<span class="DataTables_sort_icon"></span></div>
                    </th>

                    <th class="ui-state-default" role="columnheader" style="width: 60px;">
                        <div class="DataTables_sort_wrapper">Action<span class="DataTables_sort_icon"></span></div>
                    </th>
                </tr>
                </thead>
                <tbody role="alert" aria-live="polite" aria-relevant="all">
                    <tr align="center">
                        <td colspan="6">No patients found</td>
                    </tr>
                </tbody>
            </table>

        </div>
    </div>

</div>