<script type="text/javascript">
    var dTbl;
    var billableServices;
    var serviceIds = [];
    jQuery(document).ready(function() {
        jq('#functionalStatus').on('change', 'input.service-status', function() {
            var index = jq.inArray(jq(this).val(), serviceIds);
            if (index > -1) {
                serviceIds.splice(index, 1);
            } else {
                serviceIds.push(jq(this).val());
            }
        });

        dTbl=jq('#functionalStatus').DataTable({
            searching: true,
            lengthChange: false,
            pageLength: 15,
            jQueryUI: true,
            pagingType: 'full_numbers',
            sort: false,
            dom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
            language: {
                zeroRecords: 'No Services Found',
                paginate: {
                    first: 'First',
                    previous: 'Previous',
                    next: 'Next',
                    last: 'Last'
                }
            }
        });

        // getBillableServices();
        jq.ajax({
            type: "GET",
            url: "${ui.actionLink('laboratoryapp','functionalStatus','getBillableServices')}",
            dataType: "json",
            success: function (data) {
                billableServices = data

                var dataRows = [];

                _.each(billableServices, function(billableService, indx) {
                    var isChecked = (billableService.disable === true) ?"checked=checked":"";
                    dataRows.push(
                        [
                            indx+1,
                            billableService.name,
                            '<input type="checkbox" class="service-status" '+ isChecked + '" value="'+ billableService.serviceId +'">'
                        ]
                    )
                });

                dTbl.rows.add(dataRows).draw();
                // dTbl.draw();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr);
                jQuery("#ajaxLoader").hide();
            }
        });

        dTbl.on( 'order.dt search.dt', function () {
            dTbl.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
            } );
        } );

        jq('#filter-status').on('keyup',function(){
            var searchPhrase = jq(this).val();
            dTbl.search(searchPhrase).draw();
        });
        // getBillableServices();

        jQuery('#functionalStatus tbody').on("click", function(){
            jq('#submitSave').on("click", function(){

                jq.post('${ui.actionLink('laboratoryapp','functionalStatus','updateBillableServices')}',
                        { "serviceIds" : serviceIds.toString() },
                        function (data) {
                            if (data.status === "fail") {
                                jq().toastmessage('showErrorToast', data.error);
                            } else {
                                jq().toastmessage('showSuccessToast', "saved");

                            }
                        },
                        'json'
                );
            });
        });


    });


</script>

<style>	
	.paging_full_numbers .fg-button {
		margin: 1px;
	}
	.paging_full_numbers {
		width: 62% !important;
	}	
	.dataTables_info {
		float: left;
		width: 35%;
	}
</style>


<fieldset style="margin: 0 0 5px">		
	<form style="padding-left: 10px;">
		<label for="filter-status">Filter Functional Status</label>			
		<input type="text" id="filter-status" placeholder="Filter Status" style="width: 94.6%; padding-left: 30px;"/>
		<i class="icon-search small" style="color: rgb(242, 101, 34); float: right; position: relative; margin-top: -32px; margin-right: 96.7%;"></i>
	</form>
</fieldset>


<table id='functionalStatus' style="width: 100%;">
    <thead>
    <tr>
        <th style="width: 10px!important;">#</th>
        <th>STATUS</th>
        <th>DISABLED</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

    <input type='hidden' id='serviceIds' name='serviceIds' value=''/>
    <input type='submit' id='submitSave' value='Save'/>
