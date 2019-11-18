package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;

import com.doannganh.salesmobileassistant.Manager.DAO.EmployeeDAO;
import com.doannganh.salesmobileassistant.model.Employee;

public class EmployeePresenter {
    private static EmployeePresenter instance;
    EmployeeDAO employeeDAO;
    Context context;

    private EmployeePresenter(){

    }

    public static EmployeePresenter Instance(Context context)
    {
        if (instance == null){
            instance = new EmployeePresenter();
        }

        instance.employeeDAO = EmployeeDAO.Instance(context);
        instance.context = context;
        return EmployeePresenter.instance;
    }
    public Employee getEmployee(String id){
        return employeeDAO.getEmployee(id);
    }
}
