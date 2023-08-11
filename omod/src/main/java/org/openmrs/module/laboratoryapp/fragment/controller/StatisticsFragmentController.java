package org.openmrs.module.laboratoryapp.fragment.controller;

import org.openmrs.ui.framework.fragment.FragmentModel;
import java.text.ParseException;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.EhrDepartment;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.ui.framework.SimpleObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;


public class StatisticsFragmentController {

    public void controller(FragmentModel model) throws ParseException {
    }
    // this method functionality does summation of total amount collected on the lab test done
    private Double getLaboratoryTotalOnDateRange(HospitalCoreService hospitalCoreService, Date startDate, Date endDate, EhrDepartment ehrDepartment) {
        double total = 0.0;

        List<PatientServiceBillItem> getBilledItemsPerDepartment = hospitalCoreService.getPatientServiceBillByDepartment(ehrDepartment, startDate, endDate);
        if (getBilledItemsPerDepartment.size() > 0) {
            for (PatientServiceBillItem patientServiceBillItem : getBilledItemsPerDepartment) {
                if (patientServiceBillItem.getActualAmount() != null) {
                    total += patientServiceBillItem.getActualAmount().doubleValue();
                }
            }
        }
        return total;
    }

    //this method functionality counts the total number of test done
    private int countPaidTestsOnDateRange(HospitalCoreService hospitalCoreService, Date startDate, Date endDate, EhrDepartment ehrDepartment) {
        int count = 0;
        List<PatientServiceBillItem> getBilledItemsPerDepartment = hospitalCoreService.getPatientServiceBillByDepartment(ehrDepartment, startDate, endDate);
        if (getBilledItemsPerDepartment.size() > 0) {
            for (PatientServiceBillItem patientServiceBillItem : getBilledItemsPerDepartment) {
                if (patientServiceBillItem.getActualAmount() != null && patientServiceBillItem.getActualAmount().compareTo(BigDecimal.ZERO) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // This method functionality counts the total number of pending tests or tests that are not paid
    private int countPendingTestsOnDateRange(HospitalCoreService hospitalCoreService, Date startDate, Date endDate, EhrDepartment ehrDepartment) {
        int count = 0;
        List<PatientServiceBillItem> getBilledItemsPerDepartment = hospitalCoreService.getPatientServiceBillByDepartment(ehrDepartment, startDate, endDate);
        if (getBilledItemsPerDepartment.size() > 0) {
            for (PatientServiceBillItem patientServiceBillItem : getBilledItemsPerDepartment) {
                if (patientServiceBillItem.getActualAmount() == null || patientServiceBillItem.getActualAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public SimpleObject getLaboratoryTotalOnDateRange(@RequestParam(value = "fromDate", required = false) Date startDate,
                                                      @RequestParam(value = "toDate", required = false) Date endDate) throws ParseException {
        HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);

        SimpleObject simpleObject = new SimpleObject();

        simpleObject.put(
                "laboratory",
                getLaboratoryTotalOnDateRange(hospitalCoreService, startDate, endDate,
                        hospitalCoreService.getDepartmentByName("Laboratory")));
        simpleObject.put(
                "totaltestdone",
                countPaidTestsOnDateRange(hospitalCoreService, startDate, endDate,
                        hospitalCoreService.getDepartmentByName("Laboratory")));
        return simpleObject;
    }
}
