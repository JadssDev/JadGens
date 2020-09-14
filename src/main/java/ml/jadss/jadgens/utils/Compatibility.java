package ml.jadss.jadgens.utils;

import com.cryptomorin.xseries.XMaterial;
import ml.jadss.jadgens.JadGens;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class Compatibility {

    public Compatibility() {
        return;
    }

    public String aDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String bDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String cDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String dDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String eDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String fDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String gDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String hDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String iDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String jDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String kDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String lDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String mDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String oDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String pDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String qDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String rDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String sDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String tDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String uDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String vDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String wDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String xDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String yDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    public String zDontusethis() {
        return "DON'T USE THIS METHOD! IT IS FOR COMPATIBILITY FOR ALL THE VERSION SUPPORTED BY THE PLUGIN!";
    }

    private String getMaterial(String block) {
        if (block.equalsIgnoreCase("STAINED_GLASS_PANE")) return glassPane();
        if (block.equalsIgnoreCase("WOOL")) return wool();
        return block;
    }

    private String glassPane() {
        try {
            Material m = Enum.valueOf(Material.class, "GLASS_PANE");
            return m.name();
        } catch (IllegalArgumentException exception) {
            return Material.STAINED_GLASS_PANE.name();
        }
    }

    private boolean isGlassPane(String name) {
        if (name.equalsIgnoreCase("STAINED_GLASS_PANE")) {
            return true;
        }
        return false;
    }

    private String wool() {
        try {
            Material m = Enum.valueOf(Material.class, "WHITE_WOOL");
            return m.name();
        } catch (IllegalArgumentException exception) {
            return Material.WOOL.name();
        }
    }

    private boolean isWool(String name) {
        if (name.equalsIgnoreCase("WOOL")) {
            return true;
        }
        return false;
    }

    public String getTitle(Inventory inv, InventoryView invView) {
        try {
            String var = inv.getName();
            return var;
        } catch (NoSuchMethodError e) {
            return invView.getTitle();
        }
    }

    public Material matParser(String s) {
        String initial = s;
        if (!JadGens.getInstance().getCompMode()) {
            if (isGlassPane(initial)) return Material.getMaterial(glassPane());
            if (isWool(initial)) return Material.getMaterial(wool());

            initial = getMaterial(s);
            XMaterial xMaterial = XMaterial.valueOf(initial);
            return xMaterial.parseMaterial();
        } else {
            return Material.getMaterial(initial);
        }
    }
}
