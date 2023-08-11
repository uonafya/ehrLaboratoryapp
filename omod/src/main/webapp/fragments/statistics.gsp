<script type="text/javascript">
    jQuery(function(){
       populateLabAmount();
       jq("#filter").click(function () {
       populateLabAmount();
       });
    });
    function populateLabAmount(){
        const summaryFromDate = jq('#summaryFromDate-field').val(),
            summaryToDate = jq('#summaryToDate-field').val();
        jq.getJSON('${ui.actionLink("laboratoryapp", "Statistics", "getLaboratoryTotalOnDateRange")}',
            {
                "fromDate" : summaryFromDate,
                "toDate" : summaryToDate,
            }
        ).success(function (data) {
            jq('.stat-digit').eq(0).html(data.pendingtests)
            jq('.stat-digit').eq(1).html(data.totaltestdone)
            jq('.stat-digit').eq(2).html(data.laboratory)


        })
    }
</script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" />
<style>
.card-counter {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    box-shadow: 2px 2px 10px #DADADA;
    margin: 5px;
    padding: 40px 10px;
    background-color: #fff;
    position: relative;
    border-radius: 5px;
    width: calc(33.33% - 10px);
    transition: .3s linear all;
}

.card-counter:hover {
    box-shadow: 4px 4px 20px #DADADA;
    transition: .3s linear all;
}

.card-counter.danger {
    background-color: #EEDC5B;
    color: black;
}

.card-counter.pham {
    background-color: #a7f432;
    color: black;
}

.card-counter.success {
    background-color: #A9FF96;
    color: black;
}

.card-counter i {
    font-size: 3.5em;
    opacity: 0.2;
    margin-right: 10px;
}

.card-counter .count-numbers {
    font-size: 20px;
    font-weight: bold;
    position: absolute;
    top: 10px; /* Adjust the top distance */
    right: 10px; /* Adjust the right distance */
}

.card-counter .count-name {
    font-style: italic;
    text-transform: uppercase;
    opacity: 0.5;
    font-size: 15px;
    position: absolute;
    bottom: 10px; /* Adjust the bottom distance */
    right: 10px; /* Adjust the right distance */
    padding: 5px;
}
</style>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Laboratory Statistics</div>
    <div class="ke-panel-content">
        <br />
        <div class="row">
            <div class="col-12">
                <div style="margin-top: -1px " class="onerow">
                    <i class="icon-filter" style="font-size: 26px!important; color: #5b57a6"></i>
                    <label>&nbsp;&nbsp;From&nbsp;</label>${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'fromDate', id: 'summaryFromDate', label: '', useTime: false, defaultToday: false, class: ['newdtp']])}
                    <label>&nbsp;&nbsp;To&nbsp;</label>${ui.includeFragment("uicommons", "field/datetimepicker", [formFieldName: 'toDate', id: 'summaryToDate', label: '', useTime: false, defaultToday: false, class: ['newdtp']])}
                    <button id="filter" type="button" class="btn btn-primary right">${ui.message("Filter")}</button>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr />
            </div>
        </div>
        <div class="row">
            <div class="col-12" style="display: flex; justify-content: space-between;">
                <div class="card-counter danger">
                    <i class="fa fa-flask"></i>
                    <div>
                        <span class="count-name stat-text">Total Pending Tests</span>
                        <span class="count-numbers stat-digit"></span>
                    </div>
                </div>

                <div class="card-counter pham">
                    <i class="fa fa-user-md"></i>
                    <div>
                        <span class="count-name stat-text">Total Test Done</span>
                        <span class="count-numbers stat-digit"></span>
                    </div>
                </div>

                <div class="card-counter success">
                    <i class="fa fa-money"></i>
                    <div>
                        <span class="count-name stat-text">Total amount collected</span>
                        <span class="count-numbers stat-digit"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
