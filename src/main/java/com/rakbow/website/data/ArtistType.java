package com.rakbow.website.data;

import com.rakbow.website.entity.Music;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-12 18:04
 * @Description: 专辑，音乐的创作人员职位类型枚举类
 */
@AllArgsConstructor
public enum ArtistType {
    VOCAL(1,"演唱", "Vocal"),
    COMPOSER(2,"作曲", "Composer"),
    ARRANGER(2,"编曲", "Arranger"),
    LYRICIST(2,"作词", "Lyricist"),
    CHORUS_ARRANGEMENT(3,"和声编曲", "Chorus Arrangement"),
    STRINGS(4,"弦乐", "Strings"),
    GUITARS(4,"吉他", "Guitars"),
    ELECTRIC_GUITAR(5,"电吉他", "Electric Guitar"),
    ACOUSTIC_GUITAR(6,"古典吉他", "Acoustic Guitar"),
    PROGRAMMING(7,"音乐工程", "Programming"),
    Bass(8,"贝斯", "Bass"),
    RECORDED_BY(9,"录制", "Recorded by"),
    MIXED_BY(9,"混音", "Mixed by"),
    RECORDING_ENGINEER(9,"录音工程师", "Recording Engineer"),
    MIXING_ENGINEER(9,"混音工程师", "Mixing Engineer"),
    RECORDING_STUDIO(9,"录音工作室", "Recording Studio"),
    MIXING_STUDIO(9,"混音工作室", "Mixing Studio"),
    MASTERING_ENGINEER(9,"母带后期处理", "Mastering Engineer"),
    MASTERING_STUDIO(9,"母带后期处理工作室", "Mastering Studio"),
    PROGRAMMER(9,"采样", "Programmer"),
    PERFORMER(9,"演奏", "Performer"),
    MUSIC_VIDEO_PRODUCER(9,"MV制作", "Music Video Producer"),
    MUSIC_VIDEO_DIRECTOR(9,"MV导演", "Music Video Director"),
    PRODUCER(9,"制片人", "Producer"),
    GENERAL_PRODUCER(9,"出品人", "General Producer"),
    EXECUTIVE_PRODUCER(9,"监制", "Executive Producer"),
    PRODUCTION_MANAGER(9,"制片经理", "Production Manager"),
    CAMERA(9,"摄影", "Camera"),
    DANCER(9,"舞蹈演员", "Dancer"),
    DIRECTOR(9,"导演", "Director"),
    ASSISTANT_DIRECTOR(9,"助理导演", "Assistant Director"),
    ARTIST_AND_REPERTOIRE(9,"A&R", "A&R"),
    PROMOTION(9,"推广", "Promotion"),
    SALES_PROMOTION(9,"营业推广", "Sales Promotion"),
    ORGANIZATION_CONTROL(9,"组织管理", "Organization Control"),
    ILLUSTRATION(9,"插画", "Illustration"),
    ART_DIRECTION(9,"艺术指导", "Art Direction"),
    DESIGN(9,"设计", "Design"),
    ASSISTANT_DESIGN(9,"助理设计", "Assistant Design"),
    PHOTOGRAPH(9,"摄像", "Photograph"),
    HAIR(9,"化妆·发型", "Hair"),
    MAKE_UP(9,"化妆", "Make-up"),
    STYLING(9,"造型设计", "Styling"),
    LOCATION_COOPERATION(9,"", "Location Cooperation"),
    PAINT(9,"", "Paint"),
    BACKGROUND_ART(9,"", "Background Art"),
    ARTIST_MANAGEMENT(9,"", "Artist Management"),
    SPECIAL_THANKS(9,"特别鸣谢", "Special Thanks");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
