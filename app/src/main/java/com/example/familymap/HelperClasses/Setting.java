package com.example.familymap.HelperClasses;

import java.util.HashMap;
import java.util.Map;

public class Setting
{
    private Boolean lifeStoryLines;
    private Boolean familyTreeLines;
    private Boolean spouseLines;

    private Map<String, String> lineColors;
    private Map<String, String> markerColors;

    private String mapType;
    private String[] colors;
    private int colorsIndex;

    public Setting(Boolean lifeStoryLines, Boolean familyTreeLines, Boolean spouseLines, String mapType)
    {
        this.lifeStoryLines = lifeStoryLines;
        this.familyTreeLines = familyTreeLines;
        this.spouseLines = spouseLines;
        this.mapType = mapType;

        lineColors = new HashMap<>();
        markerColors = new HashMap<>();
        InitializeColorsArray();
        SetDefaultLineColors();
        SetDefaultMarkerColors();
    }

    private void InitializeColorsArray()
    {
        colors = new String[10];
        colorsIndex = 0;

        colors[0] = "Red";
        colors[1] = "Blue";
        colors[2] = "Green";
        colors[3] = "Purple";
        colors[4] = "Rose";
        colors[5] = "Orange";
        colors[6] = "Cyan";
        colors[7] = "Magenta";
        colors[8] = "Yellow";
        colors[9] = "Azure";
    }

    public void SetLineColorInMap(String key, String value)
    {
        lineColors.put(key, value);
    }

    public String GetLineColorValueForKey(String key)
    {
        return lineColors.get(key);
    }

    public String GetMarkerColorValueForKey(String key)
    {
        String result = markerColors.get(key);

        if (result == null)
        {
            result = colors[colorsIndex];
            markerColors.put(key, colors[colorsIndex]);
            colorsIndex++;

            if (colorsIndex == colors.length)
            {
                colorsIndex = 0;
            }
        }

        return result;
    }

    public void SetLifeStoryLines(Boolean lifeStoryLines)
    {
        this.lifeStoryLines = lifeStoryLines;
    }

    public void SetFamilyTreeLines(Boolean familyTreeLines)
    {
        this.familyTreeLines = familyTreeLines;
    }

    public void SetSpouseLines(Boolean spouseLines)
    {
        this.spouseLines = spouseLines;
    }

    public void SetMapType(String mapType)
    {
        this.mapType = mapType;
    }

    public Boolean GetLifeStoryLines()
    {
        return lifeStoryLines;
    }

    public Boolean GetFamilyTreeLines()
    {
        return familyTreeLines;
    }

    public Boolean GetSpouseLines()
    {
        return spouseLines;
    }

    public String GetMapType()
    {
        return mapType;
    }

    private void SetDefaultLineColors()
    {
        lineColors.put("lifeStory", "Red");
        lineColors.put("familyTree", "Blue");
        lineColors.put("spouse", "Green");
    }

    private void SetDefaultMarkerColors()
    {
        markerColors.put("Birth", "Red");
        markerColors.put("Baptism", "Blue");
        markerColors.put("Marriage", "Green");
    }
}
