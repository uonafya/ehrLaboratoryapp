<%
    ui.decorateWith("kenyaemr", "standardPage", [ patient: currentPatient ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>
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
<div class="ke-page-content">
<% if(testPaid) {%>
    <div>
        <div class="page">
            <div class="subpage" id="food-handle-print-div">
               <div style="text-align: center;" id="header">
                   ${ui.includeFragment("patientdashboardapp", "printHeader", [currentPatient: currentPatient])}
               </div>
               <div>
                    <center>
                        <h3>Food Handling Certificate</h3>
                    </center>
               </div>
               <hr />
               <div>
                    <h4>Receipt No. ${generatedReceiptNumber}</h4>
               </div>
               <div id="foodHandlerPrintInfo">
                <table>
                    <tr>
                        <td>Medical Examination - Food handlers</td>
                        <td>${costOfTest}</td>
                    </tr>
                    <tr>
                        <td>Total</td>
                        <td>${costOfTest}</td>
                    </tr>
                </table>
               <div>
               <div>
                    <p>Served by: ${user}</p>
               </div>
               <div>
                    <p>Payments Summary for Receipt No.${receiptNumber}, Invoice(s): [${comments}]</p>
               </div>
               <div>
                    ${description}
               </div>
            </div>
            <div style="position:fixed; bottom:0;">
                SerialNumber ${receiptSerialNumber}
            </div>
        </div>
        </div>
        <div class="onerow" style="margin-top:10px;">
            <button id="printFoodHandlerBtn"  onclick="printFoodHandler()" class="button confirm right">
                <i class="btn-info"></i>
                Print Certificate
            </button>
        </div>
<%} else {%>
    <div>
        <p>
            <i>This patient has NOT undertaken food handling test. Please make sure such tests are configured and ordered for a patient by the doctor.</i>
        </p>
    </div>
<%}%>
</div>