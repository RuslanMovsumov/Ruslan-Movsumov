 
class Person.java {

    private String fullName;
    private String position;
    private String email;
    private String phone;
    private double salary;
    private int age;

    // Конструктор класса
    public Person(String fullName, String position, String email, String phone, double salary, int age) {
        this.fullName = fullName;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.age = age;
    }

    // Метод для вывода информации об объекте
    public void displayInfo() {
        System.out.println("ФИО: " + fullName);
        System.out.println("Должность: " + position);
        System.out.println("Email: " + email);
        System.out.println("Телефон: " + phone);
        System.out.println("Зарплата: " + salary);
        System.out.println("Возраст: " + age); 
    }
    
    public static void main(String[] args) {
        // Объявляем массив из 5 сотрудников
        Person[] persArray = new Person[5];

        // Заполняем массив данными сотрудников
        persArray[0] = new Person("Гордей Никита Валерьевич", "Инженер", "gordey@example.com", "+7 999 111 11 11", 50000, 50);
        persArray[1] = new Person("Чубинский Иван Владиславович", "Менеджер", "c4ubausv@example.com", "+7 999 222 22 22", 60000, 55);
        persArray[2] = new Person("Мовсумов Руслан Раулович", "Аналитик", "mowsum@example.com", "+7 999 333 33 33", 70000, 26);
        persArray[3] = new Person("Леонов Николай Витальевич", "Разработчик", "leonov@example.com", "+7 999 444 44 44", 80000, 40);
        persArray[4] = new Person("Ропот Олег Дмитриевич", "Дизайнер", "ropot@example.com", "+7 999 555 55 55", 65000, 32);

        // Вывод информации о каждом сотруднике
        System.out.println("Информация о сотрудниках:");
        for (Person person : persArray) {
            person.displayInfo(); 
        }
    }
}
