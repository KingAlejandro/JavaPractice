public class Contact {
    private String name;
    private String phoneNumber;
    private String email;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = "";
    }

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void printName() {
        System.out.println(name);
    }

    public void printPhoneNumber() {
        System.out.println(phoneNumber);
    }

    public void printEmail() {
        if (!email.equals("")) {
            System.out.println(email);
        } else {
            System.out.println("Email not provided.");
        }
    }

    public void printContact() {
        if (!email.equals("")) {
            System.out.println("Name: " + name + "; Phone number: " + phoneNumber + "; Email: " + email);
        } else {
            System.out.println("Name: " + name + "; Phone number: " + phoneNumber);
        }
    }
}
