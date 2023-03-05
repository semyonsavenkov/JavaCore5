public class Employee {

    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee(long l, String firstName, String lastName) {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

}
