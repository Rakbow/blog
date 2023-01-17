package com.rakbow.website.data.emun.game;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public enum GamePlatform {

    UNKNOWN(0, "Unknown", "Unknown"),
    PC(1, "PC", "PC"),
    WEB(2, "Web", "Web"),
    ANDROID(3, "Android", "Android"),
    APPLE_IOS(4, "Apple iOS", "Apple iOS"),
    PS(5, "PS", "Sony PlayStation"),
    PS2(6, "PS2", "Sony PlayStation 2"),
    PS3(7, "PS3", "Sony PlayStation 3"),
    PS4(8, "PS4", "Sony PlayStation 4"),
    PS5(9, "PS5", "Sony PlayStation 5"),
    PSV(10, "PSV", "Sony PlayStation Vita"),
    PSP(11, "PSP", "Sony PlayStation Portable"),
    XBOX_360(12, "Xbox 360", "Microsoft Xbox 360"),
    XBOX_ONE(13, "Xbox One", "Microsoft Xbox One"),
    XBOX_SERIES_X(14, "Xbox Series X", "Microsoft Xbox Series X"),
    XBOX_SERIES_S(15, "Xbox Series S", "Microsoft Xbox Series S"),
    SWITCH(16, "NS", "Nintendo Switch"),
    N64(17, "N64", "Nintendo 64"),
    NGC(18, "NGC", "Nintendo GameCube"),
    GBC(19, "GBC", "Nintendo Game Boy Color"),
    NDS(20, "NDS", "Nintendo DS"),
    NINTENDO_3DS(21, "3DS", "Nintendo 3DS"),
    NINTENDO_WII(22, "Wii", "Nintendo Wii"),
    NINTENDO_WII_U(23, "Wii U", "Nintendo Wii U"),
    ARCADE(24, "Arcade", "Arcade"),
    NEC_PC_ENGINE(25, "NEC PC-Engine", "NEC PC-Engine"),
    SEGA_MEGA_DRIVE_GENESIS(26, "Genesis/MD", "Sega Mega Drive (Genesis)"),
    SEGA_SATURN(27, "Saturn", "Sega Saturn"),
    SEGA_DREAMCAST(28, "Dreamcast", "Sega Dreamcast");

    @Getter
    private final int index;
    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;

    public static String index2Name (int index) {
        String nameEn = UNKNOWN.nameEn;
        for (GamePlatform gamePlatform : GamePlatform.values()) {
            if (gamePlatform.index == index) {
                nameEn = gamePlatform.nameEn;
            }
        }
        return nameEn;
    }

    public static JSONArray getGamePlatformSet () {
        JSONArray list = new JSONArray();
        for (GamePlatform platform : GamePlatform.values()) {
            JSONObject jo = new JSONObject();
            jo.put("labelEn", platform.nameEn);
            jo.put("labelZh", platform.nameZh);
            jo.put("value", platform.index);
            list.add(jo);
        }
        return list;
    }

    public static JSONObject getGamePlatformJson(int platformId) {
        JSONObject platform = new JSONObject();
        platform.put("id", platformId);
        platform.put("nameEn", GamePlatform.index2Name(platformId));
        return platform;
    }

}
