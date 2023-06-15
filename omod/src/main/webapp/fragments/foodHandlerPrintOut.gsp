<script type="text/javascript">
    jq = jQuery
    jq(document).ready(function() {

    });

    function printFoodHandler() {
        var printDiv = jq("#food-handle-print-div").html();
        var printWindow = window.open('', '', 'height=400,width=800');
        printWindow.document.write('<html><head><title>Food Handler Report</title>');
        printWindow.document.write(printDiv);
        printWindow.document.write('</body></html>');
        printWindow.document.close();
        printWindow.print();
    }
</script>
<style>
    body {
        width: 100%;
        height: 100%;
        margin: 0;
        padding: 0;
        background-color: #FAFAFA;
        font: 12pt "Tahoma";
    }
    * {
        box-sizing: border-box;
        -moz-box-sizing: border-box;
    }
    .page {
        width: 210mm;
        min-height: 297mm;
        padding: 20mm;
        margin: 10mm auto;
        border: 1px #D3D3D3 solid;
        border-radius: 5px;
        background: white;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    }
    .subpage {
        padding: 1cm;
        border: 5px red solid;
        height: 257mm;
        outline: 2cm #FFEAEA solid;
    }

    @page {
        size: A4;
        margin: 0;
    }
    @media print {
        html, body {
            width: 210mm;
            height: 297mm;
        }
        .page {
            margin: 0;
            border: initial;
            border-radius: initial;
            width: initial;
            min-height: initial;
            box-shadow: initial;
            background: initial;
            page-break-after: always;
        }
    }
</style>
<div>
        <div class="page">
            <div class="subpage" id="food-handle-print-div">
               <div style="text-align: center;" id="header">
                   <center>
                       <img src="/openmrs/ms/uiframework/resource/ehrinventoryapp/images/kenya_logo.bmp" width="60" height="60" align="middle">
                   </center>
                   ${ui.includeFragment("patientdashboardapp", "printHeader")}
               </div>
               <div>
                   <center>
                        <h3>Patient SickOff Sheet</h3>
                        <hr />
                   </center>
               </div>
               <div id="biodata">
                    <h4>PATIENT BIO DATA</h4>

                    <label>
                        <span class='status active'></span>
                        Identifier:
                    </label>
                    <span>${currentPatient.getPatientIdentifier()}</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Full Names:
                    </label>
                    <span>${names}</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Age:
                    </label>
                    <span>${currentPatient.age} (${ui.formatDatePretty(currentPatient.birthdate)})</span>
                    <br/>

                    <label>
                        <span class='status active'></span>
                        Gender:
                    </label>
                    <span>${currentPatient.gender}</span>
               </div>
               <div id="foodHandlerPrintInfo">
                    <table id="testsDoneTbl">
                        <thead>
                            <th>Test Performed</th>
                            <th>Results</th>
                            <th>Date Performed</th>
                            <th>Notes</th>
                        </thead>
                        <tbody>
                        <% testsDone.each { %>
                            <tr>
                                <td>${it.testName}</td>
                                <td>${it.results}</td>
                                <td>${it.datePerformed}</td>
                                <td>${it.description}</td>
                            </tr>
                        <%}%>
                        </tbody>
                        <tbody>
                            <tr>
                                <th colspan="2">Printed By</th>
                                <th colspan="2">Date Printed</th>
                            </tr>
                            <tr>
                                <td colspan="2">${user}</td>
                                <td colspan="2">${today}</td>
                            </tr>
                        </tbody>
                    <table>
               <div>
            </div>
        </div>
    </div>
    <div class="onerow" style="margin-top:10px;">
        <button id="printFoodHandlerBtn"  onclick="printFoodHandler()" class="button confirm right">
            <i class="btn-info"></i>
            Print Certificate
        </button>
    </div>