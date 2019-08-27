package service;

import Util.HibernateUtil;
import entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebService
public class EmployeeService {

    @WebMethod
    public Employee createEmployee(Employee employee) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            employee.setId(Calendar.getInstance().getTimeInMillis());
            session.save(employee);
            transaction.commit();
            return employee;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @WebMethod
    public Employee editEmployee(Employee employee) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            Query<Employee> query = session.createQuery("from Employee e where e.id=:id");
            query.setParameter("id", employee.getId());
            Employee employee1 = query.getSingleResult();
            if (employee1 == null) {
                return null;
            }
            employee1.setName(employee.getName());
            employee1.setSalary(employee.getSalary());
            session.update(employee1);
            transaction.commit();
            return employee1;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @WebMethod
    public List<Employee> getListEmloyees() {
        Session session = HibernateUtil.getSession();
        Query<Employee> query = session.createQuery("from Employee", Employee.class);
        List<Employee> employees = query.list();
        return employees;
    }

    public Employee getById(long id) {
        Session session = HibernateUtil.getSession();
        Query<Employee> query = session.createQuery("from Employee e where e.id=:id");
        query.setParameter("id", id);
        Employee employee1 = query.getSingleResult();
        return  employee1;
    }
}
