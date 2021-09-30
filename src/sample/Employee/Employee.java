package sample.Employee;

public class Employee {
    int idemployee;
    String firstname;
    String lastname;
    String vacancy;
    String gender;
    int age;
    int salary;
    int phone;

    public Employee(int idemployee, String firstname, String lastname, String vacancy, String gender, int age, int salary, int phone) {
        this.idemployee = idemployee;
        this.firstname = firstname;
        this.lastname = lastname;
        this.vacancy = vacancy;
        this.gender = gender;
        this.age = age;
        this.salary = salary;
        this.phone = phone;
    }

    public int getIdemployee() {
        return idemployee;
    }

    public void setIdemployee(int idemployee) {
        this.idemployee = idemployee;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
