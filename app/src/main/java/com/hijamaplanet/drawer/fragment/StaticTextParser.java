package com.hijamaplanet.drawer.fragment;

import com.hijamaplanet.utils.AppConstants;

import java.util.ArrayList;

import static com.hijamaplanet.utils.AppConstants.ASHFIA;
import static com.hijamaplanet.utils.AppConstants.MASNUN_AMOL;
import static com.hijamaplanet.utils.AppConstants.RUQYAH_SHARIYAH;

public class StaticTextParser {
    private String fragmentName;
    private StaticTextLibary libary = new StaticTextLibary();

    private ArrayList<String> ashfiaHeader = libary.getAshfiaHeaders();
    private ArrayList<String> ashfiaText = libary.getAshfiaExpandableTexts();
    private ArrayList<String> ruqyahshariyahHeader = libary.getRuqyahShariyahHeaders();
    private ArrayList<String> ruqyahshariyahText = libary.getRuqyahShariyahExpandableTexts();
    private ArrayList<String> masnunHeader = libary.getMasnunHeader();
    private ArrayList<String> masnunText = libary.getMasnunExpandableTexts();

    public StaticTextParser (String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getToolbarText() {
        if (ASHFIA.equalsIgnoreCase(fragmentName)) return "রুকইয়াহ ডিটক্স";
        else if (RUQYAH_SHARIYAH.equalsIgnoreCase(fragmentName)) return "রুকইয়াহ শারইয়্যাহ";
        else if (MASNUN_AMOL.equalsIgnoreCase(fragmentName)) return "মাসনুন আমল";

        return null;
    }

    public int getElementSize() {
        if (ASHFIA.equalsIgnoreCase(fragmentName)) return ashfiaHeader.size();
        else if (RUQYAH_SHARIYAH.equalsIgnoreCase(fragmentName)) return ruqyahshariyahHeader.size();
        else if (MASNUN_AMOL.equalsIgnoreCase(fragmentName)) return masnunHeader.size();
        return 0;
    }

    public String getHeaderText(int position) {
        if (ASHFIA.equalsIgnoreCase(fragmentName)) return ashfiaHeader.get(position);
        else if (RUQYAH_SHARIYAH.equalsIgnoreCase(fragmentName)) return ruqyahshariyahHeader.get(position);
        else if (MASNUN_AMOL.equalsIgnoreCase(fragmentName)) return masnunHeader.get(position);
        return null;
    }

    public String getExpandableText(int position) {
        if (ASHFIA.equalsIgnoreCase(fragmentName)) return ashfiaText.get(position);
        else if (RUQYAH_SHARIYAH.equalsIgnoreCase(fragmentName)) return ruqyahshariyahText.get(position);
        else if (MASNUN_AMOL.equalsIgnoreCase(fragmentName)) return masnunText.get(position);
        return null;
    }
}
