<%
    ui.decorateWith("kenyaemr", "standardPage", [ patient: currentPatient ])
    ui.includeCss("ehrconfigs", "referenceapplication.css")
%>
<div class="ke-page-content">
<% if(qualified) {%>
    ${ ui.includeFragment("laboratoryapp", "foodHandlerPrintOut", [patient: currentPatient]) }
<%} else {%>
    <div>
        <p>
            <i>This patient has NOT undertaken food handling test. Please make sure such tests are configured and ordered for a patient by the doctor.</i>
        </p>
    </div>
<%}%>
</div>