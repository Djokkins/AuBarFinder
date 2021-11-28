package dk.au.mad21fall.appproject.group3;

public class Bar {

    private String Name;
    private String Description;
    private String Address;
    private String OpeningHours;
    private String Faculties;
    private String Facebook;
    private String Instagram;
    private String Open;
    private String Close;



    public String getBarID() {
        return barID;
    }

    public void setBarID(String barID) {
        this.barID = barID;
    }

    private String barID;

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
        Open = "";
        Close = "";
    }

    public Bar(String name, String description, String address, String openingHours, String faculties, String facebook, String instagram, String open, String close) {
        Name = name;
        Description = description;
        Address = address;
        OpeningHours = openingHours;
        Faculties = faculties;
        Facebook = facebook;
        Instagram = instagram;
        Open = open;
        Close = close;
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

    public void setOpen(String open) {
        Open = open;
    }

    public void setClose(String close) {
        Close = close;
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

    public String getOpen() {
        return Open;
    }

    public String getClose() {
        return Close;
    }





}
