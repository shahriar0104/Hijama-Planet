package com.hijamaplanet.location;

public class LocationAddress {

    int postalCode;

    public LocationAddress(String postalCode){
        this.postalCode = Integer.valueOf(postalCode);
    }

    public String getPostalAddress(){
        if (postalCode >= 1360 && postalCode <= 1362)
            return "Demra";
        else if (postalCode == 1206)
            return "Dhaka Cantt.";
        else if (postalCode == 1351)
            return "Dhamrai";
        else if (postalCode == 1209)
            return "Dhanmondi";
        else if (postalCode >= 1212 && postalCode <= 1213)
            return "Gulshan";
        else if (postalCode == 1232)
            return "Jatrabari";
        else if (postalCode >= 1330 && postalCode <= 1332)
            return "Joypara";
        else if (postalCode >= 1310 && postalCode <= 1313)
            return "Keraniganj";
        else if (postalCode == 1219)
            return "Khilgaon";
        else if (postalCode == 1229)
            return "Khilkhet";
        else if (postalCode == 1211)
            return "Lalbag";
        else if (postalCode == 1216)
            return "Mirpur";
        else if (postalCode == 1207 || postalCode == 1225)
            return "Mohammadpur";
        else if (postalCode == 1222 || postalCode == 1223)
            return "Motijheel";
        else if (postalCode >= 1320 && postalCode <= 1325)
            return "Nawabganj";
        else if (postalCode == 1205)
            return "New Market";
        else if (postalCode == 1000)
            return "Palton";
        else if (postalCode == 1217)
            return "Ramna";
        else if (postalCode == 1214)
            return "Sabujbag";
        else if (postalCode >= 1340 && postalCode <= 1349)
            return "Savar";
        else if (postalCode == 1100 || postalCode == 1203 || postalCode == 1204)
            return "Sutrapur";
        else if (postalCode == 1208 || postalCode == 1215)
            return "Tejgaon";
        else if (postalCode == 1230)
            return "Uttara";
        else
            return "UnKnown";
    }
}
