
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
                        <h3>Food Handling Certificate</h3>
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