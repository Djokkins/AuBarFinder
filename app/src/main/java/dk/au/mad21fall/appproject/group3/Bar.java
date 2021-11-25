package dk.au.mad21fall.appproject.group3;

public class Bar {

    private String Name;
    private String Description;
    private String Address;
    private String OpeningHours;
    private String Faculties;
    private String Facebook;
    private String Instagram;
    private String Location;

    //In coordinates? Use this to calculate distance at least, if we
    //cant do it by the address.

    public Bar(){
        Name = "";
        Description = "";
        Address = "";
        OpeningHours = "";
        Faculties = "";
        Facebook = "";
        Instagram = "";
        Location = "";
    }

    public Bar(String name, String description, String address, String openingHours, String faculties, String facebook, String instagram, String location) {
        Name = name;
        Description = description;
        Address = address;
        OpeningHours = openingHours;
        Faculties = faculties;
        Facebook = facebook;
        Instagram = instagram;
        Location = location;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setOpeningHours(String openingHours) {
        OpeningHours = openingHours;
    }

    public void setFaculties(String faculties) {
        Faculties = faculties;
    }

    public void setFacebook(String facebook) {
        Facebook = facebook;
    }

    public void setInstagram(String instagram) {
        Instagram = instagram;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getAddress() {
        return Address;
    }

    public String getOpeningHours() {
        return OpeningHours;
    }

    public String getFaculties() {
        return Faculties;
    }

    public String getFacebook() {
        return Facebook;
    }

    public String getInstagram() {
        return Instagram;
    }

    public String getLocation() {
        return Location;
    }
}
